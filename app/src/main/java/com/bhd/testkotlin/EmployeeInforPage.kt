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
import kotlinx.android.synthetic.main.content_main.*

class EmployeeInforPage : AppCompatActivity(){
    var employeeInfor = EmployeeInfor()

    //use to add all edit text and set it to enable
    val listEditText: MutableList<EditText> = arrayListOf()
    val listTextView: MutableList<TextView> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employee_infor_page)

        val employee = intent.getParcelableExtra<EmployeeInfor>("data")
        employeeInfor = employee

        val layout = findViewById<LinearLayout>(R.id.layout)

        employee.detailInfors.forEach(){
            data->
            val curLayout = LinearLayout(this)
            curLayout.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            curLayout.orientation = LinearLayout.HORIZONTAL

            val titleTextView = TextView(this)
            titleTextView.text = data.inforTitle + ":"

            val valueTextView = EditText(this)
            valueTextView.setText(data.inforDetail)
            valueTextView.isEnabled = false

            listEditText.add(valueTextView)
            listTextView.add(titleTextView)

            curLayout.addView(titleTextView)
            curLayout.addView(listEditText[listEditText.count() - 1])

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

    fun onEditButotnClicked(view: View) {
        val button = view as Button

        if (button.text == "Edit") {
            //enable text for edit
            listEditText.forEach() { editText ->
                editText.isEnabled = true
            }

            //change text button
            button.text = "Save"
        } else {
            //prevent edit then save data
            listEditText.forEach() { editText ->
                editText.isEnabled = false
            }
            getDataFromEditText()

            //change text button
            button.text = "Edit"
        }
    }


    fun getDataFromEditText(){
        var i = 0
        var listDetailInfor: MutableList<DetailInfor> = arrayListOf()
        while(i < listTextView.size){
            listDetailInfor.add(DetailInfor(listTextView[i].text.toString(), listEditText[i].text.toString()))
            i++
        }

        employeeInfor.detailInfors = listDetailInfor as ArrayList<DetailInfor>

        employeeInfor.generalInfor.name = listDetailInfor[0].inforDetail
    }

    fun onBackButtonClicked(view: View){
        setResult(RESULT_OK, intent.putExtra("data", employeeInfor))
        finish()
    }


}