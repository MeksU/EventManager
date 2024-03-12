package pl.meksu.eventmanager.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pl.meksu.eventmanager.data.Event
import kotlinx.coroutines.launch
import pl.meksu.eventmanager.EventViewModel
import pl.meksu.eventmanager.R

@Composable
fun AddEditView(
    id: Long,
    viewModel: EventViewModel,
    navController: NavController
){
    var check: Boolean by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if (id != 0L) {
        val event = viewModel.getEventById(id).collectAsState(initial = Event(0L, "", ""))
        viewModel.eventTitleState = event.value.title
        viewModel.eventDateState = event.value.date
        viewModel.eventTimeState = event.value.time
        viewModel.eventAddressState = event.value.address
        viewModel.eventLinkState = event.value.link
    } else {
        viewModel.eventTitleState = ""
        viewModel.eventDateState = ""
        viewModel.eventTimeState = ""
        viewModel.eventAddressState = ""
        viewModel.eventLinkState = ""
    }

    Scaffold(
        topBar = {
            AppBarView(
                title = if(id != 0L) stringResource(id = R.string.update_event)
                    else stringResource(id = R.string.add_event),
                onBackNavClicked = {
                    navController.navigateUp()
                }
            )
        },
        scaffoldState = scaffoldState
    ) { it ->
        Column(modifier = Modifier
            .padding(it)
            .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Spacer(modifier = Modifier.height(10.dp))

            EventTextField(
                label = "Name",
                value = viewModel.eventTitleState,
                onValueChanged = {
                    viewModel.onEventTitleChanged(it)
                },
                keyboard = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(10.dp))

            EventTextField(
                label = "Date (YYYY-MM-DD)",
                value = viewModel.eventDateState,
                onValueChanged = {
                    viewModel.onEventDateChanged(it)
                },
                keyboard = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(10.dp))

            EventTextField(
                label = "Time (hh:mm)",
                value = viewModel.eventTimeState,
                onValueChanged = {
                    viewModel.onEventTimeChanged(it)
                },
                keyboard = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(10.dp))

            EventTextField(
                label = "Address",
                value = viewModel.eventAddressState,
                onValueChanged = {
                    viewModel.onEventAddressChanged(it)
                },
                keyboard = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(10.dp))

            EventTextField(
                label = "Link",
                value = viewModel.eventLinkState,
                onValueChanged = {
                    viewModel.onEventLinkChanged(it)
                },
                keyboard = KeyboardType.Uri
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick={
                    if(viewModel.eventTitleState.isNotEmpty() &&
                        viewModel.eventAddressState.isNotEmpty() &&
                        viewModel.isLink(viewModel.eventLinkState) &&
                        viewModel.validateDate(viewModel.eventDateState) &&
                        viewModel.validateTime(viewModel.eventTimeState)
                        ) {
                        if(id != 0L) {
                            viewModel.updateEvent(
                                Event(
                                    id = id,
                                    title = viewModel.eventTitleState.trim(),
                                    date = viewModel.eventDateState.trim(),
                                    time = viewModel.eventTimeState.trim(),
                                    address = viewModel.eventAddressState.trim(),
                                    link = viewModel.eventLinkState.trim()
                                )
                            )
                        } else {
                            viewModel.addEvent(
                                Event(
                                    title = viewModel.eventTitleState.trim(),
                                    date = viewModel.eventDateState.trim(),
                                    time = viewModel.eventTimeState.trim(),
                                    address = viewModel.eventAddressState.trim(),
                                    link = viewModel.eventLinkState.trim()
                                )
                            )
                        }
                        scope.launch {
                            navController.navigateUp()
                        }
                    } else {
                        check = true
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary)
            ){
                Text(
                    text = if (id != 0L) stringResource(id = R.string.update_event)
                            else stringResource(id = R.string.add_event),
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White
                    )
                )
            }
            if(check) {
                ShowToast()
                check = false
            }
        }
    }
}

@Composable
fun EventTextField(
    label: String,
    value: String,
    keyboard: KeyboardType,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label, color = Color.Black) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun ShowToast() {
    val context = LocalContext.current
    Toast.makeText(context, "Input the valid data", Toast.LENGTH_SHORT).show()
}