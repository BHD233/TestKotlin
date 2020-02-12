package com.bhd.testkotlin

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EmployeeInforPage : AppCompatActivity(){
    var employeeInfor = EmployeeInfor()

    //use to add all edit text and set it to enable
    val listValue: MutableList<EditText> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employee_infor_page)

        val employee = intent.getParcelableExtra<EmployeeInfor>("data")
        employeeInfor = employee

        val layout = findViewById<LinearLayout>(R.id.layout)

        employee.toStringToShowUp().forEach(){
            data->
            val curLayout = LinearLayout(this)
            curLayout.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            curLayout.orientation = LinearLayout.HORIZONTAL

            val titleTextView = TextView(this)
            titleTextView.text = data.first + ":"

            val valueTextView = EditText(this)
            valueTextView.setText(data.second)
            valueTextView.isEnabled = false

            listValue.add(valueTextView)

            curLayout.addView(titleTextView)
            curLayout.addView(listValue[listValue.count() - 1])

            layout.addView(curLayout)
        }

        //add butotn to edit infor
        val button = Button(this)
        button.text = "Edit"
        button.gravity = Gravity.CENTER_HORIZONTAL
        button.setOnClickListener{view ->
            onEditButotnClicked(view)
        }

        layout.addView(button)
    }

    fun onEditButotnClicked(view: View){
        val button = view as Button

        if (button.text == "Edit") {
            //enable text for edit
            listValue.forEach() { editText ->
                editText.isEnabled = true
            }

            //change text button
            button.text = "Save"
        } else{
            //prevent edit then save data
            listValue.forEach() { editText ->
                editText.isEnabled = false
            }
            getDataFromEditText()

            //change text button
            button.text = "Edit"
        }
    }

    fun getDataFromEditText(){
        employeeInfor.name = listValue[0].text.toString()
        employeeInfor.age = listValue[1].text.toString().toInt()
        employeeInfor.gender = listValue[2].text.toString()
        employeeInfor.address = listValue[3].text.toString()
    }

    fun onBackButtonClicked(view: View){
        setResult(RESULT_OK, intent.putExtra("data", employeeInfor))
        finish()
    }


}