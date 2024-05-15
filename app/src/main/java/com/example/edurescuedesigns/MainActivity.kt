package com.example.edurescuedesigns
import ChatRoomScreen
import MapScreen
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.edurescuedesigns.classes.ContextSingleton
import com.example.edurescuedesigns.classes.Network
import com.example.edurescuedesigns.ui.theme.AppTheme
import com.google.android.gms.maps.model.LatLng


data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null
)
class MainActivity : AppCompatActivity() {
    //@OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextSingleton.initialize(this)
        //REMOVE THIS LINE TO SAVE LOGIN DATA
        Network().removeToken()


        //Check if user is already logged in
        var startDestination:String = "login"

        Network().getUserInfo().thenAccept { userRes ->
            if (userRes.validToken) {
                Log.d("Token RES", "valid")
                startDestination = "homepage"
                if(userRes.type == "professor"){
                    Network().setProfessorMarker(LatLng(0.0,0.0))
                }
            } else {
                    Log.d("Token RES", "invalid")
            }
        }



        setContent {
            val homeTab = TabBarItem(title = "Homepage", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
            val chatroomTab = TabBarItem(title = "Chatroom", selectedIcon = Icons.Filled.ChatBubble, unselectedIcon = Icons.Outlined.ChatBubble)
            val mapTab = TabBarItem(title = "Map", selectedIcon = Icons.Filled.PinDrop, unselectedIcon = Icons.Outlined.PinDrop)
            val rollcallTab = TabBarItem(title = "Rollcall", selectedIcon = Icons.Filled.Checklist, unselectedIcon = Icons.Outlined.Checklist)
            val tabBarItemsStudent = listOf(homeTab, chatroomTab,mapTab)
            val tabBarItemsProfessor = listOf(homeTab, chatroomTab,mapTab,rollcallTab)
            var shouldShowBottomBar = rememberSaveable { mutableStateOf(false) }
            var userIsStudent = rememberSaveable { mutableStateOf(true) }
            val themeViewModel = remember { ThemeViewModel()}
            val themeState by themeViewModel.uiState.collectAsState()
            // App's children will inherit theme if user selects dark mode
            AppTheme(themeState) {
                val navController = rememberNavController()
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    Scaffold (bottomBar = {
                        // Do not show bottom bar on login page
                        if(shouldShowBottomBar.value && navController.currentBackStackEntry?.destination?.route != "login") {
                            if(userIsStudent.value){
                                TabView(tabBarItemsStudent, navController)
                            }else{
                                TabView(tabBarItemsProfessor, navController)
                            }

                        }
                        }){
                        NavHost(
                            navController = navController,
                            startDestination = startDestination){
                            composable(route="homepage"){
                                HomePageForm(navController,shouldShowBottomBar,userIsStudent)}
                            composable(route="login"){
                                LoginForm(navController)
                            }
                            composable(route="chatroom"){
                                ChatRoomScreen(navController = navController)
                            }
                            composable(route = "register"){
                                RegisterForm(navController)
                            }
                            composable(route="map"){
                                MapScreen()
                            }
                            composable(route="rollcall"){
                                Rollcall(navController)
                            }
                            composable(route="profile"){
                                Profile(navController)
                            }
                            composable(route ="settings"){
                                Settings(navController, themeViewModel)
                            }

                        }

                    }

                    }


            }
        }
    }
}

@Composable
fun TabView(tabBarItems: List<TabBarItem>, navController: NavController){
    var selectedTabIndex = rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar {
        tabBarItems.forEachIndexed{ index, tabBarItem ->
            NavigationBarItem(selected = selectedTabIndex.value == index,
                onClick = {
                    selectedTabIndex.value = index
                    navController.navigate(tabBarItem.title)
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex.value == index,
                        selectedIcon = tabBarItem.selectedIcon,
                        unselectedIcon = tabBarItem.unselectedIcon,
                        title = tabBarItem.title,
                        badgeAmount = tabBarItem.badgeAmount
                    )
                },
                label = {Text(tabBarItem.title)}
            )
        }
    }
}



@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String,
    badgeAmount: Int? = null
){
    BadgedBox(badge = {TabBarBadgeView(badgeAmount)}) {
        Icon(
            imageVector = if (isSelected) {selectedIcon} else {unselectedIcon},
            contentDescription = title
        )
    }
}

@Composable
fun TabBarBadgeView(count: Int? = null){
    if (count != null){
        Badge{
            Text(count.toString())
        }
    }
}
