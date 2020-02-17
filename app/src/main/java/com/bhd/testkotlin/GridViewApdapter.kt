package com.bhd.testkotlin


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class CustomAdapter(private var c: Context, var infor: ArrayList<GeneralInfor>) : BaseAdapter() {

    override fun getCount(): Int   {  return infor.size  }
    override fun getItem(i: Int): Any {  return infor[i] }
    override fun getItemId(i: Int): Long { return i.toLong()}

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view
        if (view == null) {
            //inflate layout resource if its null
            view = LayoutInflater.from(c).inflate(R.layout.general_infor_box, viewGroup, false)
        }

        //reference textviews and imageviews from our layout
        val textView = view!!.findViewById<TextView>(R.id.textView)
        val imageView = view!!.findViewById<ImageView>(R.id.imageView)

        textView.text = infor[i].name
        Glide.with(view!!).load(infor[i].imgSource).into(imageView)
        //handle itemclicks for the GridView

        return view
    }
}