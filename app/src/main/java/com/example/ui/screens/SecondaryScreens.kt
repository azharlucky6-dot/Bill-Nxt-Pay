package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CreditCardEntity
import com.example.data.model.TransactionEntity
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.NxtPayTheme
import com.example.ui.theme.SurfaceWhite
import com.example.ui.util.formatInr

@Composable
fun LoanScreen(
    onBackToHome: () -> Unit,
    onCheckEligibility: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToHome)
    val ext = NxtPayTheme.extended

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("loan_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 124.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
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
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AccountBalance,
                            contentDescription = null,
                            tint = ext.accentIndigo,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = stringResource(R.string.loan_screen_title),
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = stringResource(R.string.loan_screen_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = ext.mutedText
                    )

                    LoanOfferItem(
                        title = stringResource(R.string.loan_personal_title),
                        subtitle = stringResource(R.string.loan_personal_sub),
                        testTag = "loan_check_personal",
                        onCheck = { onCheckEligibility("Personal Flexi Loan") }
                    )

                    LoanOfferItem(
                        title = stringResource(R.string.loan_merchant_title),
                        subtitle = stringResource(R.string.loan_merchant_sub),
                        testTag = "loan_check_merchant",
                        onCheck = { onCheckEligibility("Merchant Working Capital") }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoanOfferItem(
    title: String,
    subtitle: String,
    testTag: String,
    onCheck: () -> Unit
) {
    val ext = NxtPayTheme.extended

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ext.subtleSurface)
            .border(1.dp, ext.cardBorder, RoundedCornerShape(14.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = ext.mutedText
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            onClick = onCheck,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ext.brandIndigo,
                contentColor = SurfaceWhite
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            modifier = Modifier
                .height(38.dp)
                .testTag(testTag)
        ) {
            Text(
                text = stringResource(R.string.action_check_eligibility),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp)
            )
        }
    }
}

@Composable
fun CardsManageScreen(
    cards: List<CreditCardEntity>,
    onBackToHome: () -> Unit,
    onLinkCardClick: () -> Unit,
    onSelectCard: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToHome)
    val ext = NxtPayTheme.extended

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("cards_manage_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 124.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CreditCard,
                                contentDescription = null,
                                tint = ext.accentIndigo,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = stringResource(R.string.cards_screen_title),
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = stringResource(R.string.action_link_card),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = ext.accentIndigo,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = onLinkCardClick)
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .testTag("link_new_card_button")
                        )
                    }

                    cards.forEachIndexed { idx, card ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(ext.subtleSurface)
                                .border(1.dp, ext.cardBorder, RoundedCornerShape(14.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${card.bank} •••• ${card.last4}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${card.label} • Due: ${formatInr(card.dueAmount)}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                    color = ext.mutedText
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(
                                onClick = { onSelectCard(idx) },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, ext.cardBorder),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .height(36.dp)
                                    .testTag("view_card_button_$idx")
                            ) {
                                Text(
                                    text = stringResource(R.string.action_view_card),
                                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryScreen(
    transactions: List<TransactionEntity>,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToHome)
    val ext = NxtPayTheme.extended

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("history_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 124.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ReceiptLong,
                            contentDescription = null,
                            tint = ext.accentIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(R.string.history_screen_title),
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (transactions.isEmpty()) {
                        Text(
                            text = stringResource(R.string.history_empty),
                            style = MaterialTheme.typography.bodySmall,
                            color = ext.mutedText,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        transactions.forEach { tx ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(ext.subtleSurface)
                                    .border(1.dp, ext.cardBorder, RoundedCornerShape(14.dp))
                                    .padding(12.dp)
                                    .testTag("history_item_${tx.refId}"),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tx.title,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Ref: ${tx.refId} • ${tx.dateLabel}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 10.5.sp
                                        ),
                                        color = ext.mutedText
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = formatInr(tx.amount),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = tx.status,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ext.emeraldIcon
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SupportScreen(
    onBackToHome: () -> Unit,
    onReadTermsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToHome)
    val ext = NxtPayTheme.extended

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("support_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 124.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
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
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SupportAgent,
                            contentDescription = null,
                            tint = ext.accentIndigo,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = stringResource(R.string.support_screen_title),
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = stringResource(R.string.support_reversal_info),
                        style = MaterialTheme.typography.bodySmall,
                        color = ext.mutedText
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(ext.subtleSurface)
                            .border(1.dp, ext.cardBorder, RoundedCornerShape(14.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.support_grievance_title),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.support_grievance_email),
                            style = MaterialTheme.typography.bodySmall,
                            color = ext.mutedText
                        )
                        Text(
                            text = stringResource(R.string.support_grievance_sla),
                            style = MaterialTheme.typography.bodySmall,
                            color = ext.mutedText
                        )
                    }

                    Button(
                        onClick = onReadTermsClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("support_open_terms_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ext.brandIndigo,
                            contentColor = SurfaceWhite
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.support_read_terms),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}
