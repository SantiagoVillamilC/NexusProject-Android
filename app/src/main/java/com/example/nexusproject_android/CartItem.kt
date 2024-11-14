package com.example.nexusproject_android

import android.os.Parcel
import android.os.Parcelable

data class CartItem(
    val product: Product,
    var quantity: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        // Leer todos los parámetros necesarios para construir un Product
        Product(
            parcel.readInt(), // idProducto
            parcel.readString() ?: "", // nombre
            parcel.readDouble(), // precio
            parcel.readString() ?: "" // imageUrl
        ),
        parcel.readInt() // quantity
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(product, flags)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}

