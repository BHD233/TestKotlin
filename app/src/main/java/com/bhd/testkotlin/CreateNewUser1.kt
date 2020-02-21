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


class CreateNewUser1: AppCompatActivity(){

    var employeeInfor = EmployeeInfor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_new_user_page1)

        getInterface()
    }

    fun getInterface(){
        val layout = findViewById<LinearLayout>(R.id.mainLayout)

        //display image and add 2 change button
        val imageView = findViewById<ImageView>(R.id.createAvatarImage)
        Glide.with(this).load("https://discovery-park.co.uk/wp-content/uploads/2017/06/avatar-default.png").into(imageView)

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

    fun onNextButtonClicked(view: View){
        val intent = Intent(this, CreateNewUser2::class.java)

        startActivityForResult(intent, 0)
    }

    fun onChooseImageFromGalleryClicked(view: View){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    fun onChooseImageFromCameraClicked(view: View){
        val i = Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(i, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageView = findViewById<ImageView>(R.id.createAvatarImage)

        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            val uri = data?.data
            imageView.setImageURI(uri)
            val path = getPath(uri)
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val uri = saveImageToExternalStorage(imageBitmap)
            imageView.setImageURI(uri)
        } else if (requestCode == 0){
            val result: EmployeeInfor = (data as Intent).getParcelableExtra("data")
            setResult(RESULT_OK, intent.putExtra("data", result))
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
}