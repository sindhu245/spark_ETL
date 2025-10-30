package org.example.geo_check

import org.apache.hadoop.fs.Path
import org.apache.spark.sql.functions.{col, count, desc, lit}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

class check_neilsen_pid(spark:SparkSession){
  def load(ip_csv_path: String):DataFrame={
    spark.read.option("header", true).csv(ip_csv_path)
  }

  def transform(df: DataFrame):DataFrame={
    val df_neilsen_unmatched = df.filter(col("match_flag")==="unmatched")
    df_neilsen_unmatched
  }

  def write(dataFrame:DataFrame):Unit={
    val outputPath ="/Users/sindhupatchipulusu/IdeaProjects/spark_ETL/src/main/scala/org/example/geo_check/output/pid_total_male_35"
    print(dataFrame.count())

        dataFrame.coalesce(1)
          .write
          .mode(SaveMode.Overwrite)
          .format("csv")
          .option("header", true)
          .option("quoteAll", true)
          .save(outputPath)

        val src = new Path(outputPath)
        val fs = src.getFileSystem(spark.sparkContext.hadoopConfiguration)
        val fileName = fs.globStatus(new Path(outputPath + "/part-*"))(0).getPath().getName()
        fs.rename(new Path(outputPath + "/" + fileName), new Path(outputPath + "/neilsen_total_pid_male_35minus.csv"))
  }
}
object check_neilsen_pid extends App {
  val neilsen_pid_csv = "/Users/sindhupatchipulusu/Downloads/match_profile_check_nlsn668037.csv"
  //  val ip_csv = "/Users/sindhupatchipulusu/IdeaProjects/spark_ETL/src/main/scala/org/example/geo_check/output/ip/ft_bang_28_ip_1000.csv"
  val spark = SparkSession.builder()
    .appName("pid_extract.com")
    .master("local")
    .getOrCreate()
  spark.conf.set("spark.sql.sources.partitionOverwriteMode", "dynamic")
  Logger.getLogger("org").setLevel(Level.ERROR)
  Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
  Logger.getLogger("org.apache.hadoop").setLevel(Level.ERROR)

  val check_neilsen_pid = new check_neilsen_pid(spark)
  val df_neilsen_data = check_neilsen_pid.load(neilsen_pid_csv)
  val df_ft_bang = check_neilsen_pid.transform(df_neilsen_data)

  println("Total pid's - "+ df_ft_bang.count()) //3634837

  check_neilsen_pid.write(df_ft_bang.select("publisher_id").dropDuplicates())
}
