package pl.meksu.eventmanager.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.meksu.eventmanager.EventViewModel
import pl.meksu.eventmanager.Screen
import pl.meksu.eventmanager.data.Event

@Composable
fun SearchView(
    navController: NavController,
    viewModel: EventViewModel
) {
    val scaffoldState = rememberScaffoldState()
    var searchKey by remember { mutableStateOf("") }
    var searchCriteria by remember { mutableStateOf("Name") }
    val eventsN by viewModel.searchEvents(searchKey).collectAsState(initial = listOf())
    val eventsD by viewModel.searchByDate(searchKey).collectAsState(initial = listOf())
    val eventsA by viewModel.searchByAddress(searchKey).collectAsState(initial = listOf())

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarView(
                title= "Events Search",
                onBackNavClicked = { navController.navigateUp() }
            )
        },
    ) {
        Column {
            Spacer(modifier = Modifier.padding(it))
            OutlinedTextField(
                value = searchKey,
                onValueChange = {
                    searchKey = it
                },
                label = { Text(text = "Search", color = Color.Black) },
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = searchCriteria == "Name",
                        onClick = {
                            searchCriteria = "Name"
                        },
                        colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.primary),
                    )
                    Text("All")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = searchCriteria == "Date",
                        onClick = {
                            searchCriteria = "Date"
                        },
                        colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.primary)
                    )
                    Text("By Date")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = searchCriteria == "Place",
                        onClick = {
                            searchCriteria = "Place"
                        },
                        colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.primary)
                    )
                    Text("By Place")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                when (searchCriteria) {
                    "Name" -> {
                        items(eventsN, key= { event-> event.id }) {
                                event ->
                            EventUserItem(
                                event = event,
                                onClick = {
                                    val id = event.id
                                    navController.navigate(Screen.DetailScreen.route + "/$id")
                                }
                            )
                        }
                    }
                    "Date" -> {
                        items(eventsD, key= { event-> event.id }) {
                                event ->
                            EventUserItem(
                                event = event,
                                onClick = {
                                    val id = event.id
                                    navController.navigate(Screen.DetailScreen.route + "/$id")
                                }
                            )
                        }
                    }
                    "Place" -> {
                        items(eventsA, key= { event-> event.id }) {
                                event ->
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
}

@Composable
fun EventUserItem(event: Event, onClick: () -> Unit) {
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
    }
}