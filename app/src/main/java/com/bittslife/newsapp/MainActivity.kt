package com.bittslife.newsapp

import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var newsList: RecyclerView
    lateinit var adapter: MyAdapter
    lateinit var layoutManager: LinearLayoutManager
    private var articles = mutableListOf<Article>()
    var page = 1
    var totalResult = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        getNews()

    }

    private fun initViews() {
        newsList = findViewById(R.id.newsList)
        adapter = MyAdapter(this,articles)
        newsList.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        newsList.layoutManager = layoutManager
        newsList.adapter = adapter

        newsList.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                Log.e(TAG, "onScrolled: itemCount ${layoutManager.itemCount}")
                Log.e(TAG, "onScrolled: childCount ${layoutManager.childCount}")
                Log.e(TAG, "onScrolled: moved up items ${layoutManager.findFirstVisibleItemPosition()}")

                val itemcount = layoutManager.itemCount
                val visibleItemsOnScreen = layoutManager.childCount
                val movedUpItems = layoutManager.findFirstVisibleItemPosition()

                if (totalResult > itemcount && movedUpItems + visibleItemsOnScreen + 5 > itemcount){
                    ++page
                    getNews()
                }

            }
        })
    }

    private fun getNews() {

        val news = NewsService.newsInstance.getHeadLines("in",page)
        news.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news!=null){
                    Log.e(TAG, "onResponse: "+news.toString() )
                    Log.e(TAG, "onResponse: totalResults ${news.totalResults}", )
                    totalResult = news.totalResults
                    articles.addAll(news.articles)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.e(TAG, "onFailure: "+t )
            }

        })
    }
}