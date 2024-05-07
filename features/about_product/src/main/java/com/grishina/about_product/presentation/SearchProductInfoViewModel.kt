package com.grishina.about_product.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grishina.about_product.data.Recipe
import com.grishina.about_product.api.RetrofitClient
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchProductInfoViewModel : ViewModel() {
    enum class Status(val code: Int) {
        READY(0),
        IN_PROGRESS(1),
        SUCCESS_RESPONSE(200),
        EMPTY_RESPONSE(404),
        FAILURE_RESPONSE(409),
        BAD_RESPONSE(500),
    }

    private val mPostsLoaded = MutableLiveData<Status>(Status.READY)
    val postLoaded: LiveData<Status> = mPostsLoaded

    private val mApiService = RetrofitClient.apiService

    private val mPosts = mutableListOf<Recipe>()

    fun search(text: String) {
        mPosts.clear()
        mPostsLoaded.postValue(Status.IN_PROGRESS)
        val call = mApiService.searchRecipes(text)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful || response.code() == 404) {
                    val htmlContent = response.body()
                    if (htmlContent != null) {
                        parseHtmlContent(htmlContent)
                    } else {
                        mPostsLoaded.postValue(Status.EMPTY_RESPONSE)
                    }
                } else {
                    mPostsLoaded.postValue(Status.BAD_RESPONSE)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("TAG", t.message.toString())
                mPostsLoaded.postValue(Status.FAILURE_RESPONSE)
            }
        })
    }

    private fun parseHtmlContent(htmlContent: String) {
        val document: Document = Jsoup.parse(htmlContent)
        val posts = document.select(".cn-item")
        val baseUrl = "https://1000.menu"

        for (post in posts) {
            val name = post.select(".h5").text()
            val description = post.select(".preview-text").text()
            val relativeUrl = post.select("a").attr("href")
            val url = baseUrl + relativeUrl
            val recipe = Recipe(name, description, url)
            mPosts.add(recipe)
        }
        mPostsLoaded.postValue(Status.SUCCESS_RESPONSE)
    }


    fun getData(): List<Recipe> = mPosts
}
