package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CardMetallicStyle {
    OBSIDIAN,
    NAVY,
    EMERALD
}

@Entity(tableName = "credit_cards")
data class CreditCardEntity(
    @PrimaryKey val id: String,
    val bank: String,
    val last4: String,
    val label: String,
    val network: String,
    val status: String,
    val dueAmount: Long,
    val availableLimit: Long,
    val updatedDate: String,
    val cardStyle: String,
    val sortOrder: Int
)

@Entity(tableName = "upcoming_bills")
data class UpcomingBillEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val amount: Long,
    val category: String
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val refId: String,
    val title: String,
    val amount: Long,
    val dateLabel: String,
    val status: String,
    val createdAtMillis: Long
)

@Entity(tableName = "wallet_profile")
data class WalletProfileEntity(
    @PrimaryKey val id: Int = 1,
    val walletActivated: Boolean = false,
    val cashBalance: Long = 500L
)
