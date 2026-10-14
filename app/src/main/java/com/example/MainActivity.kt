package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.NotificationDialog
import com.example.ui.components.SpendlyBottomNav
import com.example.ui.components.SpendlyTopBar
import com.example.ui.screens.AddExpenseScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.ExpenseHistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.InverseOnSurface
import com.example.ui.theme.InverseSurface
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SpendlyTheme
import com.example.ui.viewmodel.ExpenseViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val viewModel: ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendlyTheme {
                SpendlyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SpendlyApp(viewModel: ExpenseViewModel) {
    var currentScreen by remember { mutableStateOf("home") }
    var previousScreen by remember { mutableStateOf("home") }
    var showNotificationDialog by remember { mutableStateOf(false) }

    val toastMessage by viewModel.toastMessage.collectAsState()

    // Auto-dismiss toast
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2400)
            viewModel.clearToast()
        }
    }

    // Back handling
    BackHandler(enabled = currentScreen != "home") {
        if (currentScreen == "add_expense") {
            viewModel.setEditing(null)
            currentScreen = previousScreen
        } else {
            currentScreen = "home"
        }
    }

    if (showNotificationDialog) {
        NotificationDialog(onDismiss = { showNotificationDialog = false })
    }

    val topBarTitle = when (currentScreen) {
        "home" -> "Home Dashboard"
        "expenses" -> "Expense History & Filter"
        "analytics" -> "Spending Analytics & Trends"
        "settings" -> "Budget & Preferences Settings"
        "add_expense" -> "Add Expense Form"
        else -> "Spendly"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SpendlyTopBar(
                title = topBarTitle,
                showBackButton = currentScreen == "add_expense",
                onBackClick = {
                    viewModel.setEditing(null)
                    currentScreen = previousScreen
                },
                onNotificationClick = { showNotificationDialog = true }
            )
        },
        bottomBar = {
            if (currentScreen != "add_expense") {
                SpendlyBottomNav(
                    currentRoute = currentScreen,
                    onNavigate = { route ->
                        previousScreen = currentScreen
                        currentScreen = route
                    },
                    onQuickAddClick = {
                        viewModel.setEditing(null)
                        previousScreen = currentScreen
                        currentScreen = "add_expense"
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                "home" -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToAdd = {
                        viewModel.setEditing(null)
                        previousScreen = "home"
                        currentScreen = "add_expense"
                    },
                    onNavigateToExpenses = {
                        previousScreen = "home"
                        currentScreen = "expenses"
                    },
                    onEditExpense = { expense ->
                        viewModel.setEditing(expense)
                        previousScreen = "home"
                        currentScreen = "add_expense"
                    }
                )

                "expenses" -> ExpenseHistoryScreen(
                    viewModel = viewModel,
                    onEditExpense = { expense ->
                        viewModel.setEditing(expense)
                        previousScreen = "expenses"
                        currentScreen = "add_expense"
                    }
                )

                "analytics" -> AnalyticsScreen(
                    viewModel = viewModel
                )

                "settings" -> SettingsScreen(
                    viewModel = viewModel
                )

                "add_expense" -> AddExpenseScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        currentScreen = previousScreen
                    }
                )
            }

            // Animated Floating Toast Pill
            AnimatedVisibility(
                visible = toastMessage != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = if (currentScreen == "add_expense") 70.dp else 16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = InverseSurface,
                    shadowElevation = 8.dp,
                    modifier = Modifier.testTag("action_toast_pill")
                ) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SecondaryFixed,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = toastMessage ?: "",
                            style = MaterialTheme.typography.labelLarge,
                            color = InverseOnSurface
                        )
                    }
                }
            }
        }
    }
}
