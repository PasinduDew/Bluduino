package com.pasindudew.bluduino

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.DataInputStream
import java.io.IOException
import java.util.*


public class LedControlActivity : AppCompatActivity() {

//    UI Components
    lateinit var btnTurnOn: Button
    lateinit var btnTurnOff: Button
    lateinit var btnDisconnect: Button

    lateinit var tvLEDstatus: TextView

    private lateinit  var progressDialog: ProgressDialog

//    Program Variables
    lateinit var btAddress: String

//    Bluetooth Realted Variables and Values
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var bluetoothSocket: BluetoothSocket
    var isBluetoothConnected: Boolean = false

    val myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    var filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)

    override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter?): Intent? {
        return super.registerReceiver(receiver, filter)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_led_control)
        Toast.makeText(applicationContext, "My ID: $myUUID", Toast.LENGTH_LONG).show()
        // Initialize the UI Components/ Bind UI Components to the Application Logic
        btnDisconnect = findViewById(R.id.btnDisconnect)
        btnTurnOff = findViewById(R.id.btnTurnOff)
        btnTurnOn = findViewById(R.id.btnTurnOn)
        tvLEDstatus = findViewById(R.id.tvLEDstatus)

        //receive the address of the bluetooth device
        val newIntent = intent
        btAddress = newIntent.getStringExtra("currDeviceBluetoothAddress")


        // To Check Whether the Current Bluetooth Device's Address is Received
        Toast.makeText(applicationContext, "BT Address: $btAddress", Toast.LENGTH_LONG).show()

        ConnectBluetooth().execute()




    }

    fun turnOnLED(view: View){
        writeToBluetooth("TON#")
        tvLEDstatus.append("LED ON \n")
    }

    fun turnOffLED(view: View){
        writeToBluetooth("TOFF#")
        tvLEDstatus.append("LED OFF \n")
    }

    fun disconnectBluetooth(view: View){
        bluetoothSocket.close();


    }

    fun readFromBluetooth(){

        val buffer = ByteArray(256) // buffer store for the stream
        var bytes: Int // bytes returned from read()
        try {
            var socketInputStream = bluetoothSocket.inputStream


            // Read from the InputStream
            bytes = socketInputStream.read(buffer)
            val readMessage = String(buffer, 0, bytes)
            // Send the obtained bytes to the UI Activity
        } catch (e: Exception) {

        }






    }

    fun writeToBluetooth(str: String){

        bluetoothSocket.outputStream.write(str.toByteArray())
        Toast.makeText(applicationContext, "TX_Bytes: ${str.toByteArray()}", Toast.LENGTH_LONG).show()
    }

    // --------------------------------------- Inner Class --------------------------------------------------
    inner class ConnectBluetooth: AsyncTask<Void, Void, Void>() {
        private var ConnectSuccess = true //if it's here, it's almost connected

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog.show(this@LedControlActivity, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        override fun doInBackground(vararg params: Void?): Void? {

            try {
                if ( !::bluetoothSocket.isInitialized || !isBluetoothConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() //get the mobile bluetooth device
                    val dispositivo: BluetoothDevice = bluetoothAdapter.getRemoteDevice(btAddress) //connects to the device's address and checks if it's available
                    bluetoothSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID) //create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    bluetoothSocket.connect() //start connection
//                    tvLEDstatus.append("Device Connected \n")
                }
            } catch (e: IOException) {
                ConnectSuccess = false //if the try failed, you can check the exception here
//                tvLEDstatus.append("Connection Failed \n")
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            if (!ConnectSuccess)
            {
                Toast.makeText(applicationContext,"Connection Failed. Is it a SPP Bluetooth? Try again.",Toast.LENGTH_LONG).show();
                tvLEDstatus.append("Unable to Connect :( \n")
                finish();
            }
            else
            {
                Toast.makeText(applicationContext,"Connected",Toast.LENGTH_LONG).show();
                tvLEDstatus.append("Device Connected :) \n")
                isBluetoothConnected = true;
            }
            progressDialog.dismiss();
        }

    }
}


