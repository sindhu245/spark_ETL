package org.example.testing_leadgen

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{coalesce, col, lit}
import org.example.testing_leadgen.csv_read_check.spark

import scala.util.Try

class csv_read_check {
}
object csv_read_check extends App{
  val spark = SparkSession.builder()
    .appName("blog.knoldus.com")
    .master("local")
    .getOrCreate()
  val df = spark.read.option("header", true).format("csv").load("/Users/sindhu.patchipulusu/IdeaProjects/spark-etl/src/main/scala/org/example/testing/output/advertiser=Apple/ad_id=AD12345/cd=2024-02-26/")
  val df_audi = spark.read.option("header", true).format("csv").load("/Users/sindhu.patchipulusu/IdeaProjects/spark-etl/src/main/scala/org/example/testing/output/advertiser=Audi/ad_id=AD456/cd=2024-02-26/")
  val df_nestle = spark.read.option("header", true).format("csv").load("/Users/sindhu.patchipulusu/IdeaProjects/spark-etl/src/main/scala/org/example/testing/output/advertiser=Nestle/ad_id=AD789/cd=2024-02-26/")
  println(df.count(), "   ", df_audi.count(), "   ", df_nestle.count())
  df.show()
  df_audi.show()
  df_nestle.show(false)
}