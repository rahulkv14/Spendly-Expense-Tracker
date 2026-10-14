package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val monthlyBudget: Double = 65000.0,
    val userName: String = "Priya Sharma",
    val userEmail: String = "priya.sharma@gmail.com",
    val thresholdPercent: Int = 80,
    val isThresholdAlertEnabled: Boolean = true,
    val lastSyncTime: Long = System.currentTimeMillis(),
    val totalNetBalance: Double = 148250.0,
    val currencySymbol: String = "₹",
    val financialYear: String = "FY 2024–25"
)
