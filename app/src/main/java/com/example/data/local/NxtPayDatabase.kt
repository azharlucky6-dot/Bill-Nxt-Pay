package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CardMetallicStyle
import com.example.data.model.CreditCardEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UpcomingBillEntity
import com.example.data.model.WalletProfileEntity

@Database(
    entities = [
        CreditCardEntity::class,
        UpcomingBillEntity::class,
        TransactionEntity::class,
        WalletProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NxtPayDatabase : RoomDatabase() {
    abstract fun nxtPayDao(): NxtPayDao

    companion object {
        @Volatile
        private var INSTANCE: NxtPayDatabase? = null

        fun getInstance(context: Context): NxtPayDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NxtPayDatabase::class.java,
                    "nxt_pay.db"
                ).build().also { INSTANCE = it }
            }
        }

        fun defaultCards(): List<CreditCardEntity> = listOf(
            CreditCardEntity(
                id = "c1",
                bank = "ICICI BANK",
                last4 = "9000",
                label = "Amazon Pay",
                network = "VISA",
                status = "Overdue",
                dueAmount = 739L,
                availableLimit = 112629L,
                updatedDate = "03-07-2026",
                cardStyle = CardMetallicStyle.OBSIDIAN.name,
                sortOrder = 0
            ),
            CreditCardEntity(
                id = "c2",
                bank = "HDFC BANK",
                last4 = "4821",
                label = "Millennia",
                network = "Mastercard",
                status = "Due in 4d",
                dueAmount = 14280L,
                availableLimit = 235720L,
                updatedDate = "18-11-2026",
                cardStyle = CardMetallicStyle.NAVY.name,
                sortOrder = 1
            ),
            CreditCardEntity(
                id = "c3",
                bank = "SBI CARD",
                last4 = "1194",
                label = "SimplyCLICK",
                network = "RuPay",
                status = "Paid",
                dueAmount = 0L,
                availableLimit = 85000L,
                updatedDate = "19-11-2026",
                cardStyle = CardMetallicStyle.EMERALD.name,
                sortOrder = 2
            )
        )

        fun defaultUpcomingBills(): List<UpcomingBillEntity> = listOf(
            UpcomingBillEntity(
                id = "u1",
                title = "TSSPDCL Electricity",
                subtitle = "Consumer #10948271",
                amount = 1840L,
                category = "Electricity"
            ),
            UpcomingBillEntity(
                id = "u2",
                title = "Airtel Postpaid",
                subtitle = "+91 98480 11223",
                amount = 707L,
                category = "Postpaid"
            ),
            UpcomingBillEntity(
                id = "u3",
                title = "NHAI FASTag",
                subtitle = "TS09 EF 4421",
                amount = 500L,
                category = "FASTag"
            )
        )

        fun defaultTransactions(): List<TransactionEntity> = listOf(
            TransactionEntity(
                refId = "BBPS992817",
                title = "SBI Card ****1194",
                amount = 6420L,
                dateLabel = "19 Nov 2026",
                status = "Success",
                createdAtMillis = 1795046400000L
            ),
            TransactionEntity(
                refId = "BBPS992104",
                title = "Jio Prepaid 5G",
                amount = 299L,
                dateLabel = "14 Nov 2026",
                status = "Success",
                createdAtMillis = 1794614400000L
            )
        )
    }
}
