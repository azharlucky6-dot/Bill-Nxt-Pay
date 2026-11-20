package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.CreditCardEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UpcomingBillEntity
import com.example.data.repository.NxtPayRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NxtPayTab {
    HOME,
    LOAN,
    CARDS,
    HISTORY,
    SUPPORT
}

sealed interface ActiveModal {
    data object None : ActiveModal
    data class PaymentGate(
        val billerName: String,
        val amount: Long,
        val cardIdToSettle: String? = null,
        val upcomingBillIdToRemove: String? = null
    ) : ActiveModal
    data object Utilities : ActiveModal
    data object SecurityTerms : ActiveModal
    data object WalletRewards : ActiveModal
    data object CardMarketplace : ActiveModal
    data object ReferEarn : ActiveModal
    data object LinkCard : ActiveModal
    data class BusinessPayment(val isInvoice: Boolean) : ActiveModal
    data object SellerHub : ActiveModal
}

data class NxtPayUiState(
    val activeTab: NxtPayTab = NxtPayTab.HOME,
    val activeCardIndex: Int = 0,
    val walletActivated: Boolean = false,
    val cashBalance: Long = 500L,
    val demoOtp: String = "482910",
    val cards: List<CreditCardEntity> = emptyList(),
    val upcomingBills: List<UpcomingBillEntity> = emptyList(),
    val transactions: List<TransactionEntity> = emptyList(),
    val activeModal: ActiveModal = ActiveModal.None,
    val toastMessage: String? = null
)

class NxtPayViewModel(
    private val repository: NxtPayRepository
) : ViewModel() {

    private val _activeTab = MutableStateFlow(NxtPayTab.HOME)
    private val _activeCardIndex = MutableStateFlow(0)
    private val _activeModal = MutableStateFlow<ActiveModal>(ActiveModal.None)
    private val _toastMessage = MutableStateFlow<String?>(null)
    private var toastJob: Job? = null

    private data class UiControlState(
        val activeTab: NxtPayTab,
        val activeCardIndex: Int,
        val activeModal: ActiveModal,
        val toastMessage: String?
    )

    private val controlFlow = combine(
        _activeTab,
        _activeCardIndex,
        _activeModal,
        _toastMessage
    ) { tab, cardIndex, modal, toast ->
        UiControlState(tab, cardIndex, modal, toast)
    }

    val uiState: StateFlow<NxtPayUiState> = combine(
        repository.cards,
        repository.upcomingBills,
        repository.transactions,
        repository.walletProfile,
        controlFlow
    ) { cards, upcoming, transactions, wallet, controls ->
        val safeCardIndex = if (cards.isEmpty()) 0 else controls.activeCardIndex.coerceIn(0, cards.lastIndex)
        NxtPayUiState(
            activeTab = controls.activeTab,
            activeCardIndex = safeCardIndex,
            walletActivated = wallet.walletActivated,
            cashBalance = wallet.cashBalance,
            cards = cards,
            upcomingBills = upcoming,
            transactions = transactions,
            activeModal = controls.activeModal,
            toastMessage = controls.toastMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NxtPayUiState()
    )

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    fun selectTab(tab: NxtPayTab) {
        _activeTab.value = if (_activeTab.value == tab && tab != NxtPayTab.HOME) {
            NxtPayTab.HOME
        } else {
            tab
        }
    }

    fun navigateToHome() {
        _activeTab.value = NxtPayTab.HOME
    }

    fun navigateToTabDirect(tab: NxtPayTab) {
        _activeTab.value = tab
    }

    fun selectCardIndex(index: Int) {
        _activeCardIndex.value = index.coerceAtLeast(0)
    }

    fun viewCardOnHome(index: Int) {
        _activeCardIndex.value = index.coerceAtLeast(0)
        _activeTab.value = NxtPayTab.HOME
    }

    fun openModal(modal: ActiveModal) {
        _activeModal.value = modal
    }

    fun closeModal() {
        _activeModal.value = ActiveModal.None
    }

    fun showToast(message: String) {
        toastJob?.cancel()
        _toastMessage.value = message
        toastJob = viewModelScope.launch {
            delay(3_200L)
            _toastMessage.value = null
        }
    }

    fun dismissToast() {
        toastJob?.cancel()
        _toastMessage.value = null
    }

    fun onPayCardClicked(card: CreditCardEntity) {
        if (card.dueAmount <= 0L) {
            showToast("${card.bank} •••• ${card.last4} is already paid.")
            return
        }
        openModal(
            ActiveModal.PaymentGate(
                billerName = "${card.bank} •••• ${card.last4}",
                amount = card.dueAmount,
                cardIdToSettle = card.id
            )
        )
    }

    fun onPayUpcomingClicked(bill: UpcomingBillEntity) {
        openModal(
            ActiveModal.PaymentGate(
                billerName = bill.title,
                amount = bill.amount,
                upcomingBillIdToRemove = bill.id
            )
        )
    }

    fun proceedUtilityPayment(category: String, consumerId: String, amountText: String) {
        val cleanId = consumerId.trim()
        val amount = amountText.trim().toLongOrNull() ?: 0L
        if (cleanId.isEmpty() || amount <= 0L) {
            showToast("Enter a valid ID and positive amount.")
            return
        }
        openModal(
            ActiveModal.PaymentGate(
                billerName = "$category ($cleanId)",
                amount = amount
            )
        )
    }

    fun proceedBusinessPayment(vendorName: String, amountText: String) {
        val cleanVendor = vendorName.trim().ifEmpty { "Vendor Payout" }
        val amount = amountText.trim().toLongOrNull() ?: 0L
        if (amount <= 0L) {
            showToast("Enter a valid positive payout amount.")
            return
        }
        openModal(
            ActiveModal.PaymentGate(
                billerName = cleanVendor,
                amount = amount
            )
        )
    }

    fun authorizePayment(gate: ActiveModal.PaymentGate, enteredOtp: String) {
        val demoOtp = uiState.value.demoOtp
        if (enteredOtp.trim() != demoOtp) {
            showToast("Invalid OTP. Use demo code $demoOtp.")
            return
        }
        viewModelScope.launch {
            val refId = repository.executePayment(
                billerName = gate.billerName,
                amount = gate.amount,
                cardIdToSettle = gate.cardIdToSettle,
                upcomingBillIdToRemove = gate.upcomingBillIdToRemove
            )
            _activeModal.value = ActiveModal.None
            showToast("Payment successful! Ref: $refId (+₹25 NXT Cash)")
        }
    }

    fun activateWallet() {
        viewModelScope.launch {
            val newlyActivated = repository.activateWallet()
            if (newlyActivated) {
                showToast("Closed-loop Wallet NXT activated (+₹100 bonus)!")
            } else {
                showToast("Wallet NXT is already active!")
            }
        }
    }

    fun saveLinkedCard(
        bankName: String,
        last4: String,
        dueText: String,
        label: String,
        network: String
    ) {
        val cleanBank = bankName.trim().ifEmpty { "AXIS BANK" }.uppercase()
        val cleanLast4 = last4.filter { it.isDigit() }.takeLast(4).ifEmpty { "5512" }
        val due = dueText.trim().toLongOrNull() ?: 1200L

        viewModelScope.launch {
            val newIndex = repository.linkNewCard(
                bankName = cleanBank,
                last4 = cleanLast4,
                dueAmount = due,
                label = label.trim().ifEmpty { "Rewards" },
                network = network.ifBlank { "VISA" }
            )
            _activeCardIndex.value = newIndex
            _activeModal.value = ActiveModal.None
            _activeTab.value = NxtPayTab.HOME
            showToast("$cleanBank •••• $cleanLast4 linked!")
        }
    }

    fun onDsaApplyClicked(cardName: String) {
        closeModal()
        showToast("Redirecting to $cardName KYC portal (DSA tracked).")
    }

    fun onLoanEligibilityClicked(loanName: String) {
        showToast("Checking eligibility for $loanName...")
    }

    class Factory(private val repository: NxtPayRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NxtPayViewModel(repository) as T
        }
    }
}
