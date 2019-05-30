package com.octopus.kotlin.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.octopus.kotlin.adapter.ArticleAdapter
import com.octopus.kotlin.producer.ArticleProducer
import com.octopus.test.R
import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.cancel
import org.apache.poi.xdgf.usermodel.section.geometry.EllipticalArcTo.draw
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
@ExperimentalCoroutinesApi
class MainActivity :AppCompatActivity() {

    private lateinit var articles: RecyclerView
    private lateinit var viewAdapter: ArticleAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotolin_test_layout)
        findViewById<View>(R.id.test).setOnClickListener {
        }

       /*
        viewManager = LinearLayoutManager(this)
        viewAdapter = ArticleAdapter()

        articles = findViewById<RecyclerView>(R.id.articles).apply{
            adapter = viewAdapter
            layoutManager =  this@MainActivity.viewManager
        }
        viewAdapter.add(ArticleProducer().fetchArticles())
        findViewById<View>(R.id.progressBar).visibility = View.GONE
*/
    }


    override fun onDestroy() {
        super.onDestroy()

    }
}