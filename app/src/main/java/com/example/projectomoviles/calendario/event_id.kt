package com.example.projectomoviles.calendario

import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import com.example.projectomoviles.MyDatabaseHelper
import com.example.projectomoviles.R
import com.example.projectomoviles.sessionManager
import com.example.projectomoviles.util.Common
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class event_id : AppCompatActivity() {
    private lateinit var dbHelper: MyDatabaseHelper
    //Se declaran las variables
    lateinit var  eNom: EditText
    lateinit var  frecToma: EditText
    lateinit var mili: EditText
    lateinit var eventNameET: EditText
    lateinit var eventDateTV: EditText
    lateinit var eventTimeTV: TextView
    lateinit var eventPresent: Spinner
    lateinit var eventViaAdmin: Spinner
    lateinit var eventDateFTV: EditText
    lateinit var time: LocalTime
    lateinit var medioSeleccionado: String
    lateinit var medioSeleccionado2: String
    lateinit var btnpicker: Button
    var hour:Int = 0
    var minut:Int = 0
    var textBoxes = listOf<EditText>()
    var present: Array<*> = arrayOf("")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_id)
        initWidgets()
        time = LocalTime.now()
        btnBack()

        //Se crea una lista que contenga todos los edit text para validacion
        textBoxes = listOf<EditText> (eNom, mili,eventNameET,eventDateTV,eventDateFTV,frecToma)
        Common.initiateTextControl(textBoxes)

        //Se instancia la bd
        dbHelper = MyDatabaseHelper(this)

        //Funcion que asigna el array al spinner de via de administracion
        // y dependiendo de lo que seleccione el usuario crea el spinner de presentacion del medicamento
        spinnerAdmin()

        //Realiza un onclick y obtiene el valor que selecciono el usuario en el spinner de presentacion
        //del medicamento
        spinnerPresent()

        eventDateTV.setText(calendario_utils.formattedDate(calendario_utils.selectedDate))
    }


    fun spinnerAdmin(){
        val viaAdmin = arrayOf("Oral","Tópica","Parenteral","Inhalatoria")
        val adaptAdmin = ArrayAdapter(this, android.R.layout.simple_spinner_item, viaAdmin)
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
    }

    fun spinnerPresent(){
        eventPresent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                medioSeleccionado2 = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                //no hace falta implementar
            }
        }
    }


    fun popTimePicker(view: View){
        var onTimeSetListener: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
            hour = i
            minut = i2
            btnpicker.setText(String.format(Locale.getDefault(),"%02d:%02d",i,i2))

        }
        var timePickerDialog: TimePickerDialog = TimePickerDialog(this,onTimeSetListener,hour, minut,true)
        timePickerDialog.setTitle("Eliga una hora de aviso")
        timePickerDialog.show()
    }


    fun btnBack(){
        title="Añadir medicamento"
        // Obtener la instancia de la barra de acción
        val btnBack: ActionBar? = supportActionBar

        // Establece la imagen personalizada para el botón de navegación hacia atrás
        btnBack?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)

        // Habilitar el botón de navegación hacia atrás
        if (btnBack != null) {
            btnBack.setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->
                finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun adapterVAdmin(){
        //Crea el array de los tipos de presentacion a seleccionar y lo adapta al spinner
        val adapt = ArrayAdapter(this, android.R.layout.simple_spinner_item, present)
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eventPresent.adapter=adapt
    }

    private fun initWidgets() {
        eNom = findViewById(R.id.etNomMedi)
        eventNameET = findViewById(R.id.etNomComer)
        eventDateTV = findViewById(R.id.etFechaInicio)
        eventDateFTV  = findViewById(R.id.etFechaFinal)
        eventPresent = findViewById(R.id.spinPresent)
        eventViaAdmin = findViewById(R.id.spinViaAdmi)
        mili = findViewById(R.id.etCantMg)
        frecToma = findViewById(R.id.spinTomarlo)
        btnpicker = findViewById(R.id.btnHourPicker)

    }

    val random = Random()
    val generatedIds = HashSet<Int>()

    fun generateUniqueRandomId(): Int {
        var id = random.nextInt()

        // Generate a new ID until it becomes unique
        while (generatedIds.contains(id)) {
            id = random.nextInt()
        }

        // Add the generated ID to the set
        generatedIds.add(id)

        return id
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveEventAction(view: View?) {
        lateinit var sessionManager: sessionManager
        var userId: Int
        sessionManager = sessionManager(this)
        userId = sessionManager.getUserId()

        var validacion: Boolean = Common.validateTextBoxesOnAction(textBoxes)
        if (validacion){
            val eNom = eNom.text.toString()
            val eventName = eventNameET.text.toString()
            val eventFinish: String = eventDateFTV.text.toString()
            val eventStart: String = eventDateTV.text.toString()
            val frec: String = frecToma.text.toString()
            val mg = mili.text.toString()
            val viaAdmin = medioSeleccionado
            val viapresent = medioSeleccionado2
            val time = btnpicker.text.toString()
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val formatterHour: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            var fechaActual = LocalDate.parse(eventStart,formatter)
            var fechaFinal = LocalDate.parse(eventFinish,formatter)
            var hour= LocalTime.parse(time,formatterHour)

            var idGroupMedi = dbHelper.obtenerUltimoIdmMedi()+1
            while (!fechaActual.isAfter(fechaFinal)){
                var id = dbHelper.obtenerUltimoId()+1
                val newEvent = Event(id,eventName,fechaActual, hour,eNom,viapresent,viaAdmin,mg,fechaFinal,frec,userId,idGroupMedi)
                Event.eventsList.add(newEvent)

                var result = dbHelper.addMedToDatabase(newEvent)
                if(result){
                    Common.showToastMessage(this,"El medicamento ha sido agregado al calendario exitosamente");

                }else{
                    Common.showToastMessage(this, "Ha ocurrido un error agregando el medicamento")
                }
                fechaActual = fechaActual.plusDays(frec.toInt().toLong())
            }
            finish()
        }else{
            Common.showToastMessage(this, "Campos requeridos: Por favor complete los campos para poder continuar")
        }
    }
}