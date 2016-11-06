package ru.russianpost.datacloud.store

import com.twitter.util.{ Future, Futures }
import com.twitter.finagle.Redis
import com.twitter.finagle.redis.Client
import com.twitter.finagle.redis.util.StringToBuf
import com.twitter.algebird.BFHash

import scala.util.MurmurHash

/**
 * Implementation of bloom filter store
 * backed with the Redis
 *
 * @author Alexey Ponkin
 *
 * @param name - name of bloom filter
 * @param width - number of bits in bit vector
 * @param numHashes - number of hash functions
 * @param client - finagle-redis client
 */
class RedisBloomFilterStore[T: Show](
    name: String,
    width: Long,
    numHashes: Int,
    client: Client
) extends BloomFilterStore[T] {
  import collection.JavaConverters._

  val blockSize = Int.MaxValue
  val numOfPartitions = width / blockSize + 1

  val hashes = BFHash(numHashes, if (width < blockSize) width.toInt else blockSize)

  def put(element: T): Future[Unit] = {
    val stringElement = Show[T].show(element)
    val partition = MurmurHash.stringHash(stringElement) % numOfPartitions
    val bits = hashes(stringElement)
    val key = StringToBuf(s"${name}:${partition}")
    val txResult = client.transaction { tx =>
      bits.map(offset => tx.setBit(key, offset, 1).unit).reduce(_ before _)
    }
    txResult.unit
  }

  def mightContain(element: T): Future[Boolean] = {
    val stringElement = Show[T].show(element)
    val partition = MurmurHash.stringHash(stringElement) % numOfPartitions
    val bits = hashes(stringElement)
    val key = StringToBuf(s"${name}:${partition}")
    Futures.collect(
      bits.map(offset => client.getBit(key, offset)).toList.asJava
    ).map(_.asScala.forall(_ == 1))
  }
}

object RedisBloomFilterStore {

  // Compute optimal number of hashes: k = m/n ln(2)
  def optimalNumHashes(numEntries: Long, width: Long): Int =
    math.ceil(width / numEntries * math.log(2)).toInt

  // Compute optimal width: m = - n ln(p) / (ln(2))^2
  def optimalWidth(numEntries: Long, fpProb: Double): Long =
    math.ceil(-1 * numEntries * math.log(fpProb) / math.log(2) / math.log(2)).toLong

  def apply[T: Show](redisHost: String, name: String, numEntries: Long, fpProb: Double) = {
    val width = optimalWidth(numEntries, fpProb)
    val numHashes = optimalNumHashes(numEntries, width)
    new RedisBloomFilterStore[T](name, width, numHashes, Redis.newRichClient(redisHost))
  }
}

