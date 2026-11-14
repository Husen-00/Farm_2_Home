package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppStrings
import com.example.localization.Language
import com.example.model.FarmNotification
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerConnectTopBar(
    user: UserProfile,
    currentLanguage: Language,
    notifications: List<FarmNotification>,
    onToggleLanguage: () -> Unit,
    onToggleRole: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val unreadCount = notifications.count { !it.isRead }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(PureWhiteCard)
                        .border(1.5.dp, GoldenYellowAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🌾", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = AppStrings.appTitle.get(currentLanguage),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PureWhiteCard
                        )
                    )
                    Text(
                        text = AppStrings.appSubtitle.get(currentLanguage),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SoftMintContainer
                        )
                    )
                }
            }
        },
        actions = {
            // Notifications Bell Icon with Golden Yellow Highlight Badge
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier.testTag("notifications_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge(
                                containerColor = GoldenYellowAccent,
                                contentColor = DarkCharcoalText
                            ) {
                                Text("$unreadCount", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = PureWhiteCard
                    )
                }
            }

            // Role Indicator Chip
            AssistChip(
                onClick = onToggleRole,
                label = {
                    Text(
                        text = when (user.role) {
                            UserRole.FARMER -> "👨‍🌾 ${AppStrings.roleFarmer.get(currentLanguage)}"
                            UserRole.BUYER -> "🛒 ${AppStrings.roleBuyer.get(currentLanguage)}"
                            UserRole.ADMIN -> "🛡️ ${AppStrings.roleAdmin.get(currentLanguage)}"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PureWhiteCard
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = EmeraldGreenSecondary.copy(alpha = 0.85f),
                    labelColor = PureWhiteCard
                ),
                border = null,
                modifier = Modifier
                    .testTag("toggle_role_chip")
                    .padding(end = 6.dp)
            )

            // Language Toggle (Golden Yellow Accent Pill for high visibility)
            FilterChip(
                selected = true,
                onClick = onToggleLanguage,
                label = {
                    Text(
                        text = if (currentLanguage == Language.ENGLISH) "ಕನ್ನಡ" else "English",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "Language",
                        modifier = Modifier.size(14.dp),
                        tint = DarkCharcoalText
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GoldenYellowAccent,
                    selectedLabelColor = DarkCharcoalText
                ),
                border = null,
                modifier = Modifier
                    .testTag("language_toggle_button")
                    .padding(end = 6.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ForestGreenPrimary,
            titleContentColor = PureWhiteCard,
            actionIconContentColor = PureWhiteCard
        ),
        modifier = modifier
    )
}

@Composable
fun FarmerConnectBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    language: Language,
    modifier: Modifier = Modifier
) {
    val navItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = ForestGreenPrimary,
        selectedTextColor = ForestGreenPrimary,
        indicatorColor = SoftMintContainer,
        unselectedIconColor = GreySecondaryText,
        unselectedTextColor = GreySecondaryText
    )

    Column(modifier = modifier) {
        HorizontalDivider(thickness = 1.dp, color = SoftSlateBorder)
        NavigationBar(
            containerColor = PureWhiteCard,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets.navigationBars,
            modifier = Modifier.testTag("bottom_nav_bar")
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home"
                    )
                },
                label = {
                    Text(
                        AppStrings.tabHome.get(language),
                        fontSize = 11.sp,
                        maxLines = 1,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                colors = navItemColors,
                modifier = Modifier.testTag("tab_home")
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedTab == 1) Icons.Filled.Storefront else Icons.Outlined.Storefront,
                        contentDescription = "Marketplace"
                    )
                },
                label = {
                    Text(
                        AppStrings.tabMarketplace.get(language),
                        fontSize = 11.sp,
                        maxLines = 1,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                colors = navItemColors,
                modifier = Modifier.testTag("tab_marketplace")
            )

            NavigationBarItem(
                icon = {
                    BadgedBox(badge = {
                        Badge(
                            containerColor = GoldenYellowAccent,
                            contentColor = DarkCharcoalText
                        ) {
                            Text("AI", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }) {
                        Icon(
                            imageVector = if (selectedTab == 2) Icons.Filled.Psychology else Icons.Outlined.Psychology,
                            contentDescription = "Krishi AI"
                        )
                    }
                },
                label = {
                    Text(
                        AppStrings.tabAi.get(language),
                        fontSize = 11.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold
                    )
                },
                selected = selectedTab == 2,
                onClick = { onTabSelected(2) },
                colors = navItemColors,
                modifier = Modifier.testTag("tab_krishi_ai")
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedTab == 3) Icons.Filled.Agriculture else Icons.Outlined.Agriculture,
                        contentDescription = "Services"
                    )
                },
                label = {
                    Text(
                        AppStrings.tabServices.get(language),
                        fontSize = 11.sp,
                        maxLines = 1,
                        fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selected = selectedTab == 3,
                onClick = { onTabSelected(3) },
                colors = navItemColors,
                modifier = Modifier.testTag("tab_services")
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedTab == 4) Icons.Filled.Person else Icons.Outlined.Person,
                        contentDescription = "Profile"
                    )
                },
                label = {
                    Text(
                        AppStrings.tabProfile.get(language),
                        fontSize = 11.sp,
                        maxLines = 1,
                        fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selected = selectedTab == 4,
                onClick = { onTabSelected(4) },
                colors = navItemColors,
                modifier = Modifier.testTag("tab_profile")
            )
        }
    }
}
