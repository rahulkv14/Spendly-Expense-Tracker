package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val merchant: String,
    val note: String = "",
    val amount: Double,
    val category: String, // "Food", "Groceries", "Travel", "Shopping", "Movies", "Utilities", "Health", "More"
    val paymentMode: String, // "UPI (GPay / PhonePe)", "HDFC Card ••4091", "Debit Card", "Cash", "Net Banking"
    val timestamp: Long = System.currentTimeMillis(),
    val isCredit: Boolean = false, // true for salary / income
    val isRecurring: Boolean = false,
    val recurringFrequency: String = "Monthly", // "Monthly", "Weekly"
    val tags: String = ""
)
