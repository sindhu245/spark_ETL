package org.example.testing_leadgen

import java.util.Base64
import java.nio.charset.StandardCharsets
import org.apache.spark.sql.{DataFrame, SparkSession, functions}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, concat, dense_rank, explode, first, from_json, lit, map_keys, min, monotonically_increasing_id, rank, unbase64}
import org.apache.spark.sql.types.{MapType, StringType}

import scala.io.Source

class jsonBase64(){
  val cteJson = "/Users/sindhu.patchipulusu/IdeaProjects/spark-etl/src/main/scala/org/example/testing/cte.json"
  def load(spark: SparkSession): DataFrame={
    spark.read.json(cteJson)
  }
  def transform(): Unit = {
    val jsonData = Source.fromFile(cteJson).mkString

//read map, convert it required mapping. make a string with

    // Convert JSON data to Base64
    val base64String = Base64.getEncoder.encodeToString(jsonData.getBytes(StandardCharsets.UTF_8))

    // Print or use the Base64-encoded string
    println("Base64 Encoded JSON:")
    println(base64String)
  }


}

object jsonBase64 extends App {
  val spark = SparkSession.builder()
    .appName("blog.knoldus.com")
    .master("local")
    .getOrCreate()
  val objVar = new jsonBase64()
  var df = objVar.load(spark)

  def base643(df: DataFrame) {

    var temp = df.withColumn("decode", unbase64(col("leadGen")).cast("string"))

    temp = temp.withColumn("Notes", from_json(col("decode"), MapType(StringType, StringType)))
    val allKeys = temp.limit(1).select(explode(map_keys(col("Notes"))))
    println(allKeys.count())

    allKeys.collect().foreach(row =>
      temp = temp.withColumn(row.mkString, col("Notes")(row.mkString))
    )
    //    temp.select(col("client"), col("Notes")("gender")).show()
    temp = temp.drop("Notes", "decode", "leadGen")
    temp.show(false)

    //  val explodedDF = temp
    //    .selectExpr("client", "adv", "explode(Notes) as (key, value)")
    //    .groupBy("client","adv")
    //    .pivot("key")
    //    .agg(first("value"))
    //
    //  explodedDF.show(false)

//    write(temp)
  }

  def write(dataFrame: DataFrame)={
    dataFrame.write
      .format("csv")
      .option("quoteAll", true)
      .option("header", true)
      .save("myFile.csv")
  }

  import spark.implicits._

  var df_tmp = Seq(
    ("Audi", "adv1", "eyJwaG9uZV9udW1iZXIiOjEyMzQ1Njc4OTAsImVtYWlsIjoiSm9obi53aWNrQGdtYWlsLmNvbSJ9")
//    ("Audi", "adv2", "ewogICJnZW5kZXIiIDogImZlbWFsZSIsCiAgIm5hbWUiIDogInNpbmRodSIsCiAgInBob25lX251bWJlciIgOiA5ODc4OTM1NDc4LAogICJwaW5fY29kZSIgOiAxMTAwMDEsCiAgImRhdGUiIDogMTY0ODQ0NTU2NTM0LAogICJjaXR5IiA6ICJwb25udXIiLAogICJzdGF0ZSIgOiAiQW5kaHJhIFByYWRlc2giLAogICJkZXNjcmlwdGlvbiIgOiAiTG9yZW0gaXBzdW0gbG9uZyB0ZXh0IiwKICAidGVzdF9kcml2ZV9jYXIiIDogIkF1ZGkgMjAwMHMiLAogICJwcmVmZXJyZWRfcGF5bWVudF9tZXRob2RzIiA6IFsKICAgICJ1cGkiLAogICAgImNyZWRpdF9jYXJkIgogIF0KfQ=="),
//    ("Audi", "adv1", "ewogICJnZW5kZXIiIDogImZlbWFsZSIsCiAgIm5hbWUiIDogInNpbmRodSIsCiAgInBob25lX251bWJlciIgOiA5ODc4OTM1NDc4LAogICJwaW5fY29kZSIgOiAxMTAwMDEsCiAgImRhdGUiIDogMTY0ODQ0NTU2NTM0LAogICJjaXR5IiA6ICJwb25udXIiLAogICJzdGF0ZSIgOiAiQW5kaHJhIFByYWRlc2giLAogICJkZXNjcmlwdGlvbiIgOiAiTG9yZW0gaXBzdW0gbG9uZyB0ZXh0IiwKICAidGVzdF9kcml2ZV9jYXIiIDogIkF1ZGkgMjAwMHMiLAogICJwcmVmZXJyZWRfcGF5bWVudF9tZXRob2RzIiA6IFsKICAgICJ1cGkiLAogICAgImNyZWRpdF9jYXJkIgogIF0KfQ==")
//    ,
//    ("Audi", "adv3", "ewogICJnZW5kZXIiIDogIm1hbGUiLAogICJuYW1lIiA6ICJqYXNvbiBzY2VtYSIsCiAgInBob25lX251bWJlciIgOiA5ODc4OTM1NDc4LAogICJwaW5fY29kZSIgOiAxMTAwMDEsCiAgImRhdGUiIDogMTY0ODQ0NTU2NTM0LAogICJkZXNjcmlwdGlvbiIgOiAiTG9yZW0gaXBzdW0gbG9uZyB0ZXh0IiwKICAiY2l0eSIgOiAiTHVja25vdyIsCiAgInN0YXRlIiA6ICJVdHRhciBQcmFkZXNoIiwKICAidGVzdF9kcml2ZV9jYXIiIDogIkF1ZGkgNDQwcyIsCiAgInByZWZlcnJlZF9wYXltZW50X21ldGhvZHMiIDogWwogICAgInVwaSIsCiAgICAiY3JlZGl0X2NhcmQiCiAgXQp9")
      ).toDF("client", "adv", "leadGen")
//  df = df.withColumn("id", monotonically_increasing_id())
//    df = df.join(
//      df.groupBy("client", "adv").agg(min("id").alias("min_id")), Seq("client", "adv")
//    ).withColumn("rank_row", dense_rank().over(Window.orderBy("min_id"))).drop("min_id", "id")

  df = df.withColumn("unique_id", concat(col("client"), concat(lit("_"),col("adv"))))
  df.show()
  private var df_adId = df
  val distinctAdId = df.select("unique_id").distinct()

  if (!distinctAdId.collect().isEmpty) {
      distinctAdId.collect().foreach(row => {
      val a = row.mkString;
      val b = a.split("_");
      val temp_base = df.filter(col("client") === b(0) && col("adv") === b(1))
      base643(temp_base)
    })
  }

//  df.show()
}
