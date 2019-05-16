package com.octopus.kotlin.producer

import com.octopus.kotlin.model.Article
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce

class ArticleProducer {
    private val feeds = listOf(
            Feed("npr", "https://www.npr.org/rss/rss.php?id=1001"),
            Feed("cnn", "http://rss.cnn.com/rss/cnn_topstories.rss"),
            Feed("fox", "http://feeds.foxnews.com/foxnews/latest?format=xml")
    )

    private val dispatcher = newFixedThreadPoolContext(2, "IO")

    val producer =  Channel<List<Article>>()

    fun fetchArticles() : List<Article> {

        val list = listOf<Article>()
        list + Feed("bbbbbb","bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb")
        list + Feed("aaaaaaa","w1bbbbbbbbbbbbbbbbbbbbbbbbbb")
        list + Feed("aaaaaaa","bbbbbbbbbbbbbbbbbbbbbbbbbbbb")
        return list
    }
}




fun main() = runBlocking {

    val channel = Channel<Int>()

    launch {
        for (x in 1..5) channel.send(x * x)
        channel.close() // we're done sending
    }

    // here we print received values using `for` loop (until the channel is closed)
    for (y in channel) println(y)
    println("Done!")
}

