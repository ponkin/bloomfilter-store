package ru.russianpost.datacloud.store

import com.twitter.util.{
  Await,
  Awaitable,
  Duration,
  Future,
  Try
}

object IntegrationTest extends App {
  val store: BloomFilterStore[String] = RedisBloomFilterStore[String]("redis:6379", "test_bloom_filter", 1000000000, 0.001)
  val testId = "TEST_ID"
  val test = for {
    _ <- store.put(testId)
    mightContain <- store.mightContain(testId)
  } yield mightContain
  assert(Await.result(test) == true)
  println("Everything is ok!")
}
