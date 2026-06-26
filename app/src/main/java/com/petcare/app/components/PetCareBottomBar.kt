package com.petcare.app.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.petcare.app.ui.theme.PrimaryPurple
import com.petcare.app.ui.theme.TextSecondary

// Definimos los elementos de navegación
sealed class NavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Inicio : NavItem("inicio", "Inicio", Icons.Filled.Home, Icons.Outlined.Home)
    object Mascotas : NavItem("mascotas", "Mascotas", Icons.Filled.Pets, Icons.Outlined.Pets)
    object Recordatorios : NavItem("recordatorios", "Alertas", Icons.Filled.Notifications, Icons.Outlined.Notifications)
    object Perfil : NavItem("perfil", "Perfil", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun PetCareBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        NavItem.Inicio,
        NavItem.Mascotas,
        NavItem.Recordatorios,
        NavItem.Perfil
    )

    NavigationBar(
        containerColor = NavigationBarDefaults.containerColor, // O usa tu color Surface
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPurple,
                    selectedTextColor = PrimaryPurple,
                    indicatorColor = PrimaryPurple.copy(alpha = 0.1f),
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}