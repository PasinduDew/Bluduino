//package com.pasindudew.bluduino
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.TextView
//
//class DeviceAdapter(private val context: Context, private val dataSource: ArrayList<Any>): BaseAdapter() {
//
//    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//    //1
//    override fun getCount(): Int {
//        return dataSource.size
//    }
//
//    //2
//    override fun getItem(position: Int): Any {
//        return dataSource[position]
//    }
//
//    //3
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    //4
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        // Get view for row item
//        val rowView = inflater.inflate(R.layout.list_item_device, parent, false)
//
//        // Get title element
//        val titleTextView = rowView.findViewById(R.id.tvDeviceName) as TextView
//
//        // Get subtitle element
//        val subtitleTextView = rowView.findViewById(R.id.tvDeviceAddress) as TextView
//
//        // 1
//        val recipe = getItem(position) as Device
//
//// 2
//        titleTextView.text = recipe.title
//        subtitleTextView.text = recipe.description
//
//
//
//
//        return rowView
//    }
//
//}