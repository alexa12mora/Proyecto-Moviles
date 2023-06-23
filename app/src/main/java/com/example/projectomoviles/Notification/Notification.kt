package com.example.projectomoviles.Notification

import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.database.Cursor
import android.graphics.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import es.dmoral.toasty.Toasty
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.projectomoviles.Base_Activity
import com.example.projectomoviles.MyDatabaseHelper
import com.example.projectomoviles.R
import com.example.projectomoviles.sessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class Notification : Base_Activity() {
    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var listViewAlarms: ListView
    private lateinit var alarmReceiver: BroadcastReceiver
    private lateinit var alarmIntent: PendingIntent
    private lateinit var alarmManager: AlarmManager
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notificacion)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        // Inicializar DatabaseHelper
        dbHelper = MyDatabaseHelper(this)



        // Configurar receptor de difusión (BroadcastReceiver)
        alarmReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // Mostrar notificación
                showNotification("Recordatorio", "Es hora de tomar su medicamento")
            }
        }
        registerReceiver(alarmReceiver, IntentFilter(ALARM_ACTION))

        // Obtener instancia de AlarmManager
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Crear intent para el receptor de difusión
        val intent = Intent(ALARM_ACTION)
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Configurar ListView
        listViewAlarms = findViewById(R.id.listView)

        loadAlarms()

        // Configurar botón de programar alarma
        val btnScheduleAlarm = findViewById<Button>(R.id.buttonSetAlarm)
        btnScheduleAlarm.setOnClickListener {
            showDateTimePickerDialog()
        }
        // Configurar ListView item con click listener
        listViewAlarms.setOnItemClickListener { _, view, position, _ ->
            val cursor = listViewAlarms.adapter.getItem(position) as Cursor
            val alarmId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val time = cursor.getString(cursor.getColumnIndexOrThrow("time"))
            showTaskOptionsDialog(alarmId, title, time)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTaskOptionsDialog(alarmId: Long, currentTitle: String, currentTime: String) {
        val options = arrayOf("Editar", "Eliminar")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Opciones")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> showEditAlarmDialog(alarmId, currentTitle, currentTime)
                1 -> eliminarTarea(alarmId)
            }
        }
        builder.create().show()
    }

    private fun eliminarTarea(id: Long) {
        dbHelper.deleteAlarm(id)
        Toasty.info(this, "Alarma eliminada correctamente.", Toast.LENGTH_LONG, true).show()
        loadAlarms()
    }

    private fun loadAlarms() {
        lateinit var sessionManager: sessionManager
        var userId: Int
        sessionManager = sessionManager(this)
        userId = sessionManager.getUserId()

        // Crear un ViewBinder personalizado
        val viewBinder = SimpleCursorAdapter.ViewBinder { view, cursor, columnIndex ->
            // Obtener el TextView del elemento de la vista
            val textView = view as? TextView
            if (textView != null) {
                // Obtener el valor del texto del cursor
                val text = cursor.getString(columnIndex)

                // Cambiar el color de texto del TextView
                textView.setTextColor(Color.WHITE) // Cambia el color aquí según tus necesidades

                // Asignar el texto al TextView
                textView.text = text

                // Indicar que el enlace se ha realizado correctamente
                return@ViewBinder true
            }

            // Si no es un TextView, se indica que el enlace no se ha realizado correctamente
            return@ViewBinder false
        }

        val cursor = dbHelper.getAllAlarms(userId)
        val adapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_2,
            cursor,
            arrayOf("title", "time"),
            intArrayOf(android.R.id.text1, android.R.id.text2),
            0
        )
        // Iterar sobre el cursor y programar las alarmas
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date1: Date = format.parse(cursor.getString(cursor.getColumnIndexOrThrow("time")))
                val currentDateTime = Calendar.getInstance().time


                val alarmId =
                    cursor.getLong(cursor.getColumnIndexOrThrow("_id"))
                val time =
                    cursor.getString(cursor.getColumnIndexOrThrow("time"))

                // Programar la alarma
                if(currentDateTime <= date1){
                    scheduleAlarmFromDatabase(alarmId, time)
                }

            } while (cursor.moveToNext())
        }
        adapter.viewBinder = viewBinder
        listViewAlarms.adapter = adapter
    }

    private fun scheduleAlarmFromDatabase(alarmId: Long, time: String) {
        // Obtener el tiempo de la alarma en formato Calendar o Date (dependiendo de tu implementación)
        // Por ejemplo, si el valor de `time` está en formato "dd/MM/yyyy HH:mm":
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val alarmTime = Calendar.getInstance()
        try {
            alarmTime.time = sdf.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        // Programar la alarma en el AlarmManager
        if (alarmTime != null) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarmTime.timeInMillis,
                alarmIntent
            )
        }
    }

    private fun showDateTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val dateTimePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        // Obtener la hora y fecha seleccionadas por el usuario
                        val alarmTime = Calendar.getInstance()
                        alarmTime.set(year, month, day, hourOfDay, minute)
                        scheduleAlarm(alarmTime)
                    },
                    currentHour,
                    currentMinute,
                    false
                )
                timePickerDialog.show()
            },
            currentYear,
            currentMonth,
            currentDay
        )
        dateTimePickerDialog.show()
    }


    private fun showEditAlarmDialog(alarmId: Long, currentTitle: String, currentTime: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_alarm, null)
        dialogBuilder.setView(dialogView)

        val editTextTitle = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val editTextTime = dialogView.findViewById<EditText>(R.id.editTextTime)
        editTextTitle.setText(currentTitle)
        editTextTime.setText(currentTime)

        val buttonDateTime = dialogView.findViewById<Button>(R.id.buttonDateTime)
        buttonDateTime.setOnClickListener {
            showDateTimePickerDialog(editTextTime)
        }

        dialogBuilder.setTitle("Editar alarma")
            .setPositiveButton("Guardar") { dialog, _ ->
                val newTitle = editTextTitle.text.toString()
                val newTime = editTextTime.text.toString()

                dbHelper.updateAlarm(alarmId.toInt(), newTitle, newTime)
                scheduleUpdatedAlarm(alarmId.toInt(), newTitle, newTime)
                loadAlarms()
                listViewAlarms.deferNotifyDataSetChanged()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun scheduleUpdatedAlarm(alarmId: Int, title: String, time: String) {
        // Cancelar la alarma existente
        cancelAlarm(alarmId.toLong())
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val alarmTime = sdf.parse(time)
        alarmTime?.let {
            val calendar = Calendar.getInstance()
            calendar.time = alarmTime
            updateScheduleAlarm(calendar,alarmId, title)
        }
    }

    private fun cancelAlarm(alarmId: Long) {
        val intent = Intent(ALARM_ACTION)
        val pendingIntent = PendingIntent.getBroadcast(this, alarmId.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun showDateTimePickerDialog(editTextTime: EditText) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                showTimePickerDialog(calendar, editTextTime)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun showTimePickerDialog(calendar: Calendar, editTextTime: EditText) {
        val timePickerDialog = TimePickerDialog(
            this,
            { _: TimePicker?, hourOfDay: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                updateDateTimeField(calendar, editTextTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )

        timePickerDialog.show()
    }

    private fun updateDateTimeField(calendar: Calendar, editTextTime: EditText) {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateTimeString = sdf.format(calendar.time)
        editTextTime.setText(dateTimeString)
    }

    private fun updateScheduleAlarm(alarmTime: Calendar, id: Int, title: String) {
        // Programar la alarma
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            alarmIntent
        )
        // Guardar la alarma en la base de datos
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val alarmTimeString = sdf.format(alarmTime.time)
        val title = "Alarma programada"
        val time = alarmTimeString
        dbHelper.updateAlarm(id, title, time)
        Toasty.info(this, "Alarma actualizada correctamente!", Toast.LENGTH_LONG, true).show()
        // Actualizar ListView
        loadAlarms()
    }
    private fun scheduleAlarm(alarmTime: Calendar) {
        lateinit var sessionManager: sessionManager
        var userId: Int
        sessionManager = sessionManager(this)
        userId = sessionManager.getUserId()

        // Programar la alarma
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            alarmIntent
        )
        // Guardar la alarma en la base de datos
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val alarmTimeString = sdf.format(alarmTime.time)
        val title = "Alarma programada"
        val time = alarmTimeString
        dbHelper.insertAlarm(title, time, userId)
        Toasty.info(this, "Alarma guardada correctamente", Toast.LENGTH_LONG, true).show()
        // Actualizar ListView
        loadAlarms()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, content: String) {
        val channelId = "alarm_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Crear canal de notificación (solo es necesario una vez)
        val channel = NotificationChannel(
            channelId,
            "Alarm Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(1, notificationBuilder.build())
    }

    companion object {
        private const val ALARM_ACTION = "com.example.alarma.ALARM_ACTION"
    }
}


