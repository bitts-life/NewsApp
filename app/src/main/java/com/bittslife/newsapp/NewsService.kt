package com.bittslife.newsapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=7e72099fb3cd4f52bdabfaa50ec80421
// https://newsapi.org/v2/everything?domains=wsj.com&apiKey=7e72099fb3cd4f52bdabfaa50ec80421

const val BASE_URL = "https://newsapi.org/"
const val API_KEY = "7e72099fb3cd4f52bdabfaa50ec80421"

interface NewsInterface {

    @GET("v2/top-headlines?apiKey=$API_KEY")
    fun getHeadLines(@Query("country") country: String,@Query("page") page: Int): Call<News>

}

object NewsService {

    val newsInstance: NewsInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        newsInstance = retrofit.create(NewsInterface::class.java)
    }

}