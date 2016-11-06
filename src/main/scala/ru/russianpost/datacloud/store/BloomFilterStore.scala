package ru.russianpost.datacloud.store

import com.twitter.util.Future

/**
 * Main trait for BloomFilters store
 *
 * @author Alexey Ponkin
 */
trait BloomFilterStore[T] {

  /**
   * Put element inside bloom filter
   *
   * @param element - element that need to be inserted
   * @return Future.Success - if element was added succefully
   */
  def put(element: T): Future[Unit]

  /**
   * Check whether element is in store
   *
   * @param element - element that need to be checked
   * @return true - if element may be in  bloom filter
   */
  def mightContain(element: T): Future[Boolean]

}
