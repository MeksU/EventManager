package pl.meksu.eventmanager.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import pl.meksu.eventmanager.EventViewModel
import pl.meksu.eventmanager.Screen

@Composable
fun LikedView(
    navController: NavController,
    viewModel: EventViewModel
) {
    val scaffoldState = rememberScaffoldState()
    val events by viewModel.searchEvents("").collectAsState(initial = listOf())

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarView(
                title= "Favorite Events",
                onBackNavClicked = { navController.navigateUp() }
            )
        },
    ) {
        Column {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(it)
            ) {
                items(events, key= { event-> event.id }) {
                    event ->
                    val isLiked by viewModel.isEventLiked(event.id, viewModel.loggedUser).collectAsState(initial = false)
                    if(isLiked) {
                        EventUserItem(
                            event = event,
                            onClick = {
                                val id = event.id
                                navController.navigate(Screen.DetailScreen.route + "/$id")
                            }
                        )
                    }
                }
            }
        }
    }
}