package pl.meksu.eventmanager

sealed class Screen(val route: String) {
    data object HomeScreen: Screen("home_screen")
    data object AddScreen: Screen("add_screen")
    data object LoginScreen: Screen("login_screen")
    data object UserScreen: Screen("user_screen")
    data object DetailScreen: Screen("detail_screen")
    data object SearchScreen: Screen("search_screen")
    data object LikedScreen: Screen("liked_screen")
    data object DeleteScreen: Screen("delete_screen")
}