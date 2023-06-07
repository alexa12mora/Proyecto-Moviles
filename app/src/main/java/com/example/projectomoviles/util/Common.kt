package com.example.projectomoviles.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast

class Common {
    companion object {
        private const val SALT = "Baeldung"

        fun convertIntToBoolean(int: Int): Boolean
        {
            return int > 0
        }

        fun cleanTextControl(TextBoxes: List<EditText>){

            for (textBox in TextBoxes) {
                textBox.text.clear()
                textBox.error = null
            }
        }

        fun showToastMessage(context: Context, text: String){
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }

        private fun checkEmpty(control: EditText) = if(control.text.isBlank()){
            control.error = "Debe ingresar un valor"
        }
        else{
            control.error = null
        }

        fun initiateTextControl(TextBoxes: List<EditText>){
            for (textBox in TextBoxes) {
                textBox.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int
                    ) {
                        checkEmpty(textBox)
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })

                textBox.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        checkEmpty(textBox)
                    }
                }
            }
        }

        fun validateTextBoxesOnAction(TextBoxes: List<EditText>): Boolean{
            var validation = true

            for (textBox in TextBoxes) {
                if(textBox.text.isBlank() || textBox.error != null){
                    validation = false
                    textBox.error = "Debe ingresar un valor"
                }
            }

            return validation
        }




    }
}