package hash

import sorting.Data

object HashFuncs {


  /**
   * djb hash function
   *
   * @param objdata: Data - Объект типа Data
   * @return hash: Long - значение хэша
   */
  def simpleHash(str: String): Long = {
    var hash: Long = 5381

    for (i <- 0 to str.length) {
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
    for (i <- 0 to str.length) {
      hash += str.charAt(i)
      hash -= (hash << 13) | (hash >> 19)
    }
    hash
  }


  def hashAdd(hashMap: Map[Int, Data], obj: Data): Unit = {
    if (hashMap(obj.hash) == None) {
      hashMap.updated(obj.hash, obj)
    }
  }

  def hashGet(hashMap: Map[Int, Data], key: Int): Option[Data] = {
    hashMap.get(key)
  }


}
