package com.bhd.testkotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.text.Editable
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


class CreateNewUser1: AppCompatActivity(){

    var employeeInfor = EmployeeInfor()
    val listValue: MutableList<EditText> = arrayListOf()
    val listTitle: MutableList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_new_user_page1)

        val where = intent.getIntExtra("Where", 0)

        if (where == 0) { //not have temp file
            getInterface()
        } else if (where == 1){ //have temp file
            readEmployee()
            getInterface()

            //get all text back
            for (i in 0..employeeInfor.detailInfors.size - 1){
                listValue[i].setText(employeeInfor.detailInfors[i].inforDetail)
            }
        } else {
            readEmployee()

            val intent = Intent(this, CreateNewUser2::class.java)

            intent.putExtra("Where", where)

            startActivityForResult(intent,0)
        }
    }

    fun getInterface(){
        val layout = findViewById<LinearLayout>(R.id.mainLayout)

        //display image and add 2 change button
        val imageView = findViewById<ImageView>(R.id.createAvatarImage)

        //if not have image yet
        if (employeeInfor.generalInfor.imgSource == ""){
            Glide.with(this).load("https://discovery-park.co.uk/wp-content/uploads/2017/06/avatar-default.png").into(imageView)
        } else{
            Glide.with(this).load(employeeInfor.generalInfor.imgSource).into(imageView)
        }

        val chooseFromCameraButton = findViewById<Button>(R.id.chooseFromCameraButton1)
        chooseFromCameraButton.isVisible = true

        val chooseFromGallaryButton = findViewById<Button>(R.id.chooseFromGalleryButton1)
        chooseFromGallaryButton.isVisible = true

        //get all atribute and show it up
        for(i in (0..AllAtributeOfDetailInfor.size / 3)){
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

    fun onNextButtonClicked(view: View) {
        var pass = true

        listValue.forEach() { it ->
            if (it.text.toString() == "") {
                pass = false
            }
        }

        if (pass) {
            val intent = Intent(this, CreateNewUser2::class.java)

            startActivityForResult(intent, 0)
        }
    }

    fun onChooseImageFromGalleryClicked(view: View){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    fun onChooseImageFromCameraClicked(view: View){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, 1)
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

        val file = File(path, "temp1.txt")

        file.writeText("image\"" + employeeInfor.generalInfor.imgSource + "\"")

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

        val imageView = findViewById<ImageView>(R.id.createAvatarImage)

        if (employeeInfor.generalInfor.imgSource != "") {
            Glide.with(this).load(employeeInfor.generalInfor.imgSource).into(imageView)
        }

        for (i in 0..employeeInfor.detailInfors.size - 1){
            listValue[i].setText(employeeInfor.detailInfors[i].inforDetail)
        }
    }

    fun readEmployee() {
        //clear current employee
        employeeInfor.detailInfors.clear()

        val path = Environment.getExternalStorageDirectory().toString()

        val file = File(path, "temp1.txt")

        val text = file.readText()

        val token = tokenize(text)

        val detailInfors: MutableList<DetailInfor> = arrayListOf()

        for (j in 2..token.size - 2 step 2) {
            detailInfors.add((DetailInfor(token[j], token[j + 1])))
        }

        //get name
        var generalInfor = GeneralInfor()
        if (detailInfors.size > 0) {
            generalInfor = GeneralInfor(detailInfors[0].inforDetail, token[1])
        }
        //get img
        if (token.size > 0 && token[1] != ""){
            generalInfor.imgSource = token[1]
        }

        employeeInfor = EmployeeInfor(generalInfor, detailInfors as ArrayList<DetailInfor>)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageView = findViewById<ImageView>(R.id.createAvatarImage)

        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            val uri = data?.data
            imageView.setImageURI(uri)
            val path = getPath(uri)

            //save to employee
            employeeInfor.generalInfor.imgSource = path
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val uri = saveImageToExternalStorage(imageBitmap)

            imageView.setImageURI(uri)

            employeeInfor.generalInfor.imgSource = uri.toString()
        } else if (requestCode == 0){
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

            //get name to general infor
            employeeInfor.generalInfor.name = employeeInfor.detailInfors[0].inforDetail

            setResult(RESULT_OK, intent.putExtra("data", employeeInfor))
            finish()
        }
    }

    fun getPath(uri: Uri?): String{
        val str: Array<String> = arrayOf(MediaStore.Images.Media.DATA)

        val cur = contentResolver.query(uri!!, str,null,null,null)
        val colm = cur!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cur!!.moveToFirst()

        val path = cur!!.getString(colm)
        cur!!.close()

        return path
    }

    fun saveImageToExternalStorage(bitmap:Bitmap): Uri {
        // Get the external storage directory path
        val path = Environment.getExternalStorageDirectory().toString()

        // Create a file to save the image
        val file = File(path, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the output stream
            stream.flush()

            // Close the output stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image path to uri
        return Uri.parse(file.absolutePath)
    }

    fun getData(){
        //reset employee
        employeeInfor.detailInfors.clear()

        for (i in 0..listTitle.size - 1){
            if (listValue[i].text.toString() != ""){
                employeeInfor.detailInfors.add(DetailInfor(listTitle[i], listValue[i].text.toString()))
            }
        }
    }
}