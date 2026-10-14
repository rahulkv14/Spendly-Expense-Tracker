package com.example.data.repository

import com.example.data.dao.AppSettingsDao
import com.example.data.dao.ExpenseDao
import com.example.data.database.SampleData
import com.example.data.model.AppSettingsEntity
import com.example.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val appSettingsDao: AppSettingsDao
) {
    val allExpenses: Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()

    val settings: Flow<AppSettingsEntity> = appSettingsDao.getSettingsFlow().map {
        it ?: SampleData.defaultSettings
    }

    suspend fun checkAndSeedInitialData() {
        val count = expenseDao.getExpenseCount()
        if (count == 0) {
            expenseDao.insertExpenses(SampleData.getSampleExpenses())
        }
        val currentSettings = appSettingsDao.getSettings()
        if (currentSettings == null) {
            appSettingsDao.insertOrUpdate(SampleData.defaultSettings)
        }
    }

    suspend fun insertExpense(expense: ExpenseEntity): Long {
        return expenseDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpenseById(id: Long) {
        expenseDao.deleteExpenseById(id)
    }

    suspend fun deleteAllExpenses() {
        expenseDao.deleteAllExpenses()
    }

    suspend fun resetSampleData() {
        expenseDao.deleteAllExpenses()
        expenseDao.insertExpenses(SampleData.getSampleExpenses())
        appSettingsDao.insertOrUpdate(SampleData.defaultSettings)
    }

    suspend fun updateMonthlyBudget(newBudget: Double) {
        val current = appSettingsDao.getSettings() ?: SampleData.defaultSettings
        appSettingsDao.insertOrUpdate(current.copy(monthlyBudget = newBudget))
    }

    suspend fun updateSettings(settings: AppSettingsEntity) {
        appSettingsDao.insertOrUpdate(settings)
    }

    suspend fun updateLastSyncTime() {
        val current = appSettingsDao.getSettings() ?: SampleData.defaultSettings
        appSettingsDao.insertOrUpdate(current.copy(lastSyncTime = System.currentTimeMillis()))
    }
}
