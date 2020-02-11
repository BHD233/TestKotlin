package com.bhd.testkotlin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
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

import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        GetColorChange()

        //load default img
        var imageView = findViewById<ImageView>(R.id.imageView)

        Glide.with(this).load("https://cdn.mos.cms.futurecdn.net/ntFmJUZ8tw3ULD3tkBaAtf.jpg").into(imageView)
    }

    fun onSaveButtonClicked(view: View){
        var editText = findViewById<EditText>(R.id.editText)
        var textView = findViewById<TextView>(R.id.textView)

        textView.text = editText.text
    }

    fun onPickImageButtonClicked(view: View){
        var url = findViewById<TextView>(R.id.imageUrl)

        var imageView = findViewById<ImageView>(R.id.imageView)

        Glide.with(this).load(url.text.toString()).into(imageView)
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

