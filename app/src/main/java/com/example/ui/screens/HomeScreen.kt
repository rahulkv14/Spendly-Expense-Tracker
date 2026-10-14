package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExpenseCategories
import com.example.data.model.ExpenseEntity
import com.example.ui.components.DeleteConfirmationDialog
import com.example.ui.components.EditBudgetDialog
import com.example.ui.components.ScanUpiDialog
import com.example.ui.theme.ForestDarkGradientEnd
import com.example.ui.theme.ForestDarkGradientMid
import com.example.ui.theme.ForestDarkGradientStart
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixedDim
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SecondaryFixedDim
import com.example.ui.viewmodel.ExpenseViewModel
import com.example.util.CurrencyUtils
import com.example.util.DateTimeUtils

@Composable
fun HomeScreen(
    viewModel: ExpenseViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToExpenses: () -> Unit,
    onEditExpense: (ExpenseEntity) -> Unit
) {
    val context = LocalContext.current
    val metrics by viewModel.dashboardMetrics.collectAsState()
    val allExpenses by viewModel.allExpenses.collectAsState()
    val recentExpenses = remember(allExpenses) { allExpenses.take(6) }

    var selectedChartDay by remember { mutableStateOf<String?>(null) }
    var selectedChartAmount by remember { mutableStateOf<Double?>(null) }

    var showEditBudgetDialog by remember { mutableStateOf(false) }
    var showScanUpiDialog by remember { mutableStateOf(false) }
    var expenseToDelete by remember { mutableStateOf<ExpenseEntity?>(null) }

    if (showEditBudgetDialog) {
        EditBudgetDialog(
            currentBudget = metrics.monthlyBudget,
            onDismiss = { showEditBudgetDialog = false },
            onSave = {
                viewModel.updateMonthlyBudget(it)
                showEditBudgetDialog = false
            }
        )
    }

    if (showScanUpiDialog) {
        ScanUpiDialog(
            onDismiss = { showScanUpiDialog = false },
            onScanned = { merchant, amount, category ->
                showScanUpiDialog = false
                viewModel.addExpense(
                    merchant = merchant,
                    amount = amount,
                    category = category,
                    note = "Paid via Spendly UPI QR",
                    paymentMode = "UPI (GPay / PhonePe)"
                )
            }
        )
    }

    if (expenseToDelete != null) {
        DeleteConfirmationDialog(
            title = "Delete Expense?",
            message = "Are you sure you want to delete ${expenseToDelete?.merchant} (${CurrencyUtils.formatInr(expenseToDelete?.amount ?: 0.0)})?",
            onConfirm = {
                expenseToDelete?.let { viewModel.deleteExpense(it.id) }
                expenseToDelete = null
            },
            onDismiss = { expenseToDelete = null }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .testTag("home_screen_content")
    ) {
        // User Greeting & Month Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Namaste, Priya",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "👋", fontSize = 20.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Secondary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "October 2024 overview",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Month switcher pill
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 1.dp,
                modifier = Modifier.clickable {
                    viewModel.showToast("Current active view: October 2024")
                }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Oct '24",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Switch Month",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Balance & Budget Card (Emerald Gradient Slate)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            ForestDarkGradientStart,
                            ForestDarkGradientMid,
                            ForestDarkGradientEnd
                        )
                    )
                )
                .padding(20.dp)
                .testTag("dashboard_hero_card")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Balance Header & Trend Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "TOTAL NET BALANCE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryFixedDim,
                            letterSpacing = 0.8.sp
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = CurrencyUtils.formatInr(metrics.totalBalance),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = ".00",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SecondaryFixedDim,
                                modifier = Modifier.padding(bottom = 3.dp, start = 2.dp)
                            )
                        }
                    }

                    // Trend badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = SecondaryFixed,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "+12.4%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SecondaryFixed
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Translucent Budget Module
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.22f))
                        .padding(14.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = SecondaryFixed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Monthly Budget: ${CurrencyUtils.formatInr(metrics.monthlyBudget)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SecondaryContainer.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = String.format("%.1f%% spent · On track", metrics.spentPercentage),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SecondaryFixed
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tactile Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                        ) {
                            val progress = (metrics.spentPercentage / 100f).coerceIn(0f, 1f)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(SecondaryFixedDim, SecondaryContainer)
                                        )
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Metric Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Spent so far",
                                    fontSize = 11.sp,
                                    color = SecondaryFixedDim
                                )
                                Text(
                                    text = CurrencyUtils.formatInr(metrics.monthlySpending),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Safe Daily Spend",
                                    fontSize = 11.sp,
                                    color = SecondaryFixedDim
                                )
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = CurrencyUtils.formatInr(metrics.safeDailySpend),
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SecondaryFixed
                                    )
                                    Text(
                                        text = "/day",
                                        fontSize = 12.sp,
                                        color = SecondaryFixedDim,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer Micro-badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = SecondaryFixed,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Remaining: ",
                            fontSize = 12.sp,
                            color = SecondaryFixedDim
                        )
                        Text(
                            text = CurrencyUtils.formatInr(metrics.remainingBudget),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "${metrics.remainingDays} days left",
                        fontSize = 11.sp,
                        color = SecondaryFixedDim
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick Action Row (4 Buttons)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. + Add
            QuickActionButton(
                modifier = Modifier.weight(1f),
                title = "+ Add",
                icon = Icons.Default.AddCircle,
                isPrimary = true,
                onClick = onNavigateToAdd,
                testTag = "quick_action_add"
            )

            // 2. Scan UPI
            QuickActionButton(
                modifier = Modifier.weight(1f),
                title = "Scan UPI",
                icon = Icons.Default.QrCodeScanner,
                isPrimary = false,
                onClick = { showScanUpiDialog = true },
                testTag = "quick_action_scan"
            )

            // 3. Set Limit
            QuickActionButton(
                modifier = Modifier.weight(1f),
                title = "Set Limit",
                icon = Icons.Default.Tune,
                isPrimary = false,
                onClick = { showEditBudgetDialog = true },
                testTag = "quick_action_set_limit"
            )

            // 4. Export
            QuickActionButton(
                modifier = Modifier.weight(1f),
                title = "Export",
                icon = Icons.Default.FileDownload,
                isPrimary = false,
                onClick = { viewModel.exportStatementCsv(context) },
                testTag = "quick_action_export"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Weekly Spending Trend Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Weekly Spending Trend",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Total this week: ${CurrencyUtils.formatInr(metrics.totalThisWeek)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Last 7 Days",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Interactive Bar Chart
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    metrics.weeklyDailySpending.forEach { daySpend ->
                        val isSelected = selectedChartDay == daySpend.dayName
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedChartDay = daySpend.dayName
                                    selectedChartAmount = daySpend.amount
                                }
                        ) {
                            if (daySpend.isPeak) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Secondary)
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = CurrencyUtils.formatCompactInr(daySpend.amount),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            // The bar
                            val barHeightFraction = (daySpend.percentageOfPeak / 100f).coerceIn(0.12f, 1f)
                            Box(
                                modifier = Modifier
                                    .width(26.dp)
                                    .height((90 * barHeightFraction).dp)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(
                                        when {
                                            daySpend.isPeak -> PrimaryContainer
                                            isSelected -> PrimaryFixedDim
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    )
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = daySpend.dayLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (daySpend.isPeak) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (daySpend.isPeak) Primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Callout Pill (Dynamic on selection or Peak by default)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Insights,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        if (selectedChartDay != null && selectedChartAmount != null) {
                            Text(
                                text = "Selected: $selectedChartDay · Spent ${CurrencyUtils.formatInr(selectedChartAmount!!)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        } else {
                            Text(
                                text = "Peak: ${metrics.peakDayName} (${CurrencyUtils.formatInr(metrics.peakDayAmount)}) · ${metrics.peakDayNotes}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Smart Habit Delight Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SecondaryContainer.copy(alpha = 0.22f))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SecondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Smart Habit",
                        tint = PrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SMART HABIT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryContainer,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "18% saved",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondary
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "You've spent 18% less on Food this week compared to last week. Great going, Priya!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Recent Transactions Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${recentExpenses.size} items",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            TextButton(
                onClick = onNavigateToExpenses,
                modifier = Modifier.testTag("see_all_transactions_button")
            ) {
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.labelLarge,
                    color = Secondary
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Recent Transactions List
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            recentExpenses.forEach { expense ->
                TransactionCardItem(
                    expense = expense,
                    onEdit = { onEditExpense(expense) },
                    onDelete = { expenseToDelete = expense }
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun QuickActionButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPrimary: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag),
        color = if (isPrimary) PrimaryContainer else MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = if (isPrimary) 3.dp else 1.dp,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPrimary) Color.White.copy(alpha = 0.15f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isPrimary) SecondaryFixed else Secondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = if (isPrimary) Color.White else MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun TransactionCardItem(
    expense: ExpenseEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val catMeta = ExpenseCategories.getCategoryMeta(expense.category)
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (expense.isCredit) SecondaryContainer
                                else catMeta.containerBg
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (expense.isCredit) Icons.Default.Payments else catMeta.icon,
                            contentDescription = expense.category,
                            tint = if (expense.isCredit) PrimaryContainer else catMeta.iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = expense.merchant,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1
                            )
                            if (expense.isCredit) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SecondaryFixed)
                                        .padding(horizontal = 6.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "CREDIT",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF002113)
                                    )
                                }
                            }
                        }
                        if (expense.note.isNotBlank()) {
                            Text(
                                text = expense.note,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                        Text(
                            text = "${expense.category} · ${DateTimeUtils.formatDate(expense.timestamp)}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.outline,
                            maxLines = 1
                        )
                        Text(
                            text = expense.paymentMode,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (expense.isCredit) "+${CurrencyUtils.formatInr(expense.amount, true)}"
                    else "-${CurrencyUtils.formatInr(expense.amount, true)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (expense.isCredit) Secondary else MaterialTheme.colorScheme.onSurface
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit", color = MaterialTheme.colorScheme.onSurface)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
