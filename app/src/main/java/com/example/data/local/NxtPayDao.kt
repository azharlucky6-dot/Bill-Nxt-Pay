package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.CreditCardEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UpcomingBillEntity
import com.example.data.model.WalletProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NxtPayDao {
    // Credit Cards
    @Query("SELECT * FROM credit_cards ORDER BY sortOrder ASC")
    fun observeCards(): Flow<List<CreditCardEntity>>

    @Query("SELECT COUNT(*) FROM credit_cards")
    suspend fun getCardCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CreditCardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CreditCardEntity>)

    @Update
    suspend fun updateCard(card: CreditCardEntity)

    @Query("SELECT * FROM credit_cards WHERE id = :cardId LIMIT 1")
    suspend fun getCardById(cardId: String): CreditCardEntity?

    // Upcoming Bills
    @Query("SELECT * FROM upcoming_bills")
    fun observeUpcomingBills(): Flow<List<UpcomingBillEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpcomingBills(bills: List<UpcomingBillEntity>)

    @Query("DELETE FROM upcoming_bills WHERE id = :billId")
    suspend fun deleteUpcomingBill(billId: String)

    // Transactions
    @Query("SELECT * FROM transactions ORDER BY createdAtMillis DESC")
    fun observeTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(tx: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(txs: List<TransactionEntity>)

    // Wallet Profile
    @Query("SELECT * FROM wallet_profile WHERE id = 1 LIMIT 1")
    fun observeWalletProfile(): Flow<WalletProfileEntity?>

    @Query("SELECT * FROM wallet_profile WHERE id = 1 LIMIT 1")
    suspend fun getWalletProfile(): WalletProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWalletProfile(profile: WalletProfileEntity)

    @Transaction
    suspend fun completePaymentTransaction(
        tx: TransactionEntity,
        cashbackEarned: Long,
        cardIdToSettle: String?,
        upcomingBillIdToRemove: String?
    ) {
        insertTransaction(tx)

        val currentProfile = getWalletProfile() ?: WalletProfileEntity()
        upsertWalletProfile(
            currentProfile.copy(cashBalance = currentProfile.cashBalance + cashbackEarned)
        )

        if (cardIdToSettle != null) {
            val card = getCardById(cardIdToSettle)
            if (card != null && card.dueAmount > 0L) {
                updateCard(
                    card.copy(
                        availableLimit = card.availableLimit + card.dueAmount,
                        dueAmount = 0L,
                        status = "Paid",
                        updatedDate = "Today"
                    )
                )
            }
        }

        if (upcomingBillIdToRemove != null) {
            deleteUpcomingBill(upcomingBillIdToRemove)
        }
    }
}
