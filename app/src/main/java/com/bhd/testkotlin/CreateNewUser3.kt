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


class CreateNewUser3: AppCompatActivity(){

    var employeeInfor = EmployeeInfor()
    val listValue: MutableList<String> = arrayListOf()
    val listTitle: MutableList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_new_user_page1)

        getInterface()

        //reset title
        val title = findViewById<TextView>(R.id.title)
        title.text = "Create New User #3"
    }

    fun getInterface(){
        val layout = findViewById<LinearLayout>(R.id.mainLayout)

        //hide image
        val imageView = findViewById<ImageView>(R.id.createAvatarImage)
        imageView.isVisible = false

        //get all atribute and show it up
        for(i in (AllAtributeOfDetailInfor.size / 3 * 2 + 1 .. AllAtributeOfDetailInfor.size - 1)){
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
            listValue.add(valueTextView.text.toString())

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
        button.text = "Save"
        button.setOnClickListener(View.OnClickListener {
            onNextButtonClicked(it)
        })

        layout.addView(button)
    }

    fun getData(){

    }

    fun onNextButtonClicked(view: View){
        setResult(RESULT_OK, intent.putExtra("data", employeeInfor))
        finish()
    }
}