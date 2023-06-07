package com.example.projectomoviles.calendario

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.projectomoviles.R
import java.time.LocalTime

class hour_adapter : ArrayAdapter<hour_event> {

    constructor(context: Context, hourEvents: List<hour_event?>?) : super(context, 0, hourEvents!!) {


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val event: hour_event? = getItem(position)
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.hour_cell, parent, false)
        }
        setHour(convertView!!, event!!.time)
        setEvents(convertView, event.events)

        return convertView!!


    }

    override fun getItem(position: Int): hour_event? {
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getCount(): Int {
        return super.getCount()
    }



    companion object {
        fun setEvents(convertView: View, events: ArrayList<Event>) {
            val event1: TextView = convertView.findViewById(R.id.event1)
            val event2: TextView = convertView.findViewById(R.id.event2)
            val event3: TextView = convertView.findViewById(R.id.event3)
            val event4: TextView = convertView.findViewById(R.id.event4)
            val event5: TextView = convertView.findViewById(R.id.event5)

            val eventViews = listOf(event1, event2, event3, event4, event5)

            for (i in eventViews.indices) {
                val eventView = eventViews[i]
                if (i < events.size) {
                    eventView.text = events[i].name
                    eventView.visibility = View.VISIBLE
                } else {
                    eventView.visibility = View.INVISIBLE
                }
            }
        }



        @RequiresApi(Build.VERSION_CODES.O)
        fun setHour(convertView: View, time: LocalTime) {
            var timeTV: TextView = convertView.findViewById(R.id.timeTV)
            timeTV.text = calendario_utils.formattedShortTime(time)

        }

    }

}