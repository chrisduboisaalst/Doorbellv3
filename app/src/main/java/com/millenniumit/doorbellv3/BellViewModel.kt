package com.millenniumit.doorbellv3

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.millenniumit.doorbellv3.MainActivity
import com.millenniumit.doorbellv3.mqttcom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.MqttCallback
import kotlin.concurrent.thread

class BellViewModel(context: Context):ViewModel(){

    var mqqtMessage by mutableStateOf("No message")
    var serverURI by mutableStateOf( "tcp://10.0.0.196:1883")
    var mqttConnected by mutableStateOf(false)
    var mqttSubscribed by mutableStateOf(false)
    var mqttHeartBeatTopic by mutableStateOf("")
    var mqttBellRangTopic by mutableStateOf("")
    val mqttClient = mqttcom(serverURI,context)

    init {
        viewModelScope.launch {
            // Coroutine that will be canceled when the ViewModel is cleared.
            connect()
        }

    }

    fun connect() {
        Log.d(this.javaClass.name, "Launch Connect to MQTT in init viewmodel")
        mqttClient.connect()
        thread {
            while (!mqttClient.mqttConnected) {}
            mqttClient.subscribe()
            while (!mqttClient.mqttSubscribed) {}
            mqttConnected = mqttClient.mqttConnected
            mqttSubscribed = mqttClient.mqttSubscribed
            setCallBackFunction()
            Log.d(this.javaClass.name,"mqttConnected :$mqttConnected, mqttSubscribed :$mqttSubscribed")
        }
        mqttHeartBeatTopic =mqttClient.mqttHeartBeatTopic
        mqttBellRangTopic = mqttClient.mqttBellRangTopic


    }

    fun setCallBackFunction() {

        mqttClient.setUpdateFunction(setupdatefunction={mqqtMessage=it})
    }

}
