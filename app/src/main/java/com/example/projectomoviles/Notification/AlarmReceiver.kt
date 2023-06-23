package com.example.alarma

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.projectomoviles.R

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        // Aquí puedes realizar las acciones que deseas cuando se active la alarma
        // En este ejemplo, mostraremos una notificación en el panel de notificaciones

        val notificationId = 1

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Alarma activada")
            .setContentText("¡Es hora de hacer algo!")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(notificationId, notification)
    }

    companion object {
        private const val CHANNEL_ID = "alarm_channel"
    }
}