package com.bittslife.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var newsList: RecyclerView
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        getNews()

    }

    private fun initViews() {
        newsList = findViewById(R.id.newsList)
        newsList.setHasFixedSize(true)
        newsList.layoutManager = LinearLayoutManager(this)
    }

    private fun getNews() {

        val news = NewsService.newsInstance.getHeadLines("in",1)
        news.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news!=null){
                    Log.e(TAG, "onResponse: "+news.toString() )
                    adapter = MyAdapter(this@MainActivity,news.articles)
                    newsList.adapter = adapter

                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.e(TAG, "onFailure: "+t )
            }

        })
    }
}