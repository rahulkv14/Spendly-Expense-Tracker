package com.example.data.database

import com.example.data.model.AppSettingsEntity
import com.example.data.model.ExpenseEntity
import java.util.Calendar

object SampleData {
    val defaultSettings = AppSettingsEntity(
        id = 1,
        monthlyBudget = 65000.0,
        userName = "Priya Sharma",
        userEmail = "priya.sharma@gmail.com",
        thresholdPercent = 80,
        isThresholdAlertEnabled = true,
        lastSyncTime = System.currentTimeMillis() - (3600 * 1000 * 3), // 3 hours ago
        totalNetBalance = 148250.0,
        currencySymbol = "₹",
        financialYear = "FY 2024–25"
    )

    fun getSampleExpenses(): List<ExpenseEntity> {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()

        // Helper to get millis for days ago and specific hour/minute
        fun timeFor(daysAgo: Int, hour: Int, minute: Int): Long {
            calendar.timeInMillis = now
            calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            return calendar.timeInMillis
        }

        return listOf(
            ExpenseEntity(
                merchant = "Swiggy - Dinner Order",
                note = "Truffles Burger & Fries",
                amount = 640.0,
                category = "Food",
                paymentMode = "UPI (GPay / PhonePe)",
                timestamp = timeFor(0, 20, 45),
                isCredit = false,
                isRecurring = false,
                tags = "#dinner"
            ),
            ExpenseEntity(
                merchant = "Blinkit - Daily Groceries",
                note = "Organic milk, veggies & curd",
                amount = 1180.0,
                category = "Groceries",
                paymentMode = "UPI (GPay / PhonePe)",
                timestamp = timeFor(0, 11, 20),
                isCredit = false,
                isRecurring = false,
                tags = "#groceries #essentials"
            ),
            ExpenseEntity(
                merchant = "Third Wave Coffee, Indiranagar",
                note = "Team catchup coffee & snacks #work",
                amount = 1450.0,
                category = "Food",
                paymentMode = "UPI (GPay / PhonePe)",
                timestamp = timeFor(0, 19, 30),
                isCredit = false,
                isRecurring = false,
                tags = "#work #coffee"
            ),
            ExpenseEntity(
                merchant = "Shell Fuel Station",
                note = "Car tank refuel",
                amount = 2200.0,
                category = "Travel",
                paymentMode = "HDFC Card ••4091",
                timestamp = timeFor(1, 17, 15),
                isCredit = false,
                isRecurring = false,
                tags = "#fuel"
            ),
            ExpenseEntity(
                merchant = "Apollo Pharmacy",
                note = "Vitamins & medicine",
                amount = 1550.0,
                category = "Health",
                paymentMode = "UPI (GPay / PhonePe)",
                timestamp = timeFor(1, 14, 10),
                isCredit = false,
                isRecurring = false,
                tags = "#health #tax-claimable"
            ),
            ExpenseEntity(
                merchant = "Zepto Quick Delivery",
                note = "Midnight snacks & beverages",
                amount = 890.0,
                category = "Groceries",
                paymentMode = "UPI (GPay / PhonePe)",
                timestamp = timeFor(2, 23, 10),
                isCredit = false,
                isRecurring = false,
                tags = "#snacks"
            ),
            ExpenseEntity(
                merchant = "Uber Premier",
                note = "Airport commute to T2",
                amount = 540.0,
                category = "Travel",
                paymentMode = "UPI (GPay / PhonePe)",
                timestamp = timeFor(2, 9, 30),
                isCredit = false,
                isRecurring = false,
                tags = "#travel"
            ),
            ExpenseEntity(
                merchant = "Amazon India",
                note = "Mechanical keyboard",
                amount = 4300.0,
                category = "Shopping",
                paymentMode = "HDFC Card ••4091",
                timestamp = timeFor(3, 16, 20),
                isCredit = false,
                isRecurring = false,
                tags = "#electronics #setup"
            ),
            ExpenseEntity(
                merchant = "Netflix India Subscription",
                note = "Premium UHD 4-screen",
                amount = 649.0,
                category = "Movies",
                paymentMode = "Net Banking",
                timestamp = timeFor(3, 10, 0),
                isCredit = false,
                isRecurring = true,
                recurringFrequency = "Monthly",
                tags = "#subscription"
            ),
            ExpenseEntity(
                merchant = "Tata Power Electricity",
                note = "Monthly electric utility bill",
                amount = 2350.0,
                category = "Utilities",
                paymentMode = "Net Banking",
                timestamp = timeFor(6, 12, 0),
                isCredit = false,
                isRecurring = true,
                recurringFrequency = "Monthly",
                tags = "#bills"
            ),
            ExpenseEntity(
                merchant = "Airtel Xstream Fiber",
                note = "High-speed broadband plan",
                amount = 1179.0,
                category = "Utilities",
                paymentMode = "UPI (GPay / PhonePe)",
                timestamp = timeFor(9, 15, 30),
                isCredit = false,
                isRecurring = true,
                recurringFrequency = "Monthly",
                tags = "#wifi"
            ),
            ExpenseEntity(
                merchant = "Zara Mall of India",
                note = "Autumn formal wear",
                amount = 3200.0,
                category = "Shopping",
                paymentMode = "HDFC Card ••4091",
                timestamp = timeFor(8, 19, 45),
                isCredit = false,
                isRecurring = false,
                tags = "#clothing"
            ),
            ExpenseEntity(
                merchant = "Cult.fit Gym Pass",
                note = "Group workout membership",
                amount = 1690.0,
                category = "Health",
                paymentMode = "UPI (GPay / PhonePe)",
                timestamp = timeFor(10, 8, 0),
                isCredit = false,
                isRecurring = true,
                recurringFrequency = "Monthly",
                tags = "#fitness"
            ),
            ExpenseEntity(
                merchant = "PVR Cinemas IMAX",
                note = "Weekend cinema tickets & popcorn",
                amount = 1200.0,
                category = "Movies",
                paymentMode = "Debit Card",
                timestamp = timeFor(12, 21, 0),
                isCredit = false,
                isRecurring = false,
                tags = "#entertainment"
            ),
            ExpenseEntity(
                merchant = "Dineout Cafe Social",
                note = "Sunday brunch with friends",
                amount = 2800.0,
                category = "Food",
                paymentMode = "UPI (GPay / PhonePe)",
                timestamp = timeFor(13, 14, 0),
                isCredit = false,
                isRecurring = false,
                tags = "#dining"
            ),
            ExpenseEntity(
                merchant = "Fuel HPCL Auto Care",
                note = "Highway petrol top-up",
                amount = 1800.0,
                category = "Travel",
                paymentMode = "Cash",
                timestamp = timeFor(15, 11, 30),
                isCredit = false,
                isRecurring = false,
                tags = "#fuel"
            ),
            ExpenseEntity(
                merchant = "Salary Credited",
                note = "ICICI NetBanking ••8112",
                amount = 95000.0,
                category = "Income",
                paymentMode = "Net Banking",
                timestamp = timeFor(23, 9, 0),
                isCredit = true,
                isRecurring = true,
                recurringFrequency = "Monthly",
                tags = "#salary #payday"
            ),
            ExpenseEntity(
                merchant = "Freelance Consulting Payout",
                note = "Fintech app UX design contract",
                amount = 30000.0,
                category = "Income",
                paymentMode = "UPI (GPay / PhonePe)",
                timestamp = timeFor(14, 16, 0),
                isCredit = true,
                isRecurring = false,
                tags = "#freelance"
            )
        )
    }
}
