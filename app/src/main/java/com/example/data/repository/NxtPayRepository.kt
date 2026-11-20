package com.example.data.repository

import com.example.data.local.NxtPayDao
import com.example.data.local.NxtPayDatabase
import com.example.data.model.CardMetallicStyle
import com.example.data.model.CreditCardEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UpcomingBillEntity
import com.example.data.model.WalletProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class NxtPayRepository(private val dao: NxtPayDao) {
    val cards: Flow<List<CreditCardEntity>> = dao.observeCards()
    val upcomingBills: Flow<List<UpcomingBillEntity>> = dao.observeUpcomingBills()
    val transactions: Flow<List<TransactionEntity>> = dao.observeTransactions()
    val walletProfile: Flow<WalletProfileEntity> = dao.observeWalletProfile()
        .map { it ?: WalletProfileEntity() }

    suspend fun seedInitialDataIfNeeded() {
        if (dao.getWalletProfile() == null) {
            dao.upsertWalletProfile(WalletProfileEntity(id = 1, walletActivated = false, cashBalance = 500L))
            if (dao.getCardCount() == 0) {
                dao.insertCards(NxtPayDatabase.defaultCards())
                dao.insertUpcomingBills(NxtPayDatabase.defaultUpcomingBills())
                dao.insertTransactions(NxtPayDatabase.defaultTransactions())
            }
        }
    }

    suspend fun activateWallet(): Boolean {
        val current = dao.getWalletProfile() ?: WalletProfileEntity()
        if (current.walletActivated) return false
        dao.upsertWalletProfile(
            current.copy(
                walletActivated = true,
                cashBalance = current.cashBalance + 100L
            )
        )
        return true
    }

    suspend fun executePayment(
        billerName: String,
        amount: Long,
        cardIdToSettle: String? = null,
        upcomingBillIdToRemove: String? = null
    ): String {
        val refId = "BBPS${Random.nextInt(100000, 999999)}"
        val tx = TransactionEntity(
            refId = refId,
            title = billerName,
            amount = amount,
            dateLabel = "Today",
            status = "Success",
            createdAtMillis = System.currentTimeMillis()
        )
        dao.completePaymentTransaction(
            tx = tx,
            cashbackEarned = 25L,
            cardIdToSettle = cardIdToSettle,
            upcomingBillIdToRemove = upcomingBillIdToRemove
        )
        return refId
    }

    suspend fun linkNewCard(
        bankName: String,
        last4: String,
        dueAmount: Long,
        label: String = "Rewards",
        network: String = "VISA"
    ): Int {
        val count = dao.getCardCount()
        val style = when (count % 3) {
            0 -> CardMetallicStyle.OBSIDIAN.name
            1 -> CardMetallicStyle.NAVY.name
            else -> CardMetallicStyle.EMERALD.name
        }
        val cleanBank = bankName.trim().ifBlank { "AXIS BANK" }.uppercase()
        val cleanLast4 = last4.filter { it.isDigit() }.takeLast(4).padStart(4, '5')
        val validDue = dueAmount.coerceAtLeast(0L)
        val status = if (validDue > 0L) "Due soon" else "Paid"

        dao.insertCard(
            CreditCardEntity(
                id = "c_${System.currentTimeMillis()}",
                bank = cleanBank,
                last4 = cleanLast4,
                label = label.ifBlank { "Rewards" },
                network = network.ifBlank { "VISA" },
                status = status,
                dueAmount = validDue,
                availableLimit = 150000L,
                updatedDate = "Today",
                cardStyle = style,
                sortOrder = count
            )
        )
        return count
    }
}
