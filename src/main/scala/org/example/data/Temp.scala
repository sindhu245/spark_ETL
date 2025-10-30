package org.example.data

import org.apache.spark.sql.catalyst.expressions.CurrentRow.nullable
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession, functions}
import org.example.testing_leadgen.csv_read_check.spark
import org.apache.spark.sql.functions.{broadcast, col, dense_rank, first, from_json, lit, lower, rank, row_number, to_date, udf, unbase64}
import org.apache.spark.sql.types.{MapType, StringType}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

import java.sql.Timestamp

class Temp {
  var content = List[String]()

  def change(): Unit = {
    content = List("a", "b", "c")
  }

  def check(): Unit = {
    println(content)
  }
  def addColumns(dataFrame: DataFrame, columns: Map[String, Any]): DataFrame = {
    columns.foldLeft(dataFrame) { case (df, (colName, colValue)) =>
      df.withColumn(colName, lit(colValue))
    }
  }

}

object Temp extends App{

  val spark = SparkSession.builder()
    .appName("blog.knoldus.com")
    .master("local")
    .getOrCreate()

  import spark.implicits._

  val adIdDf = Seq(
    ("1", "Bangalore", "1234","Bangalore", "1234", "432"),
    ("2", "California", null, "California", null, "123"),
    ("3", "Bangalore", "1234", "Bangalore", "1234", "456"),
    ("4", "California", null, "California", null, "CA123"),
    ("5", "India", "1234", "India", "1234", "432"),
    ("6", "California", null, "California", null, "567"),
    ("7", "", null, "", null, "")
  ).toDF("ad_id", "ad_name", "adset_name", "campaign_name", "adset_id", "campaign_id")
  def enrich_ad_id(mappedDf: DataFrame, abc: String, id_column: String): DataFrame={

    val df_schema = mappedDf.columns
    val enrich_columns = List("ad_name", "adset_name", "campaign_name", "adset_id", "campaign_id")
    val schema = df_schema ++ enrich_columns
    var col_mapping = Map.empty[String, String]
    var dataFrame = mappedDf

    for( enrich_column <- enrich_columns){
      val new_col_name = abc + "_" + enrich_column + "s"
      col_mapping = col_mapping + (enrich_column -> new_col_name)
    }

    dataFrame = dataFrame.withColumn("id_split", functions.split(col(id_column), ","))
    println("printing data after split")
    dataFrame.show()

    dataFrame = dataFrame.withColumn("id", explode_outer(col("id_split")))
    println("printing data after split")
    dataFrame.show()

    dataFrame = dataFrame.join(adIdDf, dataFrame("id") === adIdDf("ad_id"), "left").select(schema.map(m=>col(m)):_*).na.fill("")
      .groupBy(df_schema.map(m=>col(m)):_*)
      .agg(concat_ws(",", collect_list(col("ad_name"))).as(col_mapping("ad_name")),
        concat_ws(",", collect_list(col("adset_name"))).as(col_mapping("adset_name")),
        concat_ws(",", collect_list(col("campaign_name"))).as(col_mapping("campaign_name")),
        concat_ws(",", collect_list(col("adset_id"))).as(col_mapping("adset_id")),
        concat_ws(",", collect_list(col("campaign_id"))).as(col_mapping("campaign_id")))
    dataFrame
  }
  def testing(sample: DataFrame) {
    sample.show()
//    println("------------------------->  "+ a, b)
//    val df: DataFrame = sample.filter(col("client")=== a && col("adv")=== b)
//    df.show()
    // Define a UDF to decode Base64-encoded string
    val decodeBase64UDF = udf((encoded: String) => new String(java.util.Base64.getDecoder.decode(encoded)))

    // Decode the Base64-encoded column
    val decodedDF = sample.withColumn("decoded_data", decodeBase64UDF(col("leadGen")))
    decodedDF.show(false)

    // Explode each key into separate columns
//    val explodedDF = decodedDF
//      .withColumn("json_struct", from_json(col("decoded_data"), MapType(StringType, StringType)))
//      .selectExpr("client", "adv", "explode(json_struct) as (key, value)")
//      .groupBy("client", "adv")
//      .pivot("key")
//      .agg(first("value"))
//
//    explodedDF.show(false)
//    println(a + "   " + b)
  }
//for(i <- 1 to 10) {
//  var current_time = System.currentTimeMillis()
//  //  println(new )
//
//  var temp: Long = 0
//  if (i >= 5) {
//    temp = current_time
//  }
//  else {
//    println()
//    temp = current_time + (2 * 365 * 24 * 60 * 60 * 1000)
//  }
//
//  var hello = current_time
//
//
//  def epochToDate(epochMillis: Long): String = {
//    val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
//    df.format(epochMillis)
//  }
//
//  //  println("-----"+ epochToDate(hello)+ "------------"+epochToDate(temp)+"-----"+epochToDate(1753092440626L))
//  ////  +"\n")
//  //  println(63072000000L+1690020440626L)
//}

//  val cDateTime: LocalDateTime = LocalDateTime.now
//  println(cDateTime)
//  val nextyear = LocalDateTime.now.plusYears(2)
//  println(LocalDateTime.parse(nextyear.toString).isAfter(cDateTime))

//
//  val ctimestamp = Timestamp.valueOf(cDateTime)
//  val nextTime = Timestamp.valueOf(nextyear)
////  println(nextTime-ctimestamp)
//println(LocalDateTime.now)
//  println(Timestamp.valueOf(nextyear))




////  println(Long.valueOf(nextyear))
//  val obj = new loadData()
//    val data3 = obj.readDataframe("dataset_3")
//  println("\n"+data3.agg(sum("is_expired")).first.get(0))


//println("2025-07-22T17:43:16.328" > "2023-07-22T17:43:16.328")
//
//  val list: List[Int] = List(1,2,3,4,5)
//  println(list.find(_>3))



  //  creating the employee directory which is bigger dataframe
  val employeeDF = Seq(
    ("Amit", "Bangalore", "1234"),
    ("Ankit", "California", null),
    ("Abdul", "Pune", "125830948230"),
    ("Sumit", "California", null),
    ("Riya", "Pune", "234234")
  ).toDF("first_name", "city", "num")

  val employeeDF_1 = Seq(
    ("Amit", "Bangalore", "1234"),
    ("Ankit", "California", null),
    ("Abdul", "Pune", "125830948230"),
    ("Sumit", "California", null),
    ("Riya", "Pune", "234234")
  ).toDF("first_name", "city", "num")

  val check_data = employeeDF.union(employeeDF_1)
  check_data.show()

//  .toDF("ad_name", "adset_name", "campaign_name")

  val testdf = employeeDF.withColumn("num", col("num").cast(LongType))
    .withColumn("num", coalesce(col("num"), lit(0)))
//  testdf.show()
//  println(testdf.schema)

  var mapped_df = Seq(
    ("7,8,9,10", "Bangalore", "1234", "5,6,7"),
    ("3,5", "California", null, "6,2"),
    (null,"temp_null", "test_null",null),
    ("","test_empty", "test_empty","")
  ).toDF("inline_ad_ids", "city", "num", "buffet_ad_ids")

  mapped_df = enrich_ad_id(mapped_df, "inline", "inline_ad_ids")
  mapped_df = enrich_ad_id(mapped_df, "buffet", "buffet_ad_ids")
//  mapped_df.show(false)

  val enrich_columns = List("ad_name", "adset_name", "campaign_name", "adset_id", "campaign_id")
//  val schema = df_schema ++ enrich_columns
  var col_mapping = Map.empty[String, String]
  for( enrich_column <- enrich_columns){
    val new_col_name = "inline" + "_" + enrich_column + "s"
    col_mapping = col_mapping + (enrich_column -> new_col_name)
  }
//  println(col_mapping("ad_name"))

  val enrich_needed = "1"
  println(enrich_needed == 1)
  println(enrich_needed == "1")
  if(enrich_needed == 1){
    println("i need to do the enrichment")
  }
  employeeDF.show()

//  val base64Df = Seq(
//    ("Audi", "adv1", "ewogICJnZW5kZXIiIDogIm1hbGUiLAogICJuYW1lIiA6ICJqYXNvbiBzY2VtYSIsCiAgInBob25lX251bWJlciIgOiA5ODc4OTM1NDc4LAogICJwaW5fY29kZSIgOiAxMTAwMDEsCiAgImRhdGUiIDogMTY0ODQ0NTU2NTM0LAogICJkZXNjcmlwdGlvbiIgOiAiTG9yZW0gaXBzdW0gbG9uZyB0ZXh0IiwKICAiY2l0eSIgOiAiTHVja25vdyIsCiAgInN0YXRlIiA6ICJVdHRhciBQcmFkZXNoIiwKICAidGVzdF9kcml2ZV9jYXIiIDogIkF1ZGkgNDQwcyIsCiAgInByZWZlcnJlZF9wYXltZW50X21ldGhvZHMiIDogWwogICAgInVwaSIsCiAgICAiY3JlZGl0X2NhcmQiCiAgXQp9")
////    ,
////    ("Audi", "adv2", "ewogICJnZW5kZXIiIDogIm1hbGUiLAogICJuYW1lIiA6ICJqYXNvbiBzY2VtYSIsCiAgInBob25lX251bWJlciIgOiA5ODc4OTM1NDc4LAogICJwaW5fY29kZSIgOiAxMTAwMDEsCiAgImRhdGUiIDogMTY0ODQ0NTU2NTM0LAogICJkZXNjcmlwdGlvbiIgOiAiTG9yZW0gaXBzdW0gbG9uZyB0ZXh0IiwKICAidGVzdF9kcml2ZV9jYXIiIDogIkF1ZGkgNDQwcyIsCiAgInByZWZlcnJlZF9wYXltZW50X21ldGhvZHMiIDogWwogICAgInVwaSIsCiAgICAiY3JlZGl0X2NhcmQiCiAgXQp9"),
////    ("Audi", "adv1", "ewogICJnZW5kZXIiIDogIm1hbGUiLAogICJuYW1lIiA6ICJqYXNvbiBzY2VtYSIsCiAgInBob25lX251bWJlciIgOiA5ODc4OTM1NDc4LAogICJwaW5fY29kZSIgOiAxMTAwMDEsCiAgImRhdGUiIDogMTY0ODQ0NTU2NTM0LAogICJkZXNjcmlwdGlvbiIgOiAiTG9yZW0gaXBzdW0gbG9uZyB0ZXh0IiwKICAiY2l0eSIgOiAiTHVja25vdyIsCiAgInN0YXRlIiA6ICJVdHRhciBQcmFkZXNoIiwKICAidGVzdF9kcml2ZV9jYXIiIDogIkF1ZGkgNDQwcyIsCiAgInByZWZlcnJlZF9wYXltZW50X21ldGhvZHMiIDogWwogICAgInVwaSIsCiAgICAiY3JlZGl0X2NhcmQiCiAgXQ")
//  ).toDF("client", "adv", "leadGen")
//
////  val temp = base64Df.withColumn("decode", decode(col("leadGen"), "UTF-8"))
////    .cast(MapType(StringType, StringType)
//  var temp = base64Df.withColumn("decode", unbase64(col("leadGen")).cast("string"))
//  temp.show(false)
////  val schema = schema_of_json(lit(temp.select($"decode").as[String].first))
//  temp = temp.withColumn("Notes",from_json(col("decode"),MapType(StringType,StringType)))
////  temp.select(col("client"), col("Notes")("gender")).show()
////  temp.show(false)
//  val allKeys = temp.select(explode(map_keys(col("Notes"))))
//  temp.selectExpr("Notes.*").show(false)
  // Dynamically create columns for each key
//  val resultDF = allKeys.foldLeft(df)((tempDF, key) => tempDF.withColumn(key, col("map_column")(key)))


  //  val schema = schema_of_json(lit(temp.select($"Notes").as[String].first))
//  val dfresult = temp.withColumn("jsonColumn", explode(from_json(col("decode"), MapType(StringType, StringType))))
//  dfresult.show(false)
//    .select($"id", $"name", $"jsonColumn.*")
//  dfresult.show(false)

//  val sample = base64Df.withColumn("rank_row", rank()
//    .over(Window.partitionBy("client","adv").orderBy(col("leadGen").desc)))
//  sample.show()
//  testing(sample.filter(col("rank_row")===1))

  //  creating the citiesDf which is small df that will be broadcasted
  var citiesDF = Seq(
    ("15K-20K", "Usa"),
    ("Bangalore", "india"),
    ("Pune", "India V2"),
    ("Pune V2", "India"),
    ("Bangalore V2", "india")
  ).toDF("city", "name")

  val map_cluster = citiesDF//.select(explode($"cityList").as("city"), $"name")
    .filter(!col("name").endsWith("V2"))
    .select(regexp_replace(upper($"city")," ","_"),$"name")
    .as[(String, String)].collect().toMap

  print(map_cluster)

  //  Now we will perform the join operation on employeeDF with broadcasted citiesDF

//  var joinedDf = employeeDF.join(broadcast(citiesDF), employeeDF.col("city") === citiesDF.col("city"))
//
//  //  Now we will drop the city column from citiesDF as we don't want to keep duplicate column
//  joinedDf = joinedDf.drop(citiesDF.col("city"))
//
//  //  Finally we will see the joinedDF
//  joinedDf.show()
//
//  private var joinedDf1 = employeeDF.join(citiesDF, employeeDF.col("city") === citiesDF.col("city"))
//  joinedDf1 = joinedDf1.drop(citiesDF.col("city"))
//  joinedDf1.show()

//  var df = spark.read.parquet("s3a://hotstar-ads-targeting-us-east-1-prod/trackers/advertiser_reporting/qc/hourly/cd=2023-10-04/hr=13/")

//  df = df.withColumn("blaze_impression_percentage_match", ((col("advertiser_blaze_impression")).divide(col("data_lake_blaze_impression"))))

//  df.show()
  citiesDF = citiesDF.withColumn("temp", lower(col("city")))
//  temp = lower(temp)
//  print(citiesDF.head)

  citiesDF = citiesDF
    .withColumnRenamed("city", "metro")
    .withColumn("country", col("temp"))
//  citiesDF.show()

//  val df = spark.read.option("header", true).option("inferSchema", "true").csv("/Users/sindhu.patchipulusu/IdeaProjects/spark-etl/src/main/scala/org/example/testing/internal/advertiser=Apple/ad_id=AD12345/cd=2024-02-26/").schema
////  df.show()
//  for (i <- df){
//    println(i)
//  }
//  val data = spark.read.schema(df).option("header", true).csv("/Users/sindhu.patchipulusu/IdeaProjects/spark-etl/src/main/scala/org/example/testing/internal/advertiser=Apple/ad_id=AD12345/cd=2024-02-26/")
//  data.show()
//    .option("header", true).option("mergeSchema", true)


  // Define schema for first dataset
  val schema1 = StructType(Seq(
    StructField("col1", StringType, nullable = true),
    StructField("col2", StringType, nullable = true)
  ))

  // Define schema for second dataset
  val schema2 = StructType(Seq(
    StructField("col1", StringType, nullable = true),
    StructField("col3", StringType, nullable = true)
  ))

  // Merge schemas to create combined schema
  val combinedSchema = StructType(schema1.fields ++ schema2.fields.filterNot(_.name == "col1"))


  val test_if = Seq(
    ("Amit", "Bangalore", "1234",1),
    ("Ankit", "California", null,4),
    ("Abdul", "Pune", "125830948230",3),
    ("Sumit", "California", null,0),
    ("Riya", "Pune", "234234",4)
  ).toDF("id", "city", "name", "check_number")
  println(test_if.columns)
//  test_if.show



  val dump_sample_report = Seq(
    ("temp","1","D22960","Dazller","100","105920","ICC Womens T20 WC 2024","test_id","Squeeze_Ups",10,"ROS","Spot",50,62,10,0,2995427),
    ("temp","2","D22960","Dazller","101","105923","ICC Womens T20 WC 2024","test_id","Midroll_Mobile_CPM",20,"ROS","CPM",0,0,5,8333333,8351817),
    ("temp","3","D22960","Dazller","102","105922","ICC Womens T20 WC 2024","test_id","Midroll_CTV_CPM",10,"ROS","CPM",0,0,2,4166667,4172244)
  ).toDF("Advertiser_Name","Advertiser_ID","Deal_Number","Brand","Brand_ID","Line_Item_ID","Tournament_Name","Tournament_ID","Ad_Format","Ad_Length","Targeting","Buy_Type","Planned_Quantity","Delivered_Quantity","Crashed_Quantity","Planned_Impressions","Delivered_Impressions")

  val dump_sample_report_1 = Seq(
    ("temp","1","D22960","Dazller","100","105920","ICC Womens T20 WC 2024","test_id","Squeeze_Ups",10,"ROS","Spot",50,62,10,0,2995427),
    ("temp","2","D22960","Dazller","101","105923","ICC Womens T20 WC 2024","test_id","Midroll_Mobile_CPM",20,"ROS","CPM",0,0,5,8333333,8351817),
    ("temp","3","D22960","Dazller","102","105922","ICC Womens T20 WC 2024","test_id","Midroll_CTV_CPM",10,"ROS","CPM",0,0,2,4166667,4172244)
  ).toDF("Advertiser_Name","Advertiser_ID","Deal_Number","Brand","Brand_ID","Line_Item_ID","Tournament_Name","Tournament_ID","Ad_Format","Ad_Length","Targeting","Buy_Type","Planned_Quantity","Delivered_Quantity","Crashed_Quantity","Planned_Impressions","Delivered_Impressions")

  print(dump_sample_report.union(dump_sample_report_1).dropDuplicates().show())
  //  println(dump_sample_report.columns)
//  println(dump_sample_report.show)


  val finalDf = Seq(
    ("Capp_version_test_1.1.1.1", "ANDROI", "null"), ("Capp_version_test_1.1.1.2", "temp_IOS_dsdfg", "null")
  ).toDF("targeting_tag", "Platform", "id")

//  val idColumn = if(test_if.columns.contains("id")) Option("id") else Option.empty

//  println(idColumn.nonEmpty)

  finalDf.filter(isnull(col("id"))).show()

  finalDf.filter(isnull(col("id"))).select("targeting_tag").show
//  val dfToBeInserted = if(idColumn.nonEmpty) {
//    finalDf.filter(isnull(col("id"))).select("targeting_tag").collect().toList
//  } else {
//    List()
//  }
//  println(dfToBeInserted)


  // Read data using combined schema
//  val data = spark.read.schema(combinedSchema).option("header", true).option("sep", ",").csv("/Users/sindhu.patchipulusu/IdeaProjects/spark-etl/src/main/scala/org/example/testing/sam/")

  // Show the data
//  data.show()

  def addColumndata(dataFrame: DataFrame): DataFrame = {
    dataFrame.withColumn("new_column", lit("default_value"))
  }

  val platformList = List("temp", "check")
  val temp = new Temp()
  val liveEmptyColumnsToAdd = Map("state" -> "", "metro" -> "", "gender" -> "", "age" -> "", "nccs" -> "", "clicks" -> 0)
  val see = temp.addColumns(finalDf, liveEmptyColumnsToAdd).filter(!col("Platform").isin(platformList: _*))
  see.show()

  val times: Timestamp = Timestamp.valueOf("2025-04-11 00:00:00")
  val check = see.withColumn("ad_format",
      when(lower(col("Platform")).contains("roid").or(lower(col("Platform")).contains("ios")), "VIDEO")
        .otherwise("DISPLAY")
    )
    .withColumn("cd", lit(times))


  var see_2 = check
    .withColumn("check_date", to_date(col("cd"), "yyyy-MM-dd"))
  see_2.show(false)
  see_2 = addColumndata(see_2)
  see_2.show(false)
//  see_2.printSchema()

//  check.show()
  val see_1 = see_2.filter(col("check_date")=== lit("2025-04-11"))
    .withColumn("cehck_col", col("check_date"))
    .withColumn("platform-a", finalDf.col("targeting_tag"))
//    .withColumn("cehck_col", col("Platform"))
  val selectColumns = List("metro", "clicks")
//  val see_1 = see.select(selectColumns.map(col): _*)
//  see_1.show()
//  temp.change()
//  temp.check()

  private def unionByColumns(dfs: Seq[DataFrame]): DataFrame = {
    val nonEmptyDfs = dfs.filterNot(_.head(1).isEmpty)
    if (nonEmptyDfs.isEmpty) {
      println("All DataFrames are empty. Returning an empty DataFrame with a single column 'adv_id'." + " size = "+ nonEmptyDfs.size)
      spark.createDataFrame(spark.emptyDataFrame.rdd, StructType.fromDDL("adv_id STRING"))
    } else {
      nonEmptyDfs.reduce((df1, df2) => df1.unionByName(df2))
    }
  }

  val empty_adv_id_df = spark.emptyDataFrame //spark.createDataFrame(spark.emptyDataFrame.rdd, StructType.fromDDL("adv_id STRING"))

  val empty_adv_id_df1 = spark.createDataFrame(spark.emptyDataFrame.rdd, StructType.fromDDL("adv_id STRING"))

  val non_empty_adv_id_df = Seq(("1"), ("2"), ("3")).toDF("adv_id")
  val adLengthDf = Seq((Some(10),"as"), (None,"df"), (Some(20), "df"), (None, null), (Some(30),null)).toDF("ad_length", "name")
    .withColumn("ad_length", col("ad_length").cast(IntegerType))
  println("initial length df - "+ adLengthDf.show())

  val filledAdLengthDf = adLengthDf.na.fill(Map("ad_length" -> 1))
  println("final length df - "+ filledAdLengthDf.show())

//  i have an df with ad_length column with int datatype and few null values in it. And want to fill na with 1. Give me the code for it.
//  i want to test it. -
  //val filledAdLengthDf = adLengthDf.na.fill(Map("ad_length" -> 1))
  //
  //So give me df with ad_length of int, null values and will run and check the above code.

  print(empty_adv_id_df.show())
  empty_adv_id_df.printSchema()
//  print(empty_adv_id_df1.unionByName(empty_adv_id_df).show())

//  val agg = empty_adv_id_df1.unionByName(empty_adv_id_df).agg(countDistinct("adv_id").as("reach")).first().getAs[Long]("reach")
  val combine_df = unionByColumns(Seq(empty_adv_id_df, empty_adv_id_df1))
  println("combined df", combine_df.show())
//  println(agg)
}

