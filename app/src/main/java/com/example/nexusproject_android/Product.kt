package com.example.nexusproject_android

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val idProducto: Int,
    val name: String,
    val price: Double,
    val imageUrl: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt() ?: 0
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idProducto)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeInt(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}