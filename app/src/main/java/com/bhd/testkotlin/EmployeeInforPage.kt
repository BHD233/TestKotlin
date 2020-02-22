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

        getInterface()
    }

    fun getInterface(){
        val layout = findViewById<LinearLayout>(R.id.layout)

        //set name to title
        val titleText = findViewById<TextView>(R.id.title)
        titleText.text = employeeInfor.generalInfor.name

        //display image and add 2 change button
        val imageView = findViewById<ImageView>(R.id.avatarView)
        Glide.with(this).load(employeeInfor.generalInfor.imgSource).into(imageView)

        //get all atribute and show it up
        employeeInfor.detailInfors.forEach(){
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

    fun onEditButtonClicked(view: View) {
        val chooseFromGallery = findViewById<Button>(R.id.chooseFromGalleryButton1)
        val chooseFromCamera = findViewById<Button>(R.id.chooseFromCameraButton1)

        val button = view as Button

        if (button.text == "Edit") {
            //enable text for edit
            listEditText.forEach() { editText ->
                editText.isEnabled = true
            }

            //make 2 button visible
            chooseFromCamera.isVisible = true
            chooseFromGallery.isVisible = true

            //change text button
            button.text = "Save"
        } else {
            //prevent edit then save data
            listEditText.forEach() { editText ->
                editText.isEnabled = false
            }
            getDataFromEditText()

            //make 2 button invisible
            chooseFromCamera.isVisible = false
            chooseFromGallery.isVisible = false

            //change text button
            button.text = "Edit"
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageView = findViewById<ImageView>(R.id.avatarView)

        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            //gallery
            val uri = data?.data
            imageView.setImageURI(uri) // handle chosen image

            val path = getPath(uri)

            employeeInfor.generalInfor.imgSource = path

        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            //camera
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val uri = saveImageToExternalStorage(imageBitmap)
            imageView.setImageURI(uri)

            //save to image
            employeeInfor.generalInfor.imgSource = uri.toString()
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