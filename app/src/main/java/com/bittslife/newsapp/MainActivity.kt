package com.bittslife.newsapp

import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
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

    private var mInterstitialAd: InterstitialAd? = null
    var adRequest = AdRequest.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this)

        loadAd()
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                loadAd()
            }
        }

        initViews()
        getNews()

    }

    private fun loadAd() {

        InterstitialAd.load(this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(p0: InterstitialAd) {
                    super.onAdLoaded(p0)
                    mInterstitialAd = p0
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.e(TAG, "onAdFailedToLoad: ${p0}}" )
                }

            })
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
                if (movedUpItems % 5 == 0){
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(this@MainActivity)
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.")
                    }
                }
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