package com.bhd.testkotlin

import android.app.ActionBar
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Environment
import android.text.Layout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.util.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.File
import java.io.IOException
import kotlin.collections.ArrayList


class EmployeesInforPage  : AppCompatActivity(){

    var chooseEmployeePos: Int = -1
    val employeeInfor: MutableList<EmployeeInfor> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employees_infor_page)

        //GenerateEmployee(employeeInfor)
        readEmployee()

        ShowEmployees(employeeInfor)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        //edit
        if(requestCode == 0){
            employeeInfor[chooseEmployeePos] = (data as Intent).getParcelableExtra("data")

            writeToFile()

            //reset display
            ShowEmployees(employeeInfor)
        } else if (requestCode == 2){   //create
            employeeInfor.add((data as Intent).getParcelableExtra("data"))

            //reset display
            ShowEmployees(employeeInfor)
        }
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

    fun RandomUserData(i : Int): ArrayList<DetailInfor>{
        var result : ArrayList<DetailInfor> = arrayListOf()
        var key : ArrayList<String> = arrayListOf()
        var value : ArrayList<String> = arrayListOf()

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
                    if (it != "latitude" && it != "longitude") {
                        var detail = jsonObject.getString(it)

                        key.add(it.toString())
                        value.add(detail)

                        result.add(DetailInfor(it, detail))
                    }
                }
            }
        })

        //wait to get name and all thing
        while(result.size == 0){

        }
        return result
    }

    fun writeToFile(){

        val path = Environment.getExternalStorageDirectory().toString()

        // Create a file to save the image

        for (i in 0..employeeInfor.size - 1) {
            val file = File(path, i.toString() + ".txt")

            file.writeText("image\"" + employeeInfor[i].generalInfor.imgSource + "\"")

            for (j in (0..employeeInfor[i].detailInfors.size - 1)) {
                file.appendText(employeeInfor[i].detailInfors[j].inforTitle + "\"" + employeeInfor[i].detailInfors[j].inforDetail + "\"")
            }
        }
    }

    fun GenerateEmployee(employees: MutableList<EmployeeInfor>) {
        for (i in (0..10)) {
            val imgSource = RandomImageSource()

            val detailInfor = RandomUserData(i)

            //know that name came first
            employees.add(
                EmployeeInfor(GeneralInfor(detailInfor[0].inforDetail, imgSource), detailInfor)
            )
        }

        writeToFile()

        val path = Environment.getExternalStorageDirectory().toString()

        // Create a file to save the image
        val file = File(path, "numEmployees.txt")

        file.writeText(employeeInfor.size.toString())
    }

    fun tokenize(text: String): List<String>{
        val result: MutableList<String> = arrayListOf()
        var cur: String = ""

        text.forEach {
            it->
            if (it != '\"'){
                cur += it
            } else{
                result.add(cur)
                cur = ""
            }
        }

        return result as List<String>
    }

    fun readEmployee(){
        val path = Environment.getExternalStorageDirectory().toString()

        // Create a file to save the image
        var file = File(path, "numEmployees.txt")

        var numEmployees = file.readText()

        //read all employees
        for (i in 0..numEmployees.toInt() - 1){
            file = File(path, i.toString() + ".txt")

            val text = file.readText()

            val token = tokenize(text)

            val detailInfors: MutableList<DetailInfor> = arrayListOf()

            for (j in 2..token.size - 2){
                    detailInfors.add((DetailInfor(token[j],token[j + 1])))
            }

            val generalInfor = GeneralInfor(detailInfors[0].inforDetail, token[1])

            employeeInfor.add(EmployeeInfor(generalInfor, detailInfors as ArrayList<DetailInfor>))
        }
    }

    fun onAddButtonClicked(view: View){
        val intent = Intent(this, CreateNewUser1::class.java)

        startActivityForResult(intent,2)
    }
}