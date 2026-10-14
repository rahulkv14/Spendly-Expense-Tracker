package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryMeta(
    val id: String,
    val name: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconTint: Color,
    val containerBg: Color
)

object ExpenseCategories {
    val list = listOf(
        CategoryMeta(
            id = "Food",
            name = "Food",
            subtitle = "Swiggy/Cafe",
            icon = Icons.Default.Restaurant,
            iconTint = Color(0xFFC2410C),
            containerBg = Color(0xFFFFEDD5)
        ),
        CategoryMeta(
            id = "Groceries",
            name = "Groceries",
            subtitle = "Zepto/Blinkit",
            icon = Icons.Default.LocalMall,
            iconTint = Color(0xFF006C49),
            containerBg = Color(0xFFDCFCE7)
        ),
        CategoryMeta(
            id = "Travel",
            name = "Travel",
            subtitle = "Uber/Fuel",
            icon = Icons.Default.DirectionsCar,
            iconTint = Color(0xFF1D4ED8),
            containerBg = Color(0xFFDBEAFE)
        ),
        CategoryMeta(
            id = "Shopping",
            name = "Shopping",
            subtitle = "Amazon/Myntra",
            icon = Icons.Default.ShoppingBag,
            iconTint = Color(0xFF7E22CE),
            containerBg = Color(0xFFF3E8FF)
        ),
        CategoryMeta(
            id = "Movies",
            name = "Movies",
            subtitle = "PVR/OTT",
            icon = Icons.Default.Movie,
            iconTint = Color(0xFFBE185D),
            containerBg = Color(0xFFFCE7F3)
        ),
        CategoryMeta(
            id = "Utilities",
            name = "Utilities",
            subtitle = "Wi-Fi/Power",
            icon = Icons.Default.Bolt,
            iconTint = Color(0xFFB45309),
            containerBg = Color(0xFFFEF3C7)
        ),
        CategoryMeta(
            id = "Health",
            name = "Health",
            subtitle = "Apollo/1mg",
            icon = Icons.Default.Medication,
            iconTint = Color(0xFF047857),
            containerBg = Color(0xFFD1FAE5)
        ),
        CategoryMeta(
            id = "More",
            name = "More",
            subtitle = "Education...",
            icon = Icons.Default.GridView,
            iconTint = Color(0xFF334155),
            containerBg = Color(0xFFF1F5F9)
        )
    )

    fun getCategoryMeta(id: String): CategoryMeta {
        return list.find { it.id.equals(id, ignoreCase = true) || it.name.equals(id, ignoreCase = true) }
            ?: CategoryMeta(
                id = id,
                name = id,
                subtitle = "Expense",
                icon = Icons.Default.GridView,
                iconTint = Color(0xFF334155),
                containerBg = Color(0xFFF1F5F9)
            )
    }

    val paymentModes = listOf(
        "UPI",
        "Credit Card",
        "Debit Card",
        "Cash",
        "Net Banking"
    )

    fun normalizePaymentMode(mode: String): String {
        return when {
            mode.contains("UPI", ignoreCase = true) -> "UPI"
            mode.contains("Credit", ignoreCase = true) || mode.contains("HDFC", ignoreCase = true) -> "Credit Card"
            mode.contains("Debit", ignoreCase = true) -> "Debit Card"
            mode.contains("Cash", ignoreCase = true) -> "Cash"
            mode.contains("Net", ignoreCase = true) || mode.contains("Bank", ignoreCase = true) -> "Net Banking"
            else -> mode.ifBlank { "UPI" }
        }
    }
}
