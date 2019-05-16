package com.octopus.kotlin

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KotlinTest{

    suspend  fun test(){
        delay(1000)
        println("a+++")
    }

    suspend  fun testB(){
        delay(1000)
        println("b+++")
    }
}


fun main(args : Array<String>){
    val job = GlobalScope.launch {
        val test = KotlinTest()
        test.test()
        test.testB()
    }
    Thread.currentThread().join()
}