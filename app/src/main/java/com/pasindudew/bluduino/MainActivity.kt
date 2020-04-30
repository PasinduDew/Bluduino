package com.pasindudew.bluduino

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    lateinit var btnTurnOn: Button
    lateinit var btnTurnOff: Button
    lateinit var btnConnections: Button
    lateinit var btnMakeMeVisible: Button

    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var pairedDevices: Set<BluetoothDevice>

    lateinit var lvDevices: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI Components or Bind UI Components to the Application Logic
        btnTurnOn = findViewById(R.id.btnTrunOn) as Button
        btnMakeMeVisible = findViewById(R.id.btnMakeVisible) as Button
        lvDevices = findViewById(R.id.lvDevices) as ListView

        // Initializing Bluetooth Adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    }

    fun turnOnBluetooth(view: View){
        if (!bluetoothAdapter.isEnabled()) {
            val turnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnOn, 0)
            Toast.makeText(applicationContext, "Turned on", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "Already on", Toast.LENGTH_LONG).show()
        }
    }

    fun turnOffBluetooth(view: View){
        bluetoothAdapter.disable()
        Toast.makeText(getApplicationContext(), "Turned Off" ,Toast.LENGTH_LONG).show();
    }

    fun turnOnVisibility(view: View){
        val getVisible: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        startActivityForResult(getVisible, 0)
    }

    fun listPairedDevices(view: View){
        pairedDevices = bluetoothAdapter.bondedDevices

        var list = ArrayList<Any>()
        if(pairedDevices.isNotEmpty()){
            for (bt in pairedDevices) list.add(bt.name + " | " + bt.address)
            Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        lvDevices.adapter = adapter
        lvDevices.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                // value of item that is clicked
                val itemValue = lvDevices.getItemAtPosition(position) as String

                // Toast the values
//                Toast.makeText(applicationContext, "Position :$position\nItem Value : $itemValue", Toast.LENGTH_LONG).show()

                // Get the device MAC address, the last 17 chars in the View
                // val info = (view as TextView).text.toString()
                // Toast.makeText(applicationContext, "Info :$info", Toast.LENGTH_LONG).show()

                val address = itemValue.split(" | ")[1].trim()
                Toast.makeText(applicationContext, "address :$address", Toast.LENGTH_LONG).show()
                // Make an intent to start next activity.
                val intent = Intent(applicationContext, LedControlActivity::class.java)
                //Change the activity.
                intent.putExtra(
                    "currDeviceBluetoothAddress",
                    address
                ) //this will be received at ledControl (class) Activity

                startActivity(intent)
            }
        }

    }



    fun checkBluetoothState(){
        // Checks for the Bluetooth support and then makes sure it is turned on
        // If it isn't turned on, request to turn it on
        // List paired devices

        val REQUEST_ENABLE_BT: Int = 1
        if(bluetoothAdapter == null){
            return
        } else {
            if(bluetoothAdapter.isEnabled){
                // Do Something When BT is Enabled
                // Or You Can List Available Devices
            } else {
                //Prompt user to turn on Bluetooth

                //Prompt user to turn on Bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
        }
    }
}
