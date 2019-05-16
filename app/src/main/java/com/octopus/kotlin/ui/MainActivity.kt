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
class MainActivity() :AppCompatActivity(),CoroutineScope {



    private lateinit var articles: RecyclerView
    private lateinit var viewAdapter: ArticleAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

/*    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotolin_test_layout)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ArticleAdapter()

        articles = findViewById<RecyclerView>(R.id.articles).apply{
            adapter = viewAdapter
            layoutManager =  this@MainActivity.viewManager
        }
        viewAdapter.add(ArticleProducer().fetchArticles())
        findViewById<View>(R.id.progressBar).visibility = View.GONE
    }*/

    lateinit var job: Job

    // CoroutineScope 的实现
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 当 Activity 销毁的时候取消该 Scope 管理的 job。
        // 这样在该 Scope 内创建的子 Coroutine 都会被自动的取消。
        job.cancel()
    }

    /*
     * 注意 coroutine builder 的 scope， 如果 activity 被销毁了或者该函数内创建的 Coroutine
     * 抛出异常了，则所有子 Coroutines 都会被自动取消。不需要手工去取消。
     */
    fun loadDataFromUI() = launch { // <- 自动继承当前 activity 的 scope context，所以在 UI 线程执行
        val ioData = async(Dispatchers.IO) { // <- launch scope 的扩展函数，指定了 IO dispatcher，所以在 IO 线程运行
            // 在这里执行阻塞的 I/O 耗时操作
        }
        // 和上面的并非 I/O 同时执行的其他操作
        val data = ioData.await() // 等待阻塞 I/O 操作的返回结果
    }








}