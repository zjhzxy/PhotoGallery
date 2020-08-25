package com.italker.qiujuer.photogallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import java.lang.Math.ceil

const val DATA_STATUS_CAN_LOAD_MORE = 0
const val DATA_STATUS_NO_MORE = 1
const val DATA_STATUS_NETWORK_ERROR = 2

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _dataStatusLive = MutableLiveData<Int>()
    val dataStatusLive: LiveData<Int> get() = _dataStatusLive
    private val _photoListLive = MutableLiveData<List<PhotoItem>>()
    val photoListLive: LiveData<List<PhotoItem>>
        get() = _photoListLive
    private val keyWords = arrayListOf("cat", "flower", "student", "beauty")
    private val perPage = 50
    private var currentPage = 1
    private var totalPage = 1
    private var currentKey = "girl"
    private var isNewQuery = true
    private var isLoading = false
    var needToScrollToTop = true
    init {
        resetQuery()
    }


    fun resetQuery() {
        currentPage = 1
        totalPage = 1
        currentKey = keyWords.random()
        isNewQuery = true
        needToScrollToTop = true
        fetchData()
    }

    fun fetchData() {

        if (isLoading) return

        if (currentPage > totalPage) {
            _dataStatusLive.value = DATA_STATUS_NO_MORE
            return
        }
        isLoading = true
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            Response.Listener {
                with(Gson().fromJson(it, Pixabay::class.java)) {
                    totalPage = kotlin.math.ceil(totalHits.toDouble() / perPage).toInt()
                    if (isNewQuery) {
                        _photoListLive.value = hits.toList()
                    } else {
                        _photoListLive.value =
                            arrayListOf(_photoListLive.value!!, hits.toList()).flatten()
                    }
                }
                _dataStatusLive.value = DATA_STATUS_CAN_LOAD_MORE
                isLoading = false
                isNewQuery = false
                currentPage++
            },
            Response.ErrorListener {
                _dataStatusLive.value = DATA_STATUS_NETWORK_ERROR
                isLoading = false
            }
        )
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }

    private fun getUrl(): String {
        return "https://pixabay.com/api/?key=17854120-364dbed33dbd2a2ef76d32618&q=${keyWords.random()}&per_page=100"
    }


}