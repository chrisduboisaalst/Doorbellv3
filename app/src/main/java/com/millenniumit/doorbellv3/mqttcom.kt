package com.millenniumit.doorbellv3

import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import com.millenniumit.doorbellv3.MainActivity

class mqttcom(serverURI:String,context: Context): Service() {
    private val clientId: String = MqttClient.generateClientId()
    private var token: IMqttToken? = null
    private var serverURI = serverURI
    private var client:MqttAndroidClient? = null
    private var context = context
    private var updatefunction = {a:String-> Unit}
    private var waitingforConnection: Boolean=false


    var mqttConnected : Boolean = false
    var mqttSubscribed : Boolean = false
    var mqttHeartBeatTopic : String = "doorBellHeartBeat"
    var mqttBellRangTopic : String = "doorBellRang"


    private val defaultCbClient = object : MqttCallback {
        override fun messageArrived(topic: String?, message: MqttMessage?) {
            Log.d(this.javaClass.name, "Receive message: ${message.toString()} from topic: $topic")

            updatefunction(message.toString())

        }

        override fun connectionLost(cause: Throwable?) {
            Log.d(this.javaClass.name, "Connection lost ${cause.toString()}")
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.d(this.javaClass.name, "Delivery completed")
        }
    }

    private val defaultCbSubscribe = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d(this.javaClass.name, "Subscribed to topic")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d(this.javaClass.name, "Failed to subscribe topic")
        }
    }

    fun setUpdateFunction(setupdatefunction: (String)-> Unit){
        updatefunction=setupdatefunction
    }

    fun connect() {
        connect(context)
    }

    fun connect(context: Context) {

        val tempclient = MqttAndroidClient(
            context, serverURI,
            clientId
        )
        tempclient.setCallback(defaultCbClient)
        try {
            val temptoken = tempclient.connect()
            temptoken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // We are connected
                    Log.d(ContentValues.TAG, "MQTTConnected in connection class")
                    token=temptoken
                    client=tempclient
                    mqttConnected=true
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(ContentValues.TAG,exception.toString())
                    mqttConnected=false

                }

            }
        } catch (e: MqttException) {
            e.printStackTrace()
            mqttConnected=false

        }
    }

    fun subscribe() {
        val topic = mqttHeartBeatTopic
        val qos = 1
        if (client==null) {
            Log.d(ContentValues.TAG, "MQTTClient is null")
        }
         try {
            if (client!=null) {
                val subToken: IMqttToken = client!!.subscribe(topic, qos)
                subToken.actionCallback =defaultCbSubscribe
                Log.d(ContentValues.TAG, "MQTTSubscribed in connection class")
                mqttSubscribed = true
            }
        } catch (e: MqttException) {
            e.printStackTrace()
            mqttSubscribed = false
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }
}