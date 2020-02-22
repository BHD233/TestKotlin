package com.bhd.testkotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class CreateNewUser2: AppCompatActivity(){

    var employeeInfor = EmployeeInfor()
    val listValue: MutableList<EditText> = arrayListOf()
    val listTitle: MutableList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_new_user_page1)

        val where = intent.getIntExtra("Where", 0)

        if (where == 0) {
            getInterface()
        } else if (where == 2){
            readEmployee()
            getInterface()

            //get all text back
            for (i in 0..employeeInfor.detailInfors.size - 1){
                listValue[i].setText(employeeInfor.detailInfors[i].inforDetail)
            }
        } else {
            readEmployee()

            val intent = Intent(this, CreateNewUser3::class.java)

            intent.putExtra("Where", where)

            startActivityForResult(intent,0)
        }

        //reset title
        val title = findViewById<TextView>(R.id.title)
        title.text = "Create New User #2"
    }

    fun getInterface(){
        val layout = findViewById<LinearLayout>(R.id.mainLayout)

        //hide image
        val imageView = findViewById<ImageView>(R.id.createAvatarImage)
        imageView.isVisible = false

        //get all atribute and show it up
        for(i in (AllAtributeOfDetailInfor.size / 3 + 1 .. AllAtributeOfDetailInfor.size/3 * 2)){
            val curLayout = LinearLayout(this)
            curLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            curLayout.orientation = LinearLayout.VERTICAL

            val titleTextView = TextView(this)
            titleTextView.text = AllAtributeOfDetailInfor[i].capitalize() + ":"
            titleTextView.setTextColor(Color.BLACK)
            titleTextView.setTypeface(null, Typeface.BOLD)

            val valueTextView = EditText(this)
            valueTextView.setBackgroundResource(R.drawable.round_corner_background_text)
            valueTextView.setPadding(40,20,40,20)

            listTitle.add(titleTextView.text.toString())
            listValue.add(valueTextView)

            curLayout.addView(titleTextView)
            curLayout.addView(valueTextView)

            val param = curLayout.layoutParams as LinearLayout.LayoutParams
            param.setMargins(0,20,0,20)
            curLayout.layoutParams = param

            layout.addView(curLayout)
        }

        //show next button
        val button = Button(this)
        button.setBackgroundResource(R.drawable.round_corner_background)
        button.text = "Next"
        button.setOnClickListener(View.OnClickListener {
            onNextButtonClicked(it)
        })

        layout.addView(button)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0){
            val result: EmployeeInfor = (data as Intent).getParcelableExtra("data")

            getData()

            //if cannot get data => another activities
            if (employeeInfor.detailInfors.size == 0){
                readEmployee()
            }

            //merge data
            result.detailInfors.forEach(){
                it->
                employeeInfor.detailInfors.add(it)
            }

            setResult(RESULT_OK, intent.putExtra("data", employeeInfor))
            finish()
        }
    }

    fun onNextButtonClicked(view: View) {
        var pass = true

        listValue.forEach() { it ->
            if (it.text.toString() == "") {
                pass = false
            }
        }

        if (pass) {
            val intent = Intent(this, CreateNewUser3::class.java)

            startActivityForResult(intent, 0)
        }
    }

    fun getData(){
        //reset employee
        employeeInfor = EmployeeInfor()

        for (i in 0..listTitle.size - 1){
            if (listValue[i].text.toString() != ""){
                employeeInfor.detailInfors.add(DetailInfor(listTitle[i], listValue[i].text.toString()))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

        // get data to save all to file
        getData()

        //if cannot get data => another activities
        if (employeeInfor.detailInfors.size == 0){
            readEmployee()
        }

        //save all to file
        val path = Environment.getExternalStorageDirectory().toString()

        // Create a file to save the image

        val file = File(path, "temp2.txt")

        file.writeText("")

        for (j in (0..employeeInfor.detailInfors.size - 1)) {
            file.appendText(employeeInfor.detailInfors[j].inforTitle + "\"" + employeeInfor.detailInfors[j].inforDetail + "\"")
        }

        super.onSaveInstanceState(outState)
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        readEmployee()

        for (i in 0..employeeInfor.detailInfors.size - 1){
            listValue[i].setText(employeeInfor.detailInfors[i].inforDetail)
        }
    }

    fun readEmployee() {
        //clear current employee
        employeeInfor = EmployeeInfor()

        val path = Environment.getExternalStorageDirectory().toString()

        val file = File(path, "temp2.txt")

        val text = file.readText()

        val token = tokenize(text)

        val detailInfors: MutableList<DetailInfor> = arrayListOf()

        for (j in 0..token.size - 2 step 2) {
            detailInfors.add((DetailInfor(token[j], token[j + 1])))
        }

        val generalInfor = GeneralInfor()

        employeeInfor = EmployeeInfor(generalInfor, detailInfors as ArrayList<DetailInfor>)
    }
}