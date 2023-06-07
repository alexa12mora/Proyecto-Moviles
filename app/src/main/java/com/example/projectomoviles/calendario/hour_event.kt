package com.example.projectomoviles.calendario

import java.io.Serializable
import java.time.LocalTime

class hour_event : Serializable {

    lateinit var time: LocalTime

    lateinit var events: ArrayList<Event>

    constructor(time: LocalTime, events: ArrayList<Event>) {
        this.time = time
        this.events = events
    }

}