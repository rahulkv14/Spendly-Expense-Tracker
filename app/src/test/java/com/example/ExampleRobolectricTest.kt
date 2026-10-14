package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.database.AppDatabase
import com.example.data.database.SampleData
import com.example.data.model.ExpenseEntity
import com.example.util.CurrencyUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun readAppNameFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Spendly", appName)
    }

    @Test
    fun testCurrencyFormatting() {
        assertEquals("₹1,48,250", CurrencyUtils.formatInr(148250.0))
        assertEquals("₹640.00", CurrencyUtils.formatInr(640.0, true))
        assertEquals("₹4.8k", CurrencyUtils.formatCompactInr(4800.0))
    }

    @Test
    fun testSampleDataContainsRealisticIndianTransactions() {
        val samples = SampleData.getSampleExpenses()
        assertTrue(samples.isNotEmpty())
        assertTrue(samples.any { it.merchant.contains("Swiggy") })
        assertTrue(samples.any { it.merchant.contains("Blinkit") })
        assertTrue(samples.any { it.paymentMode.contains("UPI") })
    }

    @Test
    fun testExpenseInsertionAndDeletion() = runBlocking {
        val dao = db.expenseDao()
        val expense = ExpenseEntity(
            merchant = "Blue Tokai Coffee Roasters",
            note = "Flat white & croissant",
            amount = 450.0,
            category = "Food",
            paymentMode = "UPI (GPay / PhonePe)"
        )
        val id = dao.insertExpense(expense)
        val fetched = dao.getExpenseById(id)
        assertNotNull(fetched)
        assertEquals("Blue Tokai Coffee Roasters", fetched?.merchant)
        assertEquals(450.0, fetched?.amount ?: 0.0, 0.001)

        val all = dao.getAllExpenses().first()
        assertEquals(1, all.size)

        dao.deleteExpenseById(id)
        val afterDelete = dao.getAllExpenses().first()
        assertTrue(afterDelete.isEmpty())
    }

    @Test
    fun testPaymentMethodAndNotePersistence() = runBlocking {
        val dao = db.expenseDao()
        val expense = ExpenseEntity(
            merchant = "Starbucks Indiranagar",
            note = "Test transaction",
            amount = 350.0,
            category = "Food",
            paymentMode = "UPI"
        )
        val id = dao.insertExpense(expense)
        val fetched = dao.getExpenseById(id)
        assertNotNull(fetched)
        assertEquals("UPI", fetched?.paymentMode)
        assertEquals("Test transaction", fetched?.note)

        // Test updating
        val updated = fetched!!.copy(
            paymentMode = "Credit Card",
            note = "Updated test transaction memo"
        )
        dao.updateExpense(updated)
        val reFetched = dao.getExpenseById(id)
        assertEquals("Credit Card", reFetched?.paymentMode)
        assertEquals("Updated test transaction memo", reFetched?.note)
    }
}
