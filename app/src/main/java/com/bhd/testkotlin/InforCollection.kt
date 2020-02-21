package com.bhd.testkotlin

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


val AllAtributeOfDetailInfor: Array<String> = arrayOf("name", "address", "maiden_name",
    "birth_data", "phone_h", "phone_w","email_u","email_d","username","password",
    "domain","useragent","ipv4","macaddress","plasticcard","cardexpir","bonus",
    "company","color","uuid","height","weight","blood","eye","hair","pict","url",
    "sport","ipv4_url","email_url","domain_url")

@Parcelize
class GeneralInfor(var name: String? = "", var imgSource: String? = "") : Parcelable{
}


@Parcelize
class DetailInfor(var inforTitle: String? = "", var inforDetail : String? = "") : Parcelable{
}