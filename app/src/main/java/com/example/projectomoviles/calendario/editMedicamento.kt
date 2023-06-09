package com.example.projectomoviles.calendario

import com.example.projectomoviles.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import com.example.projectomoviles.MyDatabaseHelper
import com.example.projectomoviles.util.Common
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class editMedicamento : AppCompatActivity() {
    lateinit var selected: ArrayList<Event>
    lateinit var adapter: event_adapter
    lateinit var selectedTask: Event
    lateinit var eventTimeTV: Button
    lateinit var eventPresent: Spinner
    lateinit var eventViaAdmin: Spinner
    lateinit var medioSeleccionado: String
    lateinit var medioSeleccionado2: String
    var hour:Int = 0
    var minut:Int = 0
    var vAdmin= arrayOf("Oral","Tópica","Parenteral","Inhalatoria")
    var present= arrayOf("Inyectable subcutáneo","Intramuscular")

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_medicamento)

        title="Editar Medicamentos"
        val btnB: ActionBar? = supportActionBar
        btnB?.setHomeAsUpIndicator(R.drawable.baseline_edit_note_24)
        btnB?.setDisplayHomeAsUpEnabled(true)

        //Obtiene el objeto enviado desde la vista de calendario y lo mete en el adapter del evento
        selected = intent.getSerializableExtra("selectedItem") as ArrayList<Event>
        val listView: ListView = findViewById(R.id.tvTareas)
        adapter = event_adapter(this,selected)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            selectedTask = selected[position]
            showTaskOptionsDialog(selectedTask, position)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTaskOptionsDialog(tarea: Event, position: Int) {
        val options = arrayOf("Editar", "Eliminar")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Opciones")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> showEditTaskDialog(tarea, position)
                1 -> eliminarTarea(position)
            }
        }
        builder.create().show()
    }

    fun popTimePicker(view: View){
        var onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
            hour = i
            minut = i2
            eventTimeTV .setText(String.format(Locale.getDefault(),"%02d:%02d",i,i2))

        }
        var timePickerDialog: TimePickerDialog = TimePickerDialog(this,onTimeSetListener,hour, minut,true)
        timePickerDialog.setTitle("Eliga una hora de aviso")
        timePickerDialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEditTaskDialog(tarea: Event, position: Int) {
        val builder = AlertDialog.Builder(this)
        val inputLayout = LayoutInflater.from(this).inflate(R.layout.event_id, null)
        val tv = inputLayout.findViewById<TextView>(R.id.tvMedicamento)
        val boton: Button = inputLayout.findViewById(R.id.btnEditSave)
        tv.setText("Editar Medicamento")
        boton.visibility = View.INVISIBLE

        eventTimeTV = inputLayout.findViewById(R.id.btnHourPicker)
        val nameMedi = inputLayout.findViewById<EditText>(R.id.etNomMedi)
        val nameInput = inputLayout.findViewById<EditText>(R.id.etNomComer)
        val viaAdmin = inputLayout.findViewById<Spinner>(R.id.spinViaAdmi)
        val presentacion = inputLayout.findViewById<Spinner>(R.id.spinPresent)
        val mili = inputLayout.findViewById<EditText>(R.id.etCantMg)
        val fInicio = inputLayout.findViewById<EditText>(R.id.etFechaInicio)
        val fFinal = inputLayout.findViewById<EditText>(R.id.etFechaFinal)
        val frec = inputLayout.findViewById<EditText>(R.id.spinTomarlo)


        builder.setPositiveButton("Guardar") { _, _ ->0

            // Actualizar los datos de la tarea
            if(selected !=null){
                var sqLiteManager: MyDatabaseHelper = MyDatabaseHelper(this)
                selected[position].id  = selected[position].id
                selected[position].name = nameInput.text.toString()
                selected[position].nMedi = nameMedi.text.toString()
                selected[position].viaAdmin = medioSeleccionado
                selected[position].present = medioSeleccionado2
                selected[position].mili = mili.text.toString()
                selected[position].frecToma = frec.text.toString()
                selected[position].time = LocalTime.parse(eventTimeTV.text.toString())
                selected[position].date = LocalDate.parse(fInicio.text.toString())
                selected[position].dateFinish = LocalDate.parse(fFinal.text.toString())
                var result = sqLiteManager.updateCalendarInDB(selected[position])
                selected.set(position,selected[position])
                if(result){
                    Common.showToastMessage(this,"Medicamento actualizado correctamente")

                }else{
                    Common.showToastMessage(this,"Ha ocurrido un error al intentar actualizar el medicamento.")
                }

                var intent = Intent()
                intent = intent.putExtra("objUpdate",selected)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }

        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        //Se agregan los inputs con los valores traidos de la bd

        onClickSpinners(inputLayout)
        nameInput.setText(selectedTask.name)
        nameMedi.setText(selectedTask.nMedi)
        mili.setText(selectedTask.mili)
        eventTimeTV.text = selectedTask.time.toString()
        fInicio.setText(selectedTask.date.toString())
        fFinal.setText(selectedTask.dateFinish.toString())
        frec.setText(selectedTask.frecToma)
        initWidgets(inputLayout)

        lateinit var mselect: String
        for(i in vAdmin.indices){
            if(vAdmin[i] == selectedTask.viaAdmin){
                mselect = vAdmin[i]
                viaAdmin.setSelection(i)
                break
            }
        }

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.background_dark)
        dialog.window?.setDimAmount(0.7f)
        dialog.window?.setWindowAnimations(R.style.DialogStyle)
        dialog.setView(inputLayout)

        dialog.show()
    }


    private fun eliminarTarea(position: Int) {
        var intent = Intent()
        intent = intent.putExtra("objDelete",selected[position].id)
        setResult(2,intent)
        var sqLiteManager: MyDatabaseHelper = MyDatabaseHelper(this)
        var result = sqLiteManager.deleteEventInDB(selected[position].id)
        if(result){
            Common.showToastMessage(this,"Medicamento eliminado correctamente")

        }else{
            Common.showToastMessage(this,"Ha ocurrido un error al intentar eliminar el medicamento.")
        }

        finish()
    }
    private fun initWidgets(inputLayout: View) {
        eventPresent = inputLayout.findViewById(R.id.spinPresent)
        eventViaAdmin = inputLayout.findViewById(R.id.spinViaAdmi)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClickSpinners(inputLayout: View){
        initWidgets(inputLayout)
        val adaptAdmin = ArrayAdapter(this, android.R.layout.simple_spinner_item, vAdmin)
        adaptAdmin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eventViaAdmin.adapter=adaptAdmin

        eventViaAdmin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                medioSeleccionado = parent.getItemAtPosition(position).toString()
                present = when(medioSeleccionado){
                    "Oral"-> arrayOf("Comprimido","Capsula","Jarabe","Suspensiones")
                    "Topica"-> arrayOf("Pomada","Gel","Parches","Gotas Nasales","Gotas óticas")
                    "Parenteral"->arrayOf("Inyectable subcutáneo","Intramuscular")
                    "Inhalatoria"->arrayOf("Aerosol","Nebulizador")
                    else -> arrayOf("")
                }
                adapterVAdmin()

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                //no hace falta implementar
            }
        }
        eventPresent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                medioSeleccionado2 = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                //no hace falta implementar
            }
        }
    }



    fun adapterVAdmin(){
        //Crea el array de los tipos de presentacion a seleccionar y lo adapta al spinner
        val adapt = ArrayAdapter(this, android.R.layout.simple_spinner_item, present)
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eventPresent.adapter=adapt
    }

}