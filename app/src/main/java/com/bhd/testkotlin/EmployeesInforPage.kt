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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.IOException
import kotlin.collections.ArrayList


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

        //get name and img to display
        val employeesGeneralInfor: MutableList<GeneralInfor> = arrayListOf()
        employees.forEach(){
            employee->
            employeesGeneralInfor.add(employee.generalInfor)
        }

        //gridView for all emplyees
        val gridview = findViewById<GridView>(R.id.gridView)

        val adapter = CustomAdapter(this, employeesGeneralInfor as ArrayList<GeneralInfor>)

        gridview.adapter = adapter

        //start new activity when clicked on one employee
        gridview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, EmployeeInforPage::class.java)
            intent.putExtra("data", employees[position])

            chooseEmployeePos = position
            startActivityForResult(intent, 0)
        }

    }

    fun RandomImageSource() : String{
        val preString : String = "https://api.adorable.io/avatar/"
        val lastString : String = ".png"

        var numImg = Random().nextInt() % 1000

        return preString + numImg.toString() + numImg
    }

    fun RandomUserData(): ArrayList<DetailInfor>{
        var result : ArrayList<DetailInfor> = arrayListOf()

        var okhttp = OkHttpClient()

        val request = okhttp3.Request.Builder().url("https://api.namefake.com/").build()

        okhttp.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle this
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                // Handle this\
                val jsonString = response.body().string()

                val jsonObject = JSONObject(jsonString)

                jsonObject.keys().forEach {
                        it->
                    var detail = jsonObject.getString(it)

                    result.add(DetailInfor(it, detail))
                }
            }
        })

        //wait to get name and all thing
        while(result.size == 0){

        }

        return result
    }

    fun GenerateEmployee(employees: MutableList<EmployeeInfor>){

        for (i in (1..10)) {
            val imgSource = RandomImageSource()

            val detailInfor = RandomUserData()

            //know that name came first
            employees.add(
                EmployeeInfor(GeneralInfor(detailInfor[0].inforDetail, imgSource), detailInfor))
        }
    }

    fun onViewButtonClicked(view: View){
        val button = view as Button

        button.text = button.id.toString()
    }
}