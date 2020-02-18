package com.bhd.testkotlin

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.bumptech.glide.Glide
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

        //set name to title
        val titleText = findViewById<TextView>(R.id.title)
        titleText.text = employee.generalInfor.name

        //get all atribute and show it up
        employee.detailInfors.forEach(){
            data->
            val curLayout = LinearLayout(this)
            curLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            curLayout.orientation = LinearLayout.VERTICAL

            val titleTextView = TextView(this)
            titleTextView.text = data.inforTitle!!.capitalize() + ":"
            titleTextView.setTextColor(Color.BLACK)
            titleTextView.setTypeface(null,Typeface.BOLD)

            val valueTextView = EditText(this)
            valueTextView.setText(data.inforDetail)
            valueTextView.isEnabled = false
            valueTextView.setBackgroundResource(R.drawable.round_corner_background_text)
            valueTextView.setPadding(40,20,40,20)

            listEditText.add(valueTextView)
            listTextView.add(titleTextView)

            curLayout.addView(titleTextView)
            curLayout.addView(listEditText[listEditText.count() - 1])

            val param = curLayout.layoutParams as LinearLayout.LayoutParams
            param.setMargins(0,20,0,20)
            curLayout.layoutParams = param

            layout.addView(curLayout)
        }
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