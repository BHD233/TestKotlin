package com.bhd.testkotlin

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class GeneralInfor(var name: String? = "", var imgSource: String? = "") : Parcelable{
}


@Parcelize
class DetailInfor(var inforTitle: String? = "", var inforDetail : String? = "") : Parcelable{
}