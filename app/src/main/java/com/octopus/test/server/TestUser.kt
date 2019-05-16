package com.octopus.test.server

import android.service.autofill.UserData
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KProperty
import kotlin.text.Typography.times


class TestUser{

   //lateninit 忽略编译检查，需要自己手动初始化。还必须是非基本类型。
   lateinit var  aaa : UserData

   //延迟初始化，第一次使用的使用才会初始化这个变量 。
   val lazyVar by  lazy { 30 }

   fun println(){
      kotlin.io.println("aaaaaaaaaaaaaaaaaa")
   }

   //by 可以表示
   class Example {
      var p: String by Delegate()

   }

   class Delegate {
      operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
         return "$thisRef, thank you for delegating '${property.name}' to me!"
      }

      operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
         println("$value has been assigned to '${property.name}' in $thisRef.")
      }
   }

   interface A {
      fun foo() { print("A") }
      fun bar()
   }

   interface B {
      fun foo() { print("B") }
      fun bar() { print("bar") }
   }

   class C : A {
      override fun bar() { print("bar") }
   }

   class D : A, B {
      override fun foo() {
         super<A>.foo()
         super<B>.foo()
      }

      override fun bar() {
         super<B>.bar()
      }
   }

   //对类的某个方法进行扩展,重载。
   fun B.foo(){
      println("aaaaaaaaaaaaaaaaaaaaaaaa")
   }

   //扩展。
   fun B.fffff(){
      println("bbbbbbbbbbbbbbbbbbbbbbbbbbbb")
   }

   data class User(val name: String = "", val age: Int = 0)


   fun test() {
      GlobalScope.launch { // launch a new coroutine in background and continue
         delay(1000L)
         println("World!")
      }

      runBlocking {             // but this expression blocks the main thread
         delay(2000L)  // ... while we delay for 2 seconds to keep JVM alive
      }

      println("Hello,----") // main thread continues while coroutine is delayed
      Thread.sleep(2000L) // block main thread for 2 seconds to keep JVM alive
   }


   fun test2() = runBlocking {
      repeat(100) { // launch a lot of coroutines
         launch {
            delay(1000L)
            print(".")
         }
      }
   }

   fun main() = runBlocking {
      //sampleStart
      val job = launch {
         repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(2000)
         }
      }
      delay(13000L) // delay a bit
      println("main: I'm tired of waiting!")
      job.cancel() // cancels the job
      job.join() // waits for job's completion
      println("main: Now I can quit.")
//sampleEnd
   }

   fun main1() = runBlocking {
      //sampleStart
      val job = launch {
         repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(2000)
         }
      }
      println("main: I'm tired of waiting!")
      job.join() // waits for job's completion
      println("main: Now I can quit.")
//sampleEnd
   }


   fun main3() = runBlocking {
      val startTime = System.currentTimeMillis()
      //指定在那个线程上执行协程。
      val job = launch(Dispatchers.Default) {
         var nextPrintTime = startTime
         var i = 0
         while (i < 5) { // computation loop, just wastes CPU
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
               println("I'm sleeping ${i++} ...")
               nextPrintTime += 500L
            }
         }
      }
      println("main: I'm tired of waiting!")
      job.cancelAndJoin() // cancels the job and waits for its completion
      println("main: Now I can quit.")
   }


   fun main5() = runBlocking {
      val startTime = System.currentTimeMillis()
      val job = launch(Dispatchers.Default) {
         var nextPrintTime = startTime
         var i = 0
         while (isActive) { // cancellable computation loop
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
               println("I'm sleeping ${i++} ...")
               nextPrintTime += 500L
            }
         }
      }
      delay(1300L) // delay a bit
      println("main: I'm tired of waiting!")
      job.cancelAndJoin() // cancels the job and waits for its completion
      println("main: Now I can quit.")
   }


   fun main6() = runBlocking {
      val job = launch {
         try {
            repeat(1000) { i ->
               delay(500L)
               println("I'm sleeping $i ...")
            }
         } finally {
            println("I'm running finally")
         }
      }
      delay(1300L) // delay a bit
      println("main: I'm tired of waiting!")
      job.cancelAndJoin() // cancels the job and waits for its completion
      println("main: Now I can quit.")
   }

   fun main7() = runBlocking {
      val job = launch {
         try {
            repeat(1000) { i ->
               println("I'm sleeping $i ...")
               delay(500L)
            }
         } finally {
            withContext(NonCancellable) {
               println("I'm running finally")
               delay(1000L)
               println("And I've just delayed for 1 sec because I'm non-cancellable")
            }
         }
      }
      delay(1300L) // delay a bit
      println("main: I'm tired of waiting!")
      job.cancelAndJoin() // cancels the job and waits for its completion
      println("main: Now I can quit.")
   }


   fun main8()  = runBlocking {
      withTimeout(1300L) {
         repeat(2) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
         }
      }
   }

   fun main9() = runBlocking {
      val result = withTimeoutOrNull(2600L) {
         repeat(2) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
         }
         "Resume" // will get cancelled before it produces this result
      }
      println("Result is $result")
   }

   // kotlin 协程
   fun main10() = GlobalScope.async {

   }

   fun main12() = GlobalScope.launch {

   }

   fun  main13()  = GlobalScope.run {  }

   fun timeCountDown(){

   }

    class Data<T> {
       val ok =  OkHttpClient()
       fun test() {
          val build = Request.Builder()
          val req = build.url("http://www.baidu.com").get().build()
          val call = ok.newCall(req)
          call.enqueue(object : Callback {
             override fun onResponse(call: Call?, response: Response?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
             }

             override fun onFailure(call: Call?, e: IOException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
             }
          })
       }
        //匿名函数
    }

    val a = { i : Int -> i+1}
    var b = { i : String -> "ewewe"}


   fun a(){
      val test :String.(Int) ->String  = { times -> this.repeat(times)}
      val two : (String,Int) -> String  = test
   }


   val aa : (Int,String) -> String = { _,time ->
      kotlin.io.println(time)
      time
   }

   // 传递函数。有点像c中的函数指针。
   fun testFunction (a : (Int,String)->String){
      a(1,"10000000000000000000")
   }




}

fun main(args: Array<String>){
    TestUser().testFunction(TestUser().aa)
}
