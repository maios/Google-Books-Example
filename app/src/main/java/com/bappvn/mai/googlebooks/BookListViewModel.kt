package com.bappvn.mai.googlebooks

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.bappvn.mai.googlebooks.model.VolumeList
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.getOrElse

interface BookSearch {
    fun search(term: String)
}

class BookListViewModel(val fuelManager: FuelManager = FuelManager.instance): ViewModel(), BookSearch {

    val bookList: MutableLiveData<VolumeList> by lazy {
        MutableLiveData<VolumeList>()
    }

    init {
        fuelManager.basePath = "https://www.googleapis.com/books/v1"
        fuelManager.baseParams = listOf("key" to "AIzaSyA_v0NJKTBxqIR_mng-4D1YrFHe2jM7fBg")
    }

    override fun search(term: String) {
        Fuel.get("/volumes", parameters = listOf("q" to term, "maxResults" to 40))
            .responseObject<VolumeList> { _, _, result ->
                bookList.value = result.getOrElse(VolumeList(0, emptyList()))
            }
    }
}