package com.bhd.testkotlin

import android.os.Parcel
import android.os.Parcelable

class EmployeeInfor(var name: String? = "", var age: Int = 0, var gender: String? = "", var address: String? = "") :
    Parcelable {
    fun toStringToShowUp(): MutableList<Pair<String, String>>{
        val result: MutableList<Pair<String, String>> = arrayListOf()

        //add for all attribute
        result.add(Pair("Name", name.toString()))
        result.add(Pair("Age", age.toString()))
        result.add(Pair("Gender", gender.toString()))
        result.add(Pair("Address", address.toString()))

        return result
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(age)
        parcel.writeString(gender)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EmployeeInfor> {
        override fun createFromParcel(parcel: Parcel): EmployeeInfor {
            return EmployeeInfor(parcel)
        }

        override fun newArray(size: Int): Array<EmployeeInfor?> {
            return arrayOfNulls(size)
        }
    }

}