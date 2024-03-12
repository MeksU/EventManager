package pl.meksu.eventmanager.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pl.meksu.eventmanager.R

@Composable
fun AppBarView(
    title: String,
    onBackNavClicked: () -> Unit= {}
) {
    val navigationIcon : (@Composable () -> Unit) =
    {
        IconButton(onClick = { onBackNavClicked() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint = Color.White,
                contentDescription = "Back button"
            )
        }
    }
    TopAppBar(
        title = {
            Text(
                text = title,
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = MaterialTheme.colorScheme.primary,
        elevation = 3.dp,
        navigationIcon = navigationIcon
    )
}

@Composable
fun AppBarMainView(
    title: String,
    myIcon: ImageVector,
    onBackNavClicked: () -> Unit = {},
    onIconClicked: () -> Unit = {},
    sortIcon: @Composable (() -> Unit)? = null
) {
    var showDialog by remember { mutableStateOf(false) }

    val navigationIcon : (@Composable () -> Unit) = {
        IconButton(onClick = { showDialog = true }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                tint = Color.White,
                contentDescription = "Log out button"
            )
        }
        if (showDialog) {
            ConfirmationDialog(
                onConfirm = {
                    onBackNavClicked()
                    showDialog = false
                },
                onDismiss = {
                    showDialog = false
                },
                yes = "Log out",
                no = "Cancel",
                title = "Log out",
                text = "Are you sure you want to log out?"
            )
        }
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp),
                fontWeight = FontWeight.Bold
            )
        },
        backgroundColor = MaterialTheme.colorScheme.primary,
        elevation = 3.dp,
        navigationIcon = navigationIcon,
        actions = {
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onIconClicked() }) {
                    Icon(
                        imageVector = myIcon,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
                sortIcon?.invoke()
            }
        }
    )
}

@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    yes: String,
    no: String,
    title: String,
    text: String
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onConfirm()
            }) {
                Text(yes)
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(no)
            }
        },
        title = { Text(title) },
        text = {
            Text(text)
        }
    )
}