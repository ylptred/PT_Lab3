package hash

import sorting.Data
import sorting.Main.map_simple
import sorting.Main.map_hard

object HashFuncs {

  def adjust[A, B](m: Map[A, B], k: A)(f: B => B): Map[A, B] = m.updated(k, f(m(k)))

  var res: Array[Data] = Array.empty[Data]


  /**
   * djb hash function
   *
   * @param objdata: Data - Объект типа Data
   * @return hash: Long - значение хэша
   */
  def simpleHash(str: String): Long = {
    var hash: Long = 5381

    for (i <- 0 until str.length) {
      hash = ((hash << 5) + hash) + str.charAt(i)
    }
    hash
  }


  /**
   * rot13 hash function
   *
   * @param objdata: Data - объект типа Data
   * @return hash: Long
   */
  def hardHash(str: String): Long = {
    var hash: Long = 0
    for (i <- 0 until str.length) {
      hash += str.charAt(i)
      hash -= (hash << 13) | (hash >> 19)
    }
    hash
  }

  def hashAdd(obj: Data, hash_func: String): Unit = {
    if (hash_func == "simple") {
      val hash_s: Long = simpleHash(obj.serviceName)
      if (!map_simple.contains(hash_s)) {
        map_simple += (hash_s -> Array(obj))
      }
      var tmp: Array[Data] = map_simple(hash_s)
      tmp = tmp :+ obj
      tmp = tmp.distinct
      map_simple += (hash_s -> tmp)
    } else if (hash_func == "hard") {
      val hash_d: Long = hardHash(obj.serviceName)
      if (!map_hard.contains(hash_d)) {
        map_hard += (hash_d -> Array(obj))
      }
      var tmp: Array[Data] = map_hard(hash_d)
      tmp = tmp :+ obj
      tmp = tmp.distinct
      map_hard += (hash_d -> tmp)
    }
  }

  def GET(data: Data, key: String): Unit = {
    if (data.serviceName == key) {
      res = res :+ data
    }
  }

  def hashGet(hashMap: Map[Long, Array[Data]], key: String, hash_func: String): Unit = {
    if (hash_func == "simple") {
      for (i <- map_simple(simpleHash(key))) {
        GET(i, key)
      }
    } else if (hash_func == "hard") {
      for (j <- map_hard(hardHash(key))) {
        GET(j, key)
      }
    }
  }

  def collisions(hashMap: Map[Long, Array[Data]]): Long = {
    var counter = 0
    for (i <- hashMap.values) {
      if (i.length > 1) {
        counter += (i.toSet).size - 1
      }
    }
    counter
  }

}
