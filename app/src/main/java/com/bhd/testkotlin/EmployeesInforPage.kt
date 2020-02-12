package com.bhd.testkotlin

import android.app.ActionBar
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Layout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.util.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class EmployeesInforPage  : AppCompatActivity(){

    var chooseEmployeePos: Int = -1
    val employeeInfor: MutableList<EmployeeInfor> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employees_infor_page)

        GenerateEmployee(employeeInfor)

        ShowEmployees(employeeInfor)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        employeeInfor[chooseEmployeePos] = (data as Intent).getParcelableExtra("data")

        //reset display
        ShowEmployees(employeeInfor)
    }

    fun ShowEmployees(employees: MutableList<EmployeeInfor>){
        val layout = findViewById<LinearLayout>(R.id.employeesLayout)

        //set title
        val title = findViewById<TextView>(R.id.titleText)
        title.text = "There are " + employees.count().toString() + " employees:"

        //get name to display
        val employeesName: MutableList<String> = arrayListOf()
        employees.forEach(){
            employee->
            employeesName.add(employee.name + "\n" + employee.age.toString())
        }

        //gridView for all emplyees
        val gridview = findViewById<GridView>(R.id.gridView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, employeesName)

        gridview.adapter = adapter

        //start new activity when clicked on one employee
        gridview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, EmployeeInforPage::class.java)
            intent.putExtra("data", employees[position])

            chooseEmployeePos = position
            startActivityForResult(intent, 0)
        }

    }

    fun GenerateEmployee(employees: MutableList<EmployeeInfor>){
        employees.add(EmployeeInfor("A",21,"Nam","TPHCM"))
        employees.add(EmployeeInfor("B",23,"Nam","Dong Nai"))
        employees.add(EmployeeInfor("C",21,"Nu","Binh Duong"))
        employees.add(EmployeeInfor("D",29,"Nam","TPHCM"))
        employees.add(EmployeeInfor("E",22,"Nu","TPHCM"))
    }

    fun onViewButtonClicked(view: View){
        val button = view as Button

        button.text = button.id.toString()
    }
}