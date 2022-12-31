package com.millenniumit.doorbellv3

import android.widget.CheckBox
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel

@Composable
fun CustomDialog(
    value: String, setShowDialog: (Boolean) -> Unit, setValue: (String) -> Unit,
    viewModel: BellViewModel
) {

    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf(value) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier

                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colors.primary)
                            .fillMaxWidth()
                            .padding(16.dp),

                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,

                    ) {
                        Text(
                            text = "MQTT Broker",
                            color = MaterialTheme.colors.onPrimary,
                            style = TextStyle(

                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold,

                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    TextBoxWithLabel(
                        label="HeartBeat :",
                        text=viewModel.mqqtMessage
                    )
                    TextBoxWithLabel(
                        label="URI :",
                        text=viewModel.serverURI
                    )
                    TextBoxWithLabel(
                        label="HeartBeat Topic :",
                        text=viewModel.mqttHeartBeatTopic,
                    )
                    TextBoxWithLabel(
                        label="HeartBeat Topic :",
                        text=viewModel.mqttBellRangTopic,
                    )

                    LabelledCheckbox(
                        text = "Connected",
                        checked = viewModel.mqttConnected,
                        enabled = false,
                        onCheckedChange = {}
                    )

                    LabelledCheckbox(
                        text = "Subscribed",
                        checked = viewModel.mqttSubscribed,
                        enabled = false,
                        onCheckedChange = {}
                    )


                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .padding(20.dp, 0.dp, 20.dp, 0.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End) {
                        Button(
                            onClick = {
                              setShowDialog(false)
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(50.dp)
                        ) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LabelledCheckbox(text: String="", checked: Boolean=false, enabled: Boolean=false,onCheckedChange: ((Boolean) -> Unit)) {
    Row( verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =Arrangement.SpaceBetween ,
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable(
                indication = rememberRipple(color = MaterialTheme.colors.primary),
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onCheckedChange(!checked) }
            )
            .requiredHeight(ButtonDefaults.MinHeight)
            ) {
        val isChecked = remember { mutableStateOf(false) }

        Text(
            text = text,
            style = TextStyle(

                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(Modifier.size(6.dp))
        Checkbox(
            checked = checked,
            onCheckedChange = { isChecked.value = it },
            enabled = enabled,
            colors = CheckboxDefaults.colors(Color.Green)
        )



    }
}

@Composable
fun TextBoxWithLabel(label: String="", text:String="") {
    Row( verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =Arrangement.SpaceBetween ,
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .requiredHeight(ButtonDefaults.MinHeight)
            .padding(4.dp))
    {

        Text(
            text = label,
            style = TextStyle(

                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.size(6.dp))
        Text(
            text = text,
            style = TextStyle(

                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
            )
        )



    }
}