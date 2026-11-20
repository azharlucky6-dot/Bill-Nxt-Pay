package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.NxtPayDatabase
import com.example.data.repository.NxtPayRepository
import com.example.ui.ActiveModal
import com.example.ui.NxtPayTab
import com.example.ui.NxtPayViewModel
import com.example.ui.components.FloatingBottomNavigationBar
import com.example.ui.components.FloatingToastBanner
import com.example.ui.components.NxtPayModalHost
import com.example.ui.components.NxtPayTopBar
import com.example.ui.screens.CardsManageScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoanScreen
import com.example.ui.screens.SupportScreen
import com.example.ui.theme.NxtPayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = NxtPayDatabase.getInstance(applicationContext)
        val repository = NxtPayRepository(database.nxtPayDao())
        val viewModelFactory = NxtPayViewModel.Factory(repository)

        setContent {
            NxtPayTheme {
                val viewModel: NxtPayViewModel = viewModel(factory = viewModelFactory)
                NxtPayApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NxtPayApp(viewModel: NxtPayViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ext = NxtPayTheme.extended

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(ext.shellBgTop, ext.shellBgMid, ext.shellBgBottom)
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 560.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                NxtPayTopBar(
                    cashBalance = uiState.cashBalance,
                    onSecurityClick = { viewModel.openModal(ActiveModal.SecurityTerms) },
                    onBrandClick = { viewModel.navigateToHome() },
                    onWalletClick = { viewModel.openModal(ActiveModal.WalletRewards) }
                )

                Crossfade(
                    targetState = uiState.activeTab,
                    animationSpec = tween(durationMillis = 220),
                    label = "nxt_pay_tab_crossfade",
                    modifier = Modifier.weight(1f)
                ) { tab ->
                    when (tab) {
                        NxtPayTab.HOME -> HomeScreen(
                            uiState = uiState,
                            onCardIndexChanged = viewModel::selectCardIndex,
                            onPayCardClick = viewModel::onPayCardClicked,
                            onOpenCardMarketplace = { viewModel.openModal(ActiveModal.CardMarketplace) },
                            onBusinessPaymentClick = {
                                viewModel.openModal(ActiveModal.BusinessPayment(isInvoice = false))
                            },
                            onGstClick = {
                                viewModel.showToast("GST Challan payments launching soon!")
                            },
                            onUtilitiesClick = { viewModel.openModal(ActiveModal.Utilities) },
                            onPayInvoiceClick = {
                                viewModel.openModal(ActiveModal.BusinessPayment(isInvoice = true))
                            },
                            onWalletClick = { viewModel.openModal(ActiveModal.WalletRewards) },
                            onSellerHubClick = { viewModel.openModal(ActiveModal.SellerHub) },
                            onActivateWalletClick = viewModel::activateWallet,
                            onReferEarnClick = { viewModel.openModal(ActiveModal.ReferEarn) },
                            onViewAllHistoryClick = {
                                viewModel.navigateToTabDirect(NxtPayTab.HISTORY)
                            },
                            onPayUpcomingClick = viewModel::onPayUpcomingClicked
                        )

                        NxtPayTab.LOAN -> LoanScreen(
                            onBackToHome = viewModel::navigateToHome,
                            onCheckEligibility = viewModel::onLoanEligibilityClicked
                        )

                        NxtPayTab.CARDS -> CardsManageScreen(
                            cards = uiState.cards,
                            onBackToHome = viewModel::navigateToHome,
                            onLinkCardClick = { viewModel.openModal(ActiveModal.LinkCard) },
                            onSelectCard = viewModel::viewCardOnHome
                        )

                        NxtPayTab.HISTORY -> HistoryScreen(
                            transactions = uiState.transactions,
                            onBackToHome = viewModel::navigateToHome
                        )

                        NxtPayTab.SUPPORT -> SupportScreen(
                            onBackToHome = viewModel::navigateToHome,
                            onReadTermsClick = { viewModel.openModal(ActiveModal.SecurityTerms) }
                        )
                    }
                }
            }

            FloatingBottomNavigationBar(
                activeTab = uiState.activeTab,
                onSelectTab = viewModel::selectTab,
                onPayNowClick = { viewModel.openModal(ActiveModal.Utilities) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            FloatingToastBanner(
                message = uiState.toastMessage,
                onDismiss = viewModel::dismissToast,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }

    NxtPayModalHost(
        activeModal = uiState.activeModal,
        cashBalance = uiState.cashBalance,
        demoOtp = uiState.demoOtp,
        onDismiss = viewModel::closeModal,
        onAuthorizePayment = viewModel::authorizePayment,
        onProceedUtility = viewModel::proceedUtilityPayment,
        onProceedBusiness = viewModel::proceedBusinessPayment,
        onDsaApply = viewModel::onDsaApplyClicked,
        onSaveNewCard = viewModel::saveLinkedCard,
        onCopiedToast = viewModel::showToast
    )
}
