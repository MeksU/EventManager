package pl.meksu.eventmanager.views

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pl.meksu.eventmanager.EventViewModel
import pl.meksu.eventmanager.Screen
import pl.meksu.eventmanager.SortByAddressStrategy
import pl.meksu.eventmanager.SortByDateStrategy
import pl.meksu.eventmanager.SortByNameStrategy
import pl.meksu.eventmanager.SortingStrategy
import pl.meksu.eventmanager.data.LikedEvent

@Composable
fun UserView(
    navController: NavController,
    viewModel: EventViewModel
) {
    var sortingStrategy by remember { mutableStateOf<SortingStrategy>(SortByNameStrategy()) }
    val events = viewModel.getAllEvents.collectAsState(initial = listOf()).value.let {
        sortingStrategy.sortEvents(it)
    }
    var expanded by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarMainView(
                title= "Event Manager",
                myIcon = Icons.Default.Search,
                onBackNavClicked = { navController.navigateUp() },
                onIconClicked = { navController.navigate(Screen.SearchScreen.route) },
                sortIcon = {
                    SortingDropdownMenu(
                        sortingOptions = listOf("Sort by name", "Sort by date", "Sort by address"),
                        onSortingOptionSelected = { option ->
                            sortingStrategy = when (option) {
                                "Sort by name" -> SortByNameStrategy()
                                "Sort by date" -> SortByDateStrategy()
                                "Sort by address" -> SortByAddressStrategy()
                                else -> SortByNameStrategy()
                            }
                            events.let {
                                sortingStrategy.sortEvents(it)
                            }
                        },
                        expanded = expanded,
                        onToggle = { expanded = it }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                backgroundColor = Color.Black,
                onClick = {
                    navController.navigate(Screen.LikedScreen.route)
                }
            ) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Search event button")
            }
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(it)) {
            items(events, key= { event-> event.id }) {
                event ->
                val id = event.id
                val isLiked by viewModel.isEventLiked(event.id, viewModel.loggedUser).collectAsState(initial = false)
                if(!isLiked) {
                    EventItem(
                        event = event,
                        onClick = {
                            navController.navigate(Screen.DetailScreen.route + "/$id")
                        },
                        onButtonClick = {
                            viewModel.viewModelScope.launch {
                                viewModel.addLikedEvent(
                                    LikedEvent(
                                        eventId = id,
                                        userLogin = viewModel.loggedUser
                                    )
                                )
                            }
                            Toast.makeText(context, "Event added to favourites!", Toast.LENGTH_SHORT).show()
                        },
                        icon = Icons.Default.Favorite,
                        myColor = Color.Black
                    )
                }
                else {
                    EventItem(
                        event = event,
                        onClick = {
                            navController.navigate(Screen.DetailScreen.route + "/$id")
                        },
                        onButtonClick = {
                            viewModel.viewModelScope.launch {
                                viewModel.removeLikedEvent(event.id, viewModel.loggedUser)
                            }
                            Toast.makeText(context, "Event removed from favourites", Toast.LENGTH_SHORT).show()
                        },
                        icon = Icons.Default.Favorite,
                        myColor = Color.Red
                    )
                }
            }
        }
    }
}