package com.bhd.testkotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextPaint
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi

//for image
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URL
import java.util.*


class User(var name: String){

}

val json = """
{
  "url": "https://api.github.com/repos/square/okio/issues/156",
  "id": 91393390,
  "number": 156,
  "title": "ByteString.utf8CharSequence()",
  "labels": [
    {
      "url": "https://api.github.com/repos/square/okio/labels/enhancement",
      "id": 86454697,
      "name": "enhancement",
      "color": "84b6eb"
    }
  ],
  "milestone": {
    "url": "https://api.github.com/repos/square/okio/milestones/2",
    "id": 992290,
    "title": "Icebox",
    "creator": {
      "url": "https://api.github.com/users/swankjesse",
      "login": "swankjesse"
    },
    "open_issues": 12,
    "closed_issues": 1,
    "created_at": "2015-02-24T00:59:14.000Z"
  },
  "state": "open",
  "created_at": "2015-06-27T00:49:40.000Z",
  "body": "Would be interesting to return a `CharSequence` that's backed by the same `ByteArray`.\n"
}
"""

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        var intent = Intent(this, EmployeesInforPage::class.java)

        startActivity(intent)

        /*
        GetColorChange()

        //load default img
        var imageView = findViewById<ImageView>(R.id.imageView)


        Glide.with(this).load("https://api.adorable.io/avatar/285.png")
            .override(300, 200)
            .into(imageView)
        */
    }


    fun onSaveButtonClicked(view: View){
        var editText = findViewById<EditText>(R.id.editText)
        var textView = findViewById<TextView>(R.id.textView)

        textView.text = editText.text
    }

    fun onGoToEmpoyeesPageClicked(view: View){

        var intent = Intent(this, EmployeesInforPage::class.java)

        startActivity(intent)
    }

    fun onPickImageButtonClicked(view: View){
        //var url = findViewById<TextView>(R.id.imageUrl)

        //var imageView = findViewById<ImageView>(R.id.imageView)

        //Glide.with(this).load(url.text.toString()).into(imageView)

        dispatchTakePictureIntent()
        //val intent = Intent(Intent.ACTION_PICK)
        //intent.type = "image/*"
        //startActivityForResult(intent, 100)
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageView = findViewById<ImageView>(R.id.imageView)

        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            imageView.setImageURI(data?.data) // handle chosen image
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                val uri = saveImageToExternalStorage(imageBitmap)
                imageView.setImageURI(uri)
            }
    }

    private fun saveImageToExternalStorage(bitmap:Bitmap):Uri{
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

    fun GetColorChange(){
        //get all seeks
        var redSeekbar = findViewById<SeekBar>(R.id.redSeek)
        var greenSeekbar = findViewById<SeekBar>(R.id.greenSeek)
        var blueSeekbar = findViewById<SeekBar>(R.id.blueSeek)

        var colorLabel = findViewById<TextView>(R.id.colorShow)

        //get color for the first time
        val colorStr = getColorString(redSeekbar.progress, greenSeekbar.progress, blueSeekbar.progress)

        //set color
        colorLabel.setBackgroundColor(Color.parseColor(colorStr))

        //add all listener
        redSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar

                //get color
                val colorStr = getColorString(redSeekbar.progress, greenSeekbar.progress, blueSeekbar.progress)

                //set color
                colorLabel.setBackgroundColor(Color.parseColor(colorStr))
            }
            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })

        greenSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar

                //get color
                val colorStr = getColorString(redSeekbar.progress, greenSeekbar.progress, blueSeekbar.progress)

                //set color
                colorLabel.setBackgroundColor(Color.parseColor(colorStr))
            }
            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })

        blueSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar

                //get color
                val colorStr = getColorString(redSeekbar.progress, greenSeekbar.progress, blueSeekbar.progress)

                //set color
                colorLabel.setBackgroundColor(Color.parseColor(colorStr))
            }
            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })
    }

    //reference: https://www.tutorialkart.com/kotlin-android/android-color-picker/
    //convert r, g, b to string
    fun getColorString(red: Int, green: Int, blue: Int): String {
        var a = Integer.toHexString(255)
        if(a.length==1) a = "0"+a
        var r = Integer.toHexString(red)
        if(r.length==1) r = "0"+r
        var g = Integer.toHexString(green)
        if(g.length==1) g = "0"+g
        var b = Integer.toHexString(blue)
        if(b.length==1) b = "0"+b
        return "#" + a + r + g + b
    }
}

