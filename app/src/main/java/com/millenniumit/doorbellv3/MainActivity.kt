package com.millenniumit.doorbellv3

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.millenniumit.doorbellv3.ui.theme.Doorbellv3Theme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this.applicationContext
        setContent {
            Doorbellv3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    DoorbellApp(viewModel = BellViewModel(context))
                }
            }
        }
    }
}

@Composable
fun DoorbellApp(modifier: Modifier = Modifier, viewModel: BellViewModel ) {

    val showDialog =  remember { mutableStateOf(true) }
    var mainStatusButtonColor : Color = MaterialTheme.colors.error


    if(showDialog.value)
        CustomDialog(value = "", setShowDialog = {showDialog.value = it}, setValue = {},viewModel=viewModel)
    if (viewModel.mqttConnected)
    {
        mainStatusButtonColor= Color.Green
    } else {
        mainStatusButtonColor= MaterialTheme.colors.error
    }



    Scaffold(modifier = modifier, topBar = {
        DoorbellTopBar()
    }, floatingActionButton = {FloatingActionButton(backgroundColor = mainStatusButtonColor, onClick = {
            showDialog.value = true
    }) {
        Text("")
    }}, floatingActionButtonPosition = FabPosition.Center
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            Image(
                painter = painterResource(id = R.drawable.bluedoorbell),
                contentDescription = "Blue Doorbell",
                modifier = Modifier.fillMaxWidth(),

                )
            Text(
                text = viewModel.mqqtMessage
            )
        }

    }
}

@Composable
fun DoorbellTopBar() {
    val contextForToast = LocalContext.current.applicationContext
    TopAppBar(
        title = {
            Text(text = "DoorBell")
        },
        navigationIcon = {
            IconButton(onClick = {
                Toast.makeText(contextForToast, "Navigation Icon Click", Toast.LENGTH_SHORT)
                    .show()
            }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Navigation icon")
            }
        },
        actions = {
            // search icon
            TopAppBarActionButton(
                imageVector = Icons.Outlined.Search,
                description = "Search"
            ) {
                Toast.makeText(contextForToast, "Search Click", Toast.LENGTH_SHORT)
                    .show()
            }

            // lock icon
            TopAppBarActionButton(
                imageVector = Icons.Outlined.Lock,
                description = "Lock"
            ) {
                Toast.makeText(contextForToast, "Lock Click", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    )
}

@Composable
fun TopAppBarActionButton(
    imageVector: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(imageVector = imageVector, contentDescription = description)
    }
}

