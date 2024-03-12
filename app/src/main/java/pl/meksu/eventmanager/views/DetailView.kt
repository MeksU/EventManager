package pl.meksu.eventmanager.views

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pl.meksu.eventmanager.EventViewModel
import pl.meksu.eventmanager.data.Event

@Composable
fun DetailView(
    id: Long,
    viewModel: EventViewModel,
    navController: NavController
){
    var check: Boolean by remember {
        mutableStateOf(false)
    }
    val scaffoldState = rememberScaffoldState()
    val event = viewModel.getEventById(id).collectAsState(initial = Event(0L, "", ""))

    Scaffold(
        topBar = {
            AppBarView(
                title = "Event Detail",
                onBackNavClicked = {
                    navController.navigateUp()
                }
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = event.value.title,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000001)
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date icon")
                Text(
                    text = event.value.date,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Time icon")
                Text(
                    text = event.value.time,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Icon(imageVector = Icons.Default.Place, contentDescription = "Address icon")
                MapsLink(text = event.value.address)
            }
            Spacer(modifier = Modifier.height(10.dp))
            ButtonLink(event.value.link)
        }
    }
    if(check) {
        ShowNotificationToast()
        check = false
    }
}

@Composable
fun ShowNotificationToast() {
    val context = LocalContext.current
    Toast.makeText(context, "Notification set!", Toast.LENGTH_SHORT).show()
}

@Composable
fun MapsLink(text: String) {
    val uri = Uri.parse("geo:0,0?q=$text")

    val openMaps = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { }

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Black, textDecoration = TextDecoration.Underline)) {
            append(text)
        }
        addStringAnnotation(
            tag = "LINK",
            start = 0,
            end = text.length,
            annotation = uri.toString()
        )
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(
            fontSize = 18.sp,
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations(
                tag = "LINK",
                start = offset,
                end = offset
            ).firstOrNull()?.let { annotation ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                openMaps.launch(intent)
            }
        }
    )
}

@Composable
fun ButtonLink(url: String) {
    val openBrowser = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { }

    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            openBrowser.launch(intent)
        },
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp)
    ) {
        Text(
            text = "Go to event",
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
    }
}