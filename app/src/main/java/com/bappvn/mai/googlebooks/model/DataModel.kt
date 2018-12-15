package com.bappvn.mai.googlebooks.model

import java.io.Serializable
import java.net.URL

typealias Author = String
typealias Category = String

data class CoverImageURL(val thumbnail: URL): Serializable

data class VolumeInformation(val title: String,
                           val subtitle: String,
                           val authors: List<Author>,
                           val description: String,
                           val category: List<Category>,
                           val imageLinks: CoverImageURL): Serializable

data class Volume(val id: String, val volumeInfo: VolumeInformation): Serializable

data class VolumeList(val totalItems: Int, val items: List<Volume>)