package com.example.projectomoviles.calendario

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import com.example.projectomoviles.MyDatabaseHelper
import com.example.projectomoviles.R
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*
import com.example.projectomoviles.calendario.calendario_utils.Companion.selectedDate
import kotlin.collections.ArrayList

class calendario : AppCompatActivity() {
    lateinit var dbHelper: MyDatabaseHelper
    lateinit var monthDayText: TextView
    lateinit var dayOfWeekTV: TextView
    lateinit var hourListView: ListView
    lateinit var adapter: hour_adapter
    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendario)
        dbHelper = MyDatabaseHelper(this)
        //loadFromDBToMemory()
        initWidgets()
        title="Calendario"
        val btnB: ActionBar? = supportActionBar
        btnB?.setHomeAsUpIndicator(R.drawable.baseline_calendar_month_24)
        btnB?.setDisplayHomeAsUpEnabled(true)
        val btn: ImageButton = findViewById(R.id.btnAgregar)
        setDayView()

        btn.setOnClickListener{
            val intent = Intent(this,event_id::class.java)
            startActivity(intent)

        }
        if(hourListView != null){
            hourListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedTask = hourListView.getItemAtPosition(position)
                val selectedTask2 = hourEventList()[position].events
                val intent = Intent(this,editMedicamento::class.java)
                intent.putExtra("selectedItem",selectedTask2)
                startActivityForResult(intent,1)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadFromDBToMemory() {
        dbHelper.populateCalendarListArray()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        lateinit var event: ArrayList<Event>
        lateinit var hour: hour_event
        when(resultCode){
            -1->
                if(requestCode == 1){
                    val objActualizado = data?.getSerializableExtra("objUpdate") as ArrayList<Event>
                    var hEvent = hourEventList()
                    var x = Event.eventsList
                    if(objActualizado != null){
                        for(i in x.indices){
                            for(n in objActualizado.indices){
                                if(x[i].id == objActualizado[n].id){
                                    x[i].name = objActualizado[n].name
                                    x[i].nMedi = objActualizado[n].nMedi
                                    x[i].time = objActualizado[n].time
                                    x[i].dateFinish = objActualizado[n].dateFinish
                                    x[i].date = objActualizado[n].date
                                    x[i].mili = objActualizado[n].mili
                                    x[i].viaAdmin = objActualizado[n].viaAdmin
                                    x[i].present = objActualizado[n].present
                                    break
                                }
                            }
                        }
                    }

                }
            2->
                if(requestCode == 1){
                    val objDelete = data?.getIntExtra("objDelete",0)
                    var x = Event.eventsList
                    for(i in x.indices){
                        if(x[i].id == objDelete){
                            x.removeAt(i)
                            adapter.notifyDataSetChanged()
                            break
                        }
                    }
                }
        }
    }

    private fun initWidgets() {
        monthDayText = findViewById(R.id.monthDayText)
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV)
        hourListView = findViewById(R.id.hourListView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        setDayView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDayView() {
        monthDayText.text = calendario_utils.monthDayFromDate(calendario_utils.selectedDate)?.capitalize()
        var dayOfWeek: String = calendario_utils.selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        dayOfWeekTV.text = dayOfWeek.capitalize()
        setHourAdapter()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setHourAdapter() {
        adapter = hour_adapter(applicationContext, hourEventList())
        hourListView.adapter = adapter
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun hourEventList(): ArrayList<hour_event> {
        var list: ArrayList<hour_event> =ArrayList()
        for (hour: Int in 0 until 24) {
            var time: LocalTime = LocalTime.of(hour, 0)
            val events: ArrayList<Event> = Event.eventsForDateAndTime(selectedDate, time)
            val hourEvent = hour_event(time, events)
            list.add(hourEvent)
        }
        return list
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun previousDayAction(view: View){
        selectedDate = selectedDate.minusDays(1)
        setDayView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextDayAction(view: View){
        selectedDate = selectedDate.plusDays(1)
        setDayView()
    }




}

