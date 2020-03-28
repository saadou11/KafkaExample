import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingKafka extends App {
  val BROKERS = "localhost:9092"
  val GROUPID = "group1"
  val TOPIC = "test"

  override def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("KafkaStreamExample")
    val sparkStreamContext = new StreamingContext(sparkConf, Seconds(15))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "something",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("test")
    val stream = KafkaUtils.createDirectStream[String, String](
      sparkStreamContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )


    val line = stream.map(_.value)
    val words = line.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)

    wordCounts.print()

    sparkStreamContext.start()
    sparkStreamContext.awaitTermination()

  }
}
