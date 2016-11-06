package ru.russianpost.datacloud.store

import com.twitter.finagle.redis.Client
import com.twitter.finagle.redis.util.StringToBuf
import com.twitter.util.Future

/**
 * HyperLogLog store implementation with Redis as backend
 *
 * @author Alexey Ponkin
 */
class RedisHyperLogLogStore[T: Show](client: Client) extends HyperLogLogStore[T] {

  def putAll(name: String, elements: List[T]): Future[Unit] = {
    val key = StringToBuf(name)
    val stringElements = elements.map(e => StringToBuf(Show[T].show(e)))
    client.pfAdd(key, stringElements).unit
  }

  def estimateCount(names: Seq[String]): Future[Long] =
    client.pfCount(names.map(StringToBuf(_))).map(Long2long)
}
