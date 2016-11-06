package ru.russianpost.datacloud.store

/**
 * Type-class for all data that can be hashed.
 *
 * @author Alexey Ponkin
 */
trait Show[T] {

  /**
   * Get hash of element
   * We need Long value since
   * bloom filters can be huge
   */
  def show(t: T): String

}

object Show {

  /**
   * Syntatic sugar for type constraints
   */
  def apply[T: Show]: Show[T] = implicitly

}
