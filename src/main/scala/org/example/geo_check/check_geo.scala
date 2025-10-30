package org.example.geo_check

import org.apache.hadoop.fs.Path
import org.apache.spark.sql.functions.{col, count, desc, lit}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

class check_geo(spark:SparkSession){
  def load(ip_csv_path: String):DataFrame={
    spark.read.option("header", true).csv(ip_csv_path)
  }

  def transform(df: DataFrame):DataFrame={
    val df_ft_bang = df.filter(col("City-Name")==="bangalore" && col("Time").like("20250128%")).orderBy(col("Time").desc)


//    print(df_ft_bang.show())
    df_ft_bang
  }

  def write(dataFrame:DataFrame):Unit={
    val outputPath ="/Users/sindhupatchipulusu/IdeaProjects/spark_ETL/src/main/scala/org/example/geo_check/output/ip_total"
//    print(dataFrame.count())

//    dataFrame.coalesce(1)
//      .write
//      .mode(SaveMode.Overwrite)
//      .format("csv")
//      .option("header", true)
//      .option("quoteAll", true)
//      .save(outputPath)
//
//    val src = new Path(outputPath)
//    val fs = src.getFileSystem(spark.sparkContext.hadoopConfiguration)
//    val fileName = fs.globStatus(new Path(outputPath + "/part-*"))(0).getPath().getName()
//    fs.rename(new Path(outputPath + "/" + fileName), new Path(outputPath + "/ft_bang_28_ip_total.csv"))
  }
}
object check_geo extends App {
  val ip_csv = "/Users/sindhupatchipulusu/Downloads/log_level-data.csv"
//  val ip_csv = "/Users/sindhupatchipulusu/IdeaProjects/spark_ETL/src/main/scala/org/example/geo_check/output/ip/ft_bang_28_ip_1000.csv"
  val spark = SparkSession.builder()
    .appName("geo_check.com")
    .master("local")
    .getOrCreate()
  spark.conf.set("spark.sql.sources.partitionOverwriteMode", "dynamic")
  Logger.getLogger("org").setLevel(Level.ERROR)
  Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
  Logger.getLogger("org.apache.hadoop").setLevel(Level.ERROR)

  val check_geo = new check_geo(spark)
  val df_ft_data = check_geo.load(ip_csv)
  val df_ft_bang = check_geo.transform(df_ft_data)

  val temp = df_ft_bang.groupBy("City-Name", "State/Province-Name").agg(count(lit(1)))
  print(temp.show())

  check_geo.write(df_ft_bang.select("IP").dropDuplicates()) //14191
}
