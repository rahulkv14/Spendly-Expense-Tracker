package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixed

sealed class NavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : NavItem("home", "Home", Icons.Default.Home)
    object Expenses : NavItem("expenses", "Expenses", Icons.Default.ReceiptLong)
    object Analytics : NavItem("analytics", "Analytics", Icons.Default.PieChart)
    object Settings : NavItem("settings", "Settings", Icons.Default.Tune)
}

@Composable
fun SpendlyBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onQuickAddClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                NavButton(
                    item = NavItem.Home,
                    isSelected = currentRoute == NavItem.Home.route,
                    onClick = { onNavigate(NavItem.Home.route) }
                )

                // Expenses
                NavButton(
                    item = NavItem.Expenses,
                    isSelected = currentRoute == NavItem.Expenses.route,
                    onClick = { onNavigate(NavItem.Expenses.route) }
                )

                // Center placeholder spacer for elevated add button
                Box(modifier = Modifier.size(52.dp))

                // Analytics
                NavButton(
                    item = NavItem.Analytics,
                    isSelected = currentRoute == NavItem.Analytics.route,
                    onClick = { onNavigate(NavItem.Analytics.route) }
                )

                // Settings
                NavButton(
                    item = NavItem.Settings,
                    isSelected = currentRoute == NavItem.Settings.route,
                    onClick = { onNavigate(NavItem.Settings.route) }
                )
            }
        }

        // Elevated Center Plus Pill Button
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-6).dp)
                .size(52.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(PrimaryContainer)
                .clickable { onQuickAddClick() }
                .testTag("nav_quick_add_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Transaction",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun NavButton(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(width = 66.dp, height = 48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) PrimaryFixed.copy(alpha = 0.35f) else Color.Transparent)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .testTag("nav_tab_${item.route}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = if (isSelected) PrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = item.title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) PrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
