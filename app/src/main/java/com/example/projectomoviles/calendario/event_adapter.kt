package com.example.projectomoviles.calendario

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.projectomoviles.R

class event_adapter : ArrayAdapter<Event> {

    constructor(context: Context, events: ArrayList<Event>): super(context,0,events){}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val event = getItem(position)
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.editmedi, parent, false)
        }
        val tvEvent: TextView = convertView!!.findViewById(R.id.txt1)
        val eventTitle:String = event!!.name
        tvEvent.text = eventTitle
        return convertView!!
    }


}