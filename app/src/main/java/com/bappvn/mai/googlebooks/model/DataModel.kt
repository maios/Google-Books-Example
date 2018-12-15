package com.bappvn.mai.googlebooks.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.net.URL

typealias Author = String
typealias Category = String

data class CoverImageURL(val thumbnail: URL?): Serializable

data class VolumeInformation(val title: String,
                           val subtitle: String,
                           val authors: List<Author>,
                           val description: String,
                           val category: List<Category>,
                           val imageLinks: CoverImageURL?): Serializable

data class SaleInformation(val buyLink: URL): Serializable

data class Volume(val id: String, val volumeInfo: VolumeInformation, val saleInfo: SaleInformation): Serializable

data class VolumeList(val _totalItems: Int, @SerializedName("items") private val _items: List<Volume>? = emptyList()) {
    val items
        get() = _items ?: emptyList()
}