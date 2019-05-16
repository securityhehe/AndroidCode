package com.octopus.kotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.octopus.kotlin.model.Article
import com.octopus.test.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


interface  ArticleLoader{
    suspend fun loadMore()
}

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    class ViewHolder(val container: LinearLayout, val feed: TextView, val title: TextView, val summary: TextView) : RecyclerView.ViewHolder(container)

    private val articles : MutableList<Article> = mutableListOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val layout = LayoutInflater.from(p0.context).inflate(R.layout.kotlin_test, p0, false) as LinearLayout
        val text1 = layout.findViewById(R.id.test1) as TextView
        val text2 = layout.findViewById(R.id.test2) as TextView
        val text3 = layout.findViewById(R.id.test3) as TextView
        return  ViewHolder(layout, text1, text2, text3)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val article = articles[p1]
        p0.feed.text = article.title
        p0.title.text = article.summary
        p0.summary.text = article.feed
        p0.container.setOnClickListener{
            println("pos = $p1")
        }
    }

    fun add(articles: List<Article>) {
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    fun add(article: Article) {
        this.articles.add(article)
        notifyDataSetChanged()
    }

    fun clear() {
        this.articles.clear()
        notifyDataSetChanged()
    }


}
class Test{
    suspend  fun  getInt(): Int{
        delay(1000)
        return 10
    }

    suspend fun getString(aa:Int):String{
        delay(1000)
        return aa.toString()
    }
}

fun main(args :Array<String>){
    GlobalScope.launch {
        val test = Test()
        val a =test.getInt()
        test.getString(a)


    }

}


