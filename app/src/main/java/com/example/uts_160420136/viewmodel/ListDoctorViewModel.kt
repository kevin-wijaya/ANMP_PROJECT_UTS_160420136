package com.example.uts_160420136.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.uts_160420136.model.Doctor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListDoctorViewModel(application: Application):AndroidViewModel(application) {
    val doctorsLD = MutableLiveData<List<Doctor>>()
    val loadingDoneLD = MutableLiveData<Boolean>()
    val loadingErrorLD = MutableLiveData<Boolean>()

    private val TAG = "listDoctorTag" //identifier pada string request
    private var queue:RequestQueue ?= null

    fun load() {
        loadingDoneLD.value = false
        loadingErrorLD.value = false

        queue = Volley.newRequestQueue(getApplication())
        var url = "https://kevinwijaya.000webhostapp.com/ANMP/UTS/json.php" +
                "?T0KEN=ANMPUTS160420136KEVINWIJAYA&4CCESS=DOCTORS"
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                loadingDoneLD.value = true
                val sType = object : TypeToken<List<Doctor>>() {}.type
                val result = Gson().fromJson<List<Doctor>>(response, sType)
                doctorsLD.value =  result
                Log.d("volley", response.toString())
            },
            { error ->
                loadingDoneLD.value = true
                loadingErrorLD.value = true
                Log.d("volley", error.toString())
            })

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}