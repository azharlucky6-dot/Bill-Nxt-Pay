package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.ActiveModal
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.NxtPayTheme
import com.example.ui.theme.SurfaceWhite
import com.example.ui.util.formatInr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NxtPayModalHost(
    activeModal: ActiveModal,
    cashBalance: Long,
    demoOtp: String,
    onDismiss: () -> Unit,
    onAuthorizePayment: (ActiveModal.PaymentGate, String) -> Unit,
    onProceedUtility: (String, String, String) -> Unit,
    onProceedBusiness: (String, String) -> Unit,
    onDsaApply: (String) -> Unit,
    onSaveNewCard: (String, String, String, String, String) -> Unit,
    onCopiedToast: (String) -> Unit
) {
    if (activeModal is ActiveModal.None) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val title = when (activeModal) {
        is ActiveModal.PaymentGate -> stringResource(R.string.modal_payment_title)
        ActiveModal.Utilities -> stringResource(R.string.modal_utilities_title)
        ActiveModal.SecurityTerms -> stringResource(R.string.modal_security_title)
        ActiveModal.WalletRewards -> stringResource(R.string.modal_wallet_title)
        ActiveModal.CardMarketplace -> stringResource(R.string.modal_marketplace_title)
        ActiveModal.ReferEarn -> stringResource(R.string.modal_refer_title)
        ActiveModal.LinkCard -> stringResource(R.string.modal_link_card_title)
        is ActiveModal.BusinessPayment -> if (activeModal.isInvoice) {
            stringResource(R.string.modal_invoice_title)
        } else {
            stringResource(R.string.modal_business_title)
        }
        ActiveModal.SellerHub -> stringResource(R.string.modal_seller_title)
        ActiveModal.None -> ""
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("modal_close_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(NxtPayTheme.extended.tileSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.cd_close_modal),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            HorizontalDivider(color = NxtPayTheme.extended.cardBorder)
            Spacer(modifier = Modifier.height(14.dp))

            when (activeModal) {
                is ActiveModal.PaymentGate -> PaymentGateContent(
                    gate = activeModal,
                    demoOtp = demoOtp,
                    onAuthorize = { otp -> onAuthorizePayment(activeModal, otp) }
                )
                ActiveModal.Utilities -> UtilitiesModalContent(onProceed = onProceedUtility)
                ActiveModal.SecurityTerms -> SecurityTermsModalContent()
                ActiveModal.WalletRewards -> WalletRewardsModalContent(cashBalance = cashBalance)
                ActiveModal.CardMarketplace -> CardMarketplaceModalContent(onApply = onDsaApply)
                ActiveModal.ReferEarn -> ReferEarnModalContent(onCopiedToast = onCopiedToast)
                ActiveModal.LinkCard -> LinkCardModalContent(onSave = onSaveNewCard)
                is ActiveModal.BusinessPayment -> BusinessPaymentModalContent(onProceed = onProceedBusiness)
                ActiveModal.SellerHub -> SellerHubModalContent()
                ActiveModal.None -> Unit
            }
        }
    }
}

@Composable
private fun PaymentGateContent(
    gate: ActiveModal.PaymentGate,
    demoOtp: String,
    onAuthorize: (String) -> Unit
) {
    val ext = NxtPayTheme.extended
    var otpValue by remember(gate) { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(ext.subtleSurface)
                .border(1.dp, ext.cardBorder, RoundedCornerShape(14.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Paying to",
                style = MaterialTheme.typography.bodySmall,
                color = ext.mutedText
            )
            Text(
                text = gate.billerName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = formatInr(gate.amount),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = ext.accentIndigo
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ext.emeraldBgStart)
                .border(1.dp, ext.emeraldBorder, RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = null,
                tint = ext.emeraldIcon,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "TLS 1.3 + BBPS Idempotent Tokenization Active. Demo OTP: $demoOtp",
                style = MaterialTheme.typography.bodySmall,
                color = ext.emeraldText,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Enter 6-digit OTP to authorize",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(
                onClick = { otpValue = demoOtp },
                modifier = Modifier.testTag("autofill_demo_otp_button")
            ) {
                Text(
                    text = "Fill $demoOtp",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    color = ext.accentIndigo
                )
            }
        }

        OutlinedTextField(
            value = otpValue,
            onValueChange = { if (it.length <= 6 && it.all(Char::isDigit)) otpValue = it },
            placeholder = { Text(demoOtp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            textStyle = MaterialTheme.typography.titleMedium.copy(
                fontFamily = JetBrainsMonoFontFamily,
                letterSpacing = 3.sp
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("payment_otp_input")
        )

        Button(
            onClick = { onAuthorize(otpValue) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("confirm_payment_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ext.brandIndigo,
                contentColor = SurfaceWhite
            )
        ) {
            Text(
                text = "Authorize & Pay ${formatInr(gate.amount)}",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun UtilitiesModalContent(
    onProceed: (String, String, String) -> Unit
) {
    val ext = NxtPayTheme.extended
    val categories = listOf(
        "Electricity" to "Electricity",
        "Postpaid" to "Postpaid",
        "Prepaid" to "Prepaid 5G",
        "FASTag" to "FASTag"
    )
    var selectedCategory by remember { mutableStateOf("Electricity") }
    var consumerNumber by remember { mutableStateOf("1094827102") }
    var amountText by remember { mutableStateOf("1250") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { (key, label) ->
                        val selected = selectedCategory == key
                        Surface(
                            onClick = { selectedCategory = key },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("util_category_$key"),
                            shape = RoundedCornerShape(12.dp),
                            color = if (selected) ext.accentIndigoBg else ext.subtleSurface,
                            border = BorderStroke(
                                1.dp,
                                if (selected) ext.accentIndigo else ext.cardBorder
                            )
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = if (selected) ext.accentIndigo else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        OutlinedTextField(
            value = consumerNumber,
            onValueChange = { consumerNumber = it },
            label = { Text("Consumer / Mobile / Vehicle Number") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("util_consumer_input")
        )

        OutlinedTextField(
            value = amountText,
            onValueChange = { if (it.all(Char::isDigit)) amountText = it },
            label = { Text("Amount (INR)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("util_amount_input")
        )

        Button(
            onClick = { onProceed(selectedCategory, consumerNumber, amountText) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("util_proceed_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ext.brandIndigo,
                contentColor = SurfaceWhite
            )
        ) {
            Text(
                text = "Fetch & Pay Bill",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun SecurityTermsModalContent() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SecurityClauseCard(
            title = "1. OWASP MASVS & WAF Controls",
            body = "TLS 1.3 + Public Key Pinning, Android Keystore token storage, root detection, and Cloudflare/AWS WAF rate limiting."
        )
        SecurityClauseCard(
            title = "2. RBI Data Localization",
            body = "All payment ledger records stored in India (Mumbai/Hyderabad). Zero raw PAN/CVV storage."
        )
        SecurityClauseCard(
            title = "3. Terms & Refund SLA",
            body = "Failed BBPS/UPI transactions auto-reverse in T+1 to T+5 working days. Convenience fees disclosed before OTP."
        )
    }
}

@Composable
private fun SecurityClauseCard(title: String, body: String) {
    val ext = NxtPayTheme.extended
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
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall,
            color = ext.mutedText
        )
    }
}

@Composable
private fun WalletRewardsModalContent(cashBalance: Long) {
    val ext = NxtPayTheme.extended
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ext.amberBg)
                .border(1.dp, ext.amberBorder, RoundedCornerShape(16.dp))
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Available Closed-Loop Cashback",
                style = MaterialTheme.typography.labelLarge,
                color = ext.amberText
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = formatInr(cashBalance),
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold),
                color = ext.amberText
            )
        }
        Text(
            text = "Redeemable directly on electricity, postpaid, prepaid, and FASTag bill payments inside NXT Pay.",
            style = MaterialTheme.typography.bodySmall,
            color = ext.mutedText
        )
    }
}

@Composable
private fun CardMarketplaceModalContent(onApply: (String) -> Unit) {
    val ext = NxtPayTheme.extended
    val offers = listOf(
        "Amazon Pay ICICI" to "Lifetime Free • 5% Prime Cashback",
        "HDFC Millennia" to "5% Cashback on Online Spends",
        "SBI Cashback Card" to "5% Unlimited Online Cashback"
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        offers.forEachIndexed { idx, (name, perks) ->
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
                        text = name,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = perks,
                        style = MaterialTheme.typography.bodySmall,
                        color = ext.mutedText
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onApply(name) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ext.brandIndigo,
                        contentColor = SurfaceWhite
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("dsa_apply_button_$idx")
                ) {
                    Text(
                        text = "Apply",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReferEarnModalContent(onCopiedToast: (String) -> Unit) {
    val ext = NxtPayTheme.extended
    val clipboardManager = LocalClipboardManager.current
    val referralCode = "NXTPAY500"

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Share your referral code with friends. You both earn ₹500 NXT Cash after their first BBPS bill payment.",
            style = MaterialTheme.typography.bodySmall,
            color = ext.mutedText
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(ext.tileSurface)
                .border(1.dp, ext.cardBorder, RoundedCornerShape(14.dp))
                .clickable {
                    clipboardManager.setText(AnnotatedString(referralCode))
                    onCopiedToast("Referral code $referralCode copied!")
                }
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .testTag("copy_referral_code_button"),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = referralCode,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = JetBrainsMonoFontFamily,
                    letterSpacing = 3.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = ext.accentIndigo,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Rounded.ContentCopy,
                contentDescription = "Copy referral code",
                tint = ext.accentIndigo,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun LinkCardModalContent(
    onSave: (String, String, String, String, String) -> Unit
) {
    val ext = NxtPayTheme.extended
    var bankName by remember { mutableStateOf("") }
    var last4 by remember { mutableStateOf("") }
    var dueAmount by remember { mutableStateOf("") }
    var cardLabel by remember { mutableStateOf("Rewards") }
    val networks = listOf("VISA", "Mastercard", "RuPay")
    var selectedNetwork by remember { mutableStateOf("VISA") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = bankName,
            onValueChange = { bankName = it },
            label = { Text("Bank Name (e.g. Axis Bank)") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("link_card_bank_input")
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = last4,
                onValueChange = { if (it.length <= 4 && it.all(Char::isDigit)) last4 = it },
                label = { Text("Last 4 digits") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("link_card_last4_input")
            )
            OutlinedTextField(
                value = dueAmount,
                onValueChange = { if (it.all(Char::isDigit)) dueAmount = it },
                label = { Text("Current Due (INR)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("link_card_due_input")
            )
        }
        OutlinedTextField(
            value = cardLabel,
            onValueChange = { cardLabel = it },
            label = { Text("Card Variant (e.g. Ace, Millennia)") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            networks.forEach { net ->
                val selected = selectedNetwork == net
                Surface(
                    onClick = { selectedNetwork = net },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = if (selected) ext.accentIndigoBg else ext.subtleSurface,
                    border = BorderStroke(1.dp, if (selected) ext.accentIndigo else ext.cardBorder)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = net,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (selected) ext.accentIndigo else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Button(
            onClick = { onSave(bankName, last4, dueAmount, cardLabel, selectedNetwork) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("save_linked_card_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ext.brandIndigo,
                contentColor = SurfaceWhite
            )
        ) {
            Text(
                text = "Save Card",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun BusinessPaymentModalContent(
    onProceed: (String, String) -> Unit
) {
    val ext = NxtPayTheme.extended
    var vendorName by remember { mutableStateOf("Sri Balaji Distributors") }
    var amountText by remember { mutableStateOf("4500") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = vendorName,
            onValueChange = { vendorName = it },
            label = { Text("Vendor / GSTIN Name") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("business_vendor_input")
        )
        OutlinedTextField(
            value = amountText,
            onValueChange = { if (it.all(Char::isDigit)) amountText = it },
            label = { Text("Amount (INR)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("business_amount_input")
        )
        Button(
            onClick = { onProceed(vendorName, amountText) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("business_pay_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ext.brandIndigo,
                contentColor = SurfaceWhite
            )
        ) {
            Text(
                text = "Pay via UPI / PA",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun SellerHubModalContent() {
    val ext = NxtPayTheme.extended
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(ext.subtleSurface)
                .border(1.dp, ext.cardBorder, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Text(
                text = "Prepaid Margin",
                style = MaterialTheme.typography.bodySmall,
                color = ext.mutedText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "0.7% – 4.0%",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(ext.subtleSurface)
                .border(1.dp, ext.cardBorder, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Text(
                text = "BBPS Utility Fee",
                style = MaterialTheme.typography.bodySmall,
                color = ext.mutedText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "₹2 – ₹12 / bill",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
