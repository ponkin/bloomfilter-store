package ru.russianpost.datacloud

package object store {

  implicit val stringShow: Show[String] = new Show[String] {
    def show(element: String): String = element
  }

}
