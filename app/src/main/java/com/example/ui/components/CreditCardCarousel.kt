package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CardMetallicStyle
import com.example.data.model.CreditCardEntity
import com.example.ui.theme.Amber200
import com.example.ui.theme.Amber500
import com.example.ui.theme.BorderSlate300
import com.example.ui.theme.CardEmeraldEnd
import com.example.ui.theme.CardEmeraldMid
import com.example.ui.theme.CardEmeraldStart
import com.example.ui.theme.CardNavyEnd
import com.example.ui.theme.CardNavyMid
import com.example.ui.theme.CardNavyStart
import com.example.ui.theme.CardObsidianEnd
import com.example.ui.theme.CardObsidianMid
import com.example.ui.theme.CardObsidianStart
import com.example.ui.theme.ChipGoldDark
import com.example.ui.theme.ChipGoldLight
import com.example.ui.theme.Emerald300
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.NxtPayTheme
import com.example.ui.theme.Rose300
import com.example.ui.theme.Rose400
import com.example.ui.theme.Rose500
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextSlate300
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate900
import com.example.ui.util.formatInr
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun CreditCardCarousel(
    cards: List<CreditCardEntity>,
    activeCardIndex: Int,
    onCardIndexChanged: (Int) -> Unit,
    onPayBillClick: (CreditCardEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (cards.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = activeCardIndex.coerceIn(0, cards.lastIndex),
        pageCount = { cards.size }
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(activeCardIndex, cards.size) {
        val target = activeCardIndex.coerceIn(0, cards.lastIndex)
        if (pagerState.currentPage != target) {
            pagerState.animateScrollToPage(target)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collect { page ->
                if (page != activeCardIndex) {
                    onCardIndexChanged(page)
                }
            }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("credit_card_pager")
        ) { page ->
            MetallicCreditCard(
                card = cards[page],
                onPayBillClick = { onPayBillClick(cards[page]) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag("credit_card_dots")
        ) {
            val ext = NxtPayTheme.extended
            cards.forEachIndexed { idx, _ ->
                val isSelected = idx == pagerState.currentPage
                val dotWidth by animateDpAsState(
                    targetValue = if (isSelected) 22.dp else 7.dp,
                    label = "card_dot_width"
                )
                Box(
                    modifier = Modifier
                        .height(7.dp)
                        .width(dotWidth)
                        .clip(CircleShape)
                        .background(if (isSelected) ext.brandIndigo else BorderSlate300)
                        .clickable {
                            scope.launch { pagerState.animateScrollToPage(idx) }
                            onCardIndexChanged(idx)
                        }
                        .testTag("card_dot_$idx")
                )
            }
        }
    }
}

@Composable
private fun MetallicCreditCard(
    card: CreditCardEntity,
    onPayBillClick: () -> Unit
) {
    val gradientColors = when (card.cardStyle) {
        CardMetallicStyle.NAVY.name -> listOf(CardNavyStart, CardNavyMid, CardNavyEnd)
        CardMetallicStyle.EMERALD.name -> listOf(CardEmeraldStart, CardEmeraldMid, CardEmeraldEnd)
        else -> listOf(CardObsidianStart, CardObsidianMid, CardObsidianEnd)
    }

    val (badgeBg, badgeText, badgeBorder) = when {
        card.status.equals("Overdue", ignoreCase = true) -> Triple(
            Rose500.copy(alpha = 0.22f),
            Rose300,
            Rose400.copy(alpha = 0.45f)
        )
        card.status.equals("Paid", ignoreCase = true) || card.dueAmount <= 0L -> Triple(
            Emerald500.copy(alpha = 0.22f),
            Emerald300,
            Emerald400.copy(alpha = 0.45f)
        )
        else -> Triple(
            Amber500.copy(alpha = 0.22f),
            Amber200,
            Amber500.copy(alpha = 0.45f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = gradientColors.first(),
                spotColor = gradientColors.first()
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = gradientColors
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(20.dp)
            )
            .testTag("credit_card_${card.id}")
    ) {
        // Subtle diagonal metallic sheen overlay
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.09f), Color.Transparent),
                    center = Offset(size.width * 0.85f, size.height * 0.12f),
                    radius = size.width * 0.6f
                ),
                center = Offset(size.width * 0.85f, size.height * 0.12f),
                radius = size.width * 0.6f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Top Row: Bank + masked digits + status pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = card.bank,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        ),
                        color = SurfaceWhite
                    )
                    Text(
                        text = "•••• ${card.last4}  |  ${card.label}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 11.sp
                        ),
                        color = TextSlate300
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = badgeBg,
                    border = BorderStroke(1.dp, badgeBorder)
                ) {
                    Text(
                        text = card.status,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        ),
                        color = badgeText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Middle Row: EMV Chip + Network & Amount Due
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    EmvMetallicChip()
                    Text(
                        text = card.network,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp
                        ),
                        color = TextSlate300
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.label_amount_due),
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSlate300
                    )
                    Text(
                        text = formatInr(card.dueAmount),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 21.sp
                        ),
                        color = SurfaceWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.12f))
            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Row: Available Limit + Last Updated & Pay Bill CTA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.label_available_limit),
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSlate400
                    )
                    Text(
                        text = formatInr(card.availableLimit),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SurfaceWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.label_last_updated, card.updatedDate),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp
                        ),
                        color = TextSlate400
                    )
                }

                Button(
                    onClick = onPayBillClick,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceWhite,
                        contentColor = TextSlate900
                    ),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("pay_card_button_${card.id}")
                ) {
                    Text(
                        text = if (card.dueAmount > 0L) {
                            stringResource(R.string.action_pay_bill)
                        } else {
                            stringResource(R.string.status_paid_check)
                        },
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun EmvMetallicChip() {
    Box(
        modifier = Modifier
            .size(width = 32.dp, height = 22.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(ChipGoldLight, ChipGoldDark)
                )
            )
            .border(0.75.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(5.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val lineColor = Color.Black.copy(alpha = 0.22f)
            drawLine(
                color = lineColor,
                start = Offset(0f, size.height * 0.5f),
                end = Offset(size.width, size.height * 0.5f),
                strokeWidth = 1.5f
            )
            drawLine(
                color = lineColor,
                start = Offset(size.width * 0.35f, 0f),
                end = Offset(size.width * 0.35f, size.height),
                strokeWidth = 1.5f
            )
            drawLine(
                color = lineColor,
                start = Offset(size.width * 0.65f, 0f),
                end = Offset(size.width * 0.65f, size.height),
                strokeWidth = 1.5f
            )
        }
    }
}
