package pl.meksu.eventmanager.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import pl.meksu.eventmanager.data.Event
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.meksu.eventmanager.EventViewModel
import pl.meksu.eventmanager.Screen
import pl.meksu.eventmanager.SortByAddressStrategy
import pl.meksu.eventmanager.SortByDateStrategy
import pl.meksu.eventmanager.SortByNameStrategy
import pl.meksu.eventmanager.SortingStrategy

@Composable
fun HomeView(
    navController: NavController,
    viewModel: EventViewModel
) {
    var sortingStrategy by remember { mutableStateOf<SortingStrategy>(SortByNameStrategy()) }
    val events = viewModel.getAllEvents.collectAsState(initial = listOf()).value.let {
        sortingStrategy.sortEvents(it)
    }
    var expanded by remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarMainView(
                title= "Event Manager",
                myIcon = Icons.Default.Face,
                onBackNavClicked = { navController.navigateUp() },
                onIconClicked = { navController.navigate(Screen.DeleteScreen.route) },
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
                    navController.navigate(Screen.AddScreen.route + "/0L")
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add event button")
            }
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(it)) {
            items(events, key= { event-> event.id }) {
                event ->

                EventItem(
                    event = event,
                    onClick = {
                        val id = event.id
                        navController.navigate(Screen.AddScreen.route + "/$id")
                    },
                    onButtonClick = {
                        viewModel.deleteEvent(event)
                        viewModel.viewModelScope.launch {
                            viewModel.removeLikedAdmin(event.id)
                        }
                    },
                    icon = Icons.Default.Clear,
                    myColor = Color.Black
                )
            }
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    onClick: () -> Unit,
    onButtonClick: () -> Unit,
    icon: ImageVector,
    myColor: Color) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
        .clickable {
            onClick()
        },
        elevation = 10.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp).clickable { onClick() }
        ){
            Text(text = event.title, fontWeight = FontWeight.ExtraBold, color = Color.Black)
            Row {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date icon")
                Text(text = event.date, color = Color.Black)
            }
            Row {
                Icon(imageVector = Icons.Default.Place, contentDescription = "Address icon")
                Text(text = event.address, color = Color.Black)
            }
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            IconButton(onClick = { onButtonClick() }) {
                Icon(imageVector = icon, contentDescription = "Delete event button", tint = myColor)
            }
        }
    }
}

@Composable
fun SortingDropdownMenu(
    sortingOptions: List<String>,
    onSortingOptionSelected: (String) -> Unit,
    expanded: Boolean,
    onToggle: (Boolean) -> Unit
) {
    IconButton(onClick = { onToggle(!expanded) }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.List,
            contentDescription = "Sort Button",
            tint = Color.White
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onToggle(false) }
    ) {
        sortingOptions.forEach { option ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = option,
                        modifier = Modifier.padding(8.dp)
                    )
                },
                onClick = {
                    onToggle(false)
                    onSortingOptionSelected(option)
                }
            )
        }
    }
}