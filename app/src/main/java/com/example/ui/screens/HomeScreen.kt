package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CreditCardEntity
import com.example.data.model.UpcomingBillEntity
import com.example.ui.NxtPayUiState
import com.example.ui.components.CreditCardCarousel
import com.example.ui.theme.NxtPayTheme
import com.example.ui.theme.SurfaceWhite
import com.example.ui.util.formatInr

@Composable
fun HomeScreen(
    uiState: NxtPayUiState,
    onCardIndexChanged: (Int) -> Unit,
    onPayCardClick: (CreditCardEntity) -> Unit,
    onOpenCardMarketplace: () -> Unit,
    onBusinessPaymentClick: () -> Unit,
    onGstClick: () -> Unit,
    onUtilitiesClick: () -> Unit,
    onPayInvoiceClick: () -> Unit,
    onWalletClick: () -> Unit,
    onSellerHubClick: () -> Unit,
    onActivateWalletClick: () -> Unit,
    onReferEarnClick: () -> Unit,
    onViewAllHistoryClick: () -> Unit,
    onPayUpcomingClick: (UpcomingBillEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 6.dp,
            bottom = 124.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "card_carousel") {
            CreditCardCarousel(
                cards = uiState.cards,
                activeCardIndex = uiState.activeCardIndex,
                onCardIndexChanged = onCardIndexChanged,
                onPayBillClick = onPayCardClick
            )
        }

        item(key = "card_marketplace_banner") {
            CardMarketplaceBanner(onClick = onOpenCardMarketplace)
        }

        item(key = "payment_utility_grid") {
            PaymentAndUtilityGrid(
                onBusinessPaymentClick = onBusinessPaymentClick,
                onGstClick = onGstClick,
                onUtilitiesClick = onUtilitiesClick,
                onPayInvoiceClick = onPayInvoiceClick,
                onWalletClick = onWalletClick,
                onSellerHubClick = onSellerHubClick
            )
        }

        item(key = "activate_wallet_cta") {
            ActivateWalletCta(
                walletActivated = uiState.walletActivated,
                onClick = onActivateWalletClick
            )
        }

        item(key = "growth_cards_row") {
            GrowthCardsRow(
                onReferEarnClick = onReferEarnClick,
                onApplyCardClick = onOpenCardMarketplace
            )
        }

        item(key = "upcoming_payments") {
            UpcomingPaymentsCard(
                upcomingBills = uiState.upcomingBills,
                onViewAllClick = onViewAllHistoryClick,
                onPayUpcomingClick = onPayUpcomingClick
            )
        }
    }
}

@Composable
private fun CardMarketplaceBanner(onClick: () -> Unit) {
    val ext = NxtPayTheme.extended

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("banner_card_marketplace"),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, ext.cardBorder),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(ext.amberBg)
                        .border(1.dp, ext.amberBorder, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CreditCard,
                        contentDescription = null,
                        tint = ext.amberIcon,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = stringResource(R.string.banner_get_card_title),
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.banner_get_card_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = ext.mutedText
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = ext.mutedText,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun PaymentAndUtilityGrid(
    onBusinessPaymentClick: () -> Unit,
    onGstClick: () -> Unit,
    onUtilitiesClick: () -> Unit,
    onPayInvoiceClick: () -> Unit,
    onWalletClick: () -> Unit,
    onSellerHubClick: () -> Unit
) {
    val ext = NxtPayTheme.extended

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, ext.cardBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                UtilityGridTile(
                    icon = Icons.Rounded.AccountBalance,
                    label = stringResource(R.string.action_business_payment),
                    testTag = "grid_action_business",
                    onClick = onBusinessPaymentClick,
                    modifier = Modifier.weight(1f)
                )
                UtilityGridTile(
                    icon = Icons.Rounded.Receipt,
                    label = stringResource(R.string.action_gst),
                    badgeText = stringResource(R.string.badge_soon_coming),
                    badgeBg = ext.amberBg,
                    badgeTextColor = ext.amberText,
                    badgeBorder = ext.amberBorder,
                    testTag = "grid_action_gst",
                    onClick = onGstClick,
                    modifier = Modifier.weight(1f)
                )
                UtilityGridTile(
                    icon = Icons.Rounded.Bolt,
                    label = stringResource(R.string.action_utilities),
                    testTag = "grid_action_utilities",
                    onClick = onUtilitiesClick,
                    modifier = Modifier.weight(1f)
                )
                UtilityGridTile(
                    icon = Icons.Rounded.Description,
                    label = stringResource(R.string.action_pay_invoice),
                    testTag = "grid_action_invoice",
                    onClick = onPayInvoiceClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                UtilityGridTile(
                    icon = Icons.Rounded.AccountBalanceWallet,
                    label = stringResource(R.string.action_wallet),
                    testTag = "grid_action_wallet",
                    onClick = onWalletClick,
                    modifier = Modifier.weight(1f)
                )
                UtilityGridTile(
                    icon = Icons.Rounded.Storefront,
                    label = stringResource(R.string.action_seller_hub),
                    badgeText = stringResource(R.string.badge_new),
                    badgeBg = ext.accentIndigo,
                    badgeTextColor = SurfaceWhite,
                    badgeBorder = null,
                    testTag = "grid_action_seller",
                    onClick = onSellerHubClick,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun UtilityGridTile(
    icon: ImageVector,
    label: String,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    badgeBg: Color = Color.Transparent,
    badgeTextColor: Color = Color.Transparent,
    badgeBorder: Color? = null
) {
    val ext = NxtPayTheme.extended

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
            .testTag(testTag),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ext.tileSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label.replace("\n", " "),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    lineHeight = 14.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

        if (badgeText != null) {
            Surface(
                modifier = Modifier.offset(y = (-8).dp),
                shape = CircleShape,
                color = badgeBg,
                border = badgeBorder?.let { BorderStroke(1.dp, it) }
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = badgeTextColor,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun ActivateWalletCta(
    walletActivated: Boolean,
    onClick: () -> Unit
) {
    val ext = NxtPayTheme.extended

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(ext.emeraldBgStart, ext.emeraldBgEnd)
                )
            )
            .border(1.dp, ext.emeraldBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 13.dp)
            .testTag("activate_wallet_cta"),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (walletActivated) Icons.Rounded.CheckCircle else Icons.Rounded.Shield,
                contentDescription = null,
                tint = ext.emeraldIcon,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = if (walletActivated) {
                    stringResource(R.string.cta_wallet_active)
                } else {
                    stringResource(R.string.cta_activate_wallet)
                },
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = ext.emeraldText
            )
        }
    }
}

@Composable
private fun GrowthCardsRow(
    onReferEarnClick: () -> Unit,
    onApplyCardClick: () -> Unit
) {
    val ext = NxtPayTheme.extended

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GrowthPromoCard(
            title = stringResource(R.string.card_refer_title),
            subtitle = stringResource(R.string.card_refer_subtitle),
            cta = stringResource(R.string.card_refer_cta),
            bgStart = ext.amberBg,
            bgEnd = ext.amberBgEnd,
            borderColor = ext.amberBorder,
            ctaColor = ext.amberText,
            testTag = "growth_card_refer",
            onClick = onReferEarnClick,
            modifier = Modifier.weight(1f)
        )

        GrowthPromoCard(
            title = stringResource(R.string.card_apply_title),
            subtitle = stringResource(R.string.card_apply_subtitle),
            cta = stringResource(R.string.card_apply_cta),
            bgStart = ext.roseBgStart,
            bgEnd = ext.roseBgEnd,
            borderColor = ext.roseBorder,
            ctaColor = ext.roseText,
            testTag = "growth_card_apply",
            onClick = onApplyCardClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun GrowthPromoCard(
    title: String,
    subtitle: String,
    cta: String,
    bgStart: Color,
    bgEnd: Color,
    borderColor: Color,
    ctaColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = NxtPayTheme.extended

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(colors = listOf(bgStart, bgEnd)))
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag(testTag)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = ext.mutedText
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = cta,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp
                ),
                color = ctaColor
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = ctaColor,
                modifier = Modifier.size(13.dp)
            )
        }
    }
}

@Composable
private fun UpcomingPaymentsCard(
    upcomingBills: List<UpcomingBillEntity>,
    onViewAllClick: () -> Unit,
    onPayUpcomingClick: (UpcomingBillEntity) -> Unit
) {
    val ext = NxtPayTheme.extended

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, ext.cardBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.section_upcoming_payments),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.action_view_all),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                    color = ext.accentIndigo,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onViewAllClick)
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("upcoming_view_all_button")
                )
            }

            if (upcomingBills.isEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ext.emeraldBgStart)
                        .border(1.dp, ext.emeraldBorder, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = ext.emeraldIcon,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.upcoming_all_paid),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = ext.emeraldText
                    )
                }
            } else {
                upcomingBills.forEach { bill ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(ext.subtleSurface)
                            .border(1.dp, ext.cardBorder, RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = bill.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = bill.subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = ext.mutedText
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onPayUpcomingClick(bill) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ext.brandIndigo,
                                contentColor = SurfaceWhite
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .testTag("pay_upcoming_${bill.id}")
                        ) {
                            Text(
                                text = formatInr(bill.amount),
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}
