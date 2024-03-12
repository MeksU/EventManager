package pl.meksu.eventmanager.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.meksu.eventmanager.EventViewModel

@Composable
fun DeleteUsersView(
    navController: NavController,
    viewModel: EventViewModel
) {
    val users = viewModel.getAllUsers.collectAsState(initial = listOf()).value
    val scaffoldState = rememberScaffoldState()
    var showDialog by remember { mutableStateOf(false) }
    var deleteLogin by remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarView(
                title= "Delete Users",
                onBackNavClicked = { navController.navigateUp() },
            )
        },
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(it)) {
            items(users, key= { user -> user.login }) {
                user ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                    elevation = 10.dp,
                    backgroundColor = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ){
                        Text(text = user.login, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                    }
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        IconButton(onClick = {
                            showDialog = true
                            deleteLogin = user.login
                        }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Delete event button", tint = Color.Black)
                        }
                    }
                }
            }
        }
    }
    if(showDialog) {
        ConfirmationDialog(
            onConfirm = {
                viewModel.viewModelScope.launch {
                    viewModel.deleteUser(deleteLogin)
                    viewModel.removeUserLiked(deleteLogin)
                }
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            },
            yes = "Delete",
            no = "Cancel",
            title = "Delete user",
            text = "Are you sure you want to delete $deleteLogin?"
        )
    }
}