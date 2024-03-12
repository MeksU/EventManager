package pl.meksu.eventmanager

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pl.meksu.eventmanager.views.*

@Composable
fun Navigation(
    viewModel: EventViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController= navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(Screen.LoginScreen.route) {
            LoginView(navController, viewModel)
        }
        composable(Screen.HomeScreen.route) {
            HomeView(navController, viewModel)
        }
        composable(Screen.UserScreen.route) {
            UserView(navController, viewModel)
        }
        composable(
            route = Screen.AddScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                }
            )
        ) {entry->
            val id = if(entry.arguments != null) entry.arguments!!.getLong("id") else 0L
            AddEditView(id = id, viewModel = viewModel, navController = navController)
        }
        composable(
            route = Screen.DetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                }
            )
        ) {entry->
            val id = if(entry.arguments != null) entry.arguments!!.getLong("id") else 0L
            DetailView(id = id, viewModel = viewModel, navController = navController)
        }
        composable(Screen.SearchScreen.route) {
            SearchView(navController, viewModel)
        }
        composable(Screen.LikedScreen.route) {
            LikedView(navController, viewModel)
        }
        composable(Screen.DeleteScreen.route) {
            DeleteUsersView(navController, viewModel)
        }
    }
}