package ru.russianpost.datacloud.store

import com.twitter.util.Future

/**
 * Main trait for HyperLogLog
 * https://en.wikipedia.org/wiki/HyperLogLog
 *
 * @author Alexey Ponkin
 */
trait HyperLogLogStore[T] {

  /**
   * Put element in HyperLogLog - means
   * we have +1 element ocurence
   *
   * @param name - unique name of HyperLogLog
   * @param element - element to increment occurnce
   */
  def putAll(name: String, elements: List[T]): Future[Unit]

  /**
   * Count number of occurencies of
   * element in HyperLogLog wit given name.
   *
   * @param name - unique name of HyperLogLog
   * @param element - element to find occurence
   */
  def estimateCount(name: Seq[String]): Future[Long]

}
