package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.HeadsetMic
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Percent
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.NxtPayTab
import com.example.ui.theme.NxtPayTheme
import com.example.ui.theme.SurfaceWhite
import com.example.ui.util.formatInr

@Composable
fun NxtPayTopBar(
    cashBalance: Long,
    onSecurityClick: () -> Unit,
    onBrandClick: () -> Unit,
    onWalletClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = NxtPayTheme.extended

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            onClick = onSecurityClick,
            modifier = Modifier
                .size(48.dp)
                .testTag("security_drawer_button"),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, ext.cardBorder),
            shadowElevation = 1.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.VerifiedUser,
                    contentDescription = stringResource(R.string.cd_security_terms),
                    tint = ext.accentIndigo,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Text(
            text = stringResource(R.string.top_bar_brand),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.4.sp
            ),
            color = ext.mutedText,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable(onClick = onBrandClick)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("top_bar_brand_title")
        )

        Surface(
            onClick = onWalletClick,
            modifier = Modifier
                .height(48.dp)
                .testTag("top_wallet_cash_pill"),
            shape = CircleShape,
            color = ext.amberBg,
            border = BorderStroke(1.dp, ext.amberBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.MonetizationOn,
                    contentDescription = stringResource(R.string.cd_nxt_cash_wallet),
                    tint = ext.amberIcon,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = formatInr(cashBalance),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = ext.amberText
                )
            }
        }
    }
}

@Composable
fun FloatingBottomNavigationBar(
    activeTab: NxtPayTab,
    onSelectTab: (NxtPayTab) -> Unit,
    onPayNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = NxtPayTheme.extended

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 430.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Pill navigation surface
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(32.dp),
                color = ext.navBarSurface,
                border = BorderStroke(1.dp, ext.cardBorder),
                shadowElevation = 10.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItem(
                        label = stringResource(R.string.nav_loan),
                        selectedIcon = Icons.Rounded.Percent,
                        unselectedIcon = Icons.Outlined.Percent,
                        selected = activeTab == NxtPayTab.LOAN,
                        testTag = "nav_tab_loan",
                        onClick = { onSelectTab(NxtPayTab.LOAN) },
                        modifier = Modifier.weight(1f)
                    )
                    BottomNavItem(
                        label = stringResource(R.string.nav_cards),
                        selectedIcon = Icons.Rounded.CreditCard,
                        unselectedIcon = Icons.Outlined.CreditCard,
                        selected = activeTab == NxtPayTab.CARDS,
                        testTag = "nav_tab_cards",
                        onClick = { onSelectTab(NxtPayTab.CARDS) },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(68.dp))

                    BottomNavItem(
                        label = stringResource(R.string.nav_history),
                        selectedIcon = Icons.Rounded.ReceiptLong,
                        unselectedIcon = Icons.Outlined.ReceiptLong,
                        selected = activeTab == NxtPayTab.HISTORY,
                        testTag = "nav_tab_history",
                        onClick = { onSelectTab(NxtPayTab.HISTORY) },
                        modifier = Modifier.weight(1f)
                    )
                    BottomNavItem(
                        label = stringResource(R.string.nav_support),
                        selectedIcon = Icons.Rounded.HeadsetMic,
                        unselectedIcon = Icons.Outlined.HeadsetMic,
                        selected = activeTab == NxtPayTab.SUPPORT,
                        testTag = "nav_tab_support",
                        onClick = { onSelectTab(NxtPayTab.SUPPORT) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Elevated central Pay Now button matching HTML's -mt-6 circular #13197D CTA
            Surface(
                onClick = onPayNowClick,
                modifier = Modifier
                    .offset(y = (-18).dp)
                    .size(62.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        ambientColor = ext.brandIndigo,
                        spotColor = ext.brandIndigo
                    )
                    .testTag("floating_pay_now_button"),
                shape = CircleShape,
                color = ext.brandIndigo,
                border = BorderStroke(2.5.dp, MaterialTheme.colorScheme.surface)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.nav_pay_now),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = SurfaceWhite,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    selected: Boolean,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = NxtPayTheme.extended
    val tint = if (selected) ext.accentIndigo else ext.mutedText

    Column(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (selected) selectedIcon else unselectedIcon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 10.sp
            ),
            color = tint,
            maxLines = 1
        )
    }
}

@Composable
fun FloatingToastBanner(
    message: String?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = message != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
            .statusBarsPadding()
            .padding(top = 12.dp, start = 16.dp, end = 16.dp)
    ) {
        if (message != null) {
            Box(
                modifier = Modifier
                    .widthIn(max = 380.dp)
                    .shadow(10.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.onBackground)
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                    .clickable(onClick = onDismiss)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("toast_banner")
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.background,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
