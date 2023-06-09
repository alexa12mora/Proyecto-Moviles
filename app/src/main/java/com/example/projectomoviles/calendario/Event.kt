package com.example.projectomoviles.calendario

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

class Event : Serializable {


    fun eventsForDate(date: LocalDate): ArrayList<Event>? {
        val events: ArrayList<Event> = ArrayList()
        var eventsList: ArrayList<Event> = ArrayList()
        for (event: Event in eventsList) {
            if (event.date== date) {
                events.add(event)
            }
        }
        return events
    }
    companion object{
        var eventsList: ArrayList<Event> = ArrayList()

        @RequiresApi(Build.VERSION_CODES.O)
        fun eventsForDateAndTime(date: LocalDate, time: LocalTime): ArrayList<Event> {
            val events: ArrayList<Event> =ArrayList()

            for (event: Event in eventsList) {
                val eventHour: Int = event.time.hour
                val cellHour: Int = time.hour
                if (event.date == date && eventHour == cellHour){
                    events.add(event)
                }
            }
            return events
        }

        operator fun get(position: Int) {

        }
    }

    var id:Int
        get() {
            return field
        }
        set(value) {
            field = value
        }

    var name: String
        get() {
            return field
        }
        set(newtime) {
            field = newtime
        }
    var date: LocalDate
        get() {
            return field
        }
        set(newdate) {
            field = newdate
        }
    var time: LocalTime
        get() {
            return field
        }
        set(newtime) {
            field = newtime
        }

    var nMedi:String
        get() {
            return field
        }
        set(value) {
            field = value
        }

    var present:String
        get() {
            return field
        }
        set(value) {
            field = value
        }

    var viaAdmin:String
        get() {
            return field
        }
        set(value) {
            field = value
        }

    var mili:String
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var dateFinish: LocalDate
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var frecToma:String
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var userId:Int
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var idGroupMedi: Int
        get() {
            return field
        }
        set(value) {
            field = value
        }

    constructor(id: Int, name: String, date: LocalDate, time: LocalTime, nMedi: String, present: String, viaAdmin: String, mili: String, dateFinish: LocalDate,
                frecToma: String, userId: Int, idGroupMedi: Int) {
        this.id = id
        this.name = name
        this.date = date
        this.time = time
        this.nMedi = nMedi
        this.present = present
        this.viaAdmin = viaAdmin
        this.mili = mili
        this.dateFinish = dateFinish
        this.frecToma = frecToma
        this.userId = userId
        this.idGroupMedi = idGroupMedi
    }
}