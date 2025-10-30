//package org.example.testing
//
//import com.hotstar.ads_data.driver.advertiser.{AdvertiserHourlySummaryBlazeDriver, AdvertiserHourlySummaryShifuDriver}
//import com.hotstar.ads_data.driver.advertiser.common.AdvertiserLiveShifuUtils.{addEligibleContentId, getEligibleTournamentId, getMatchDetailsForGlobalAdFormat}
//import com.hotstar.ads_data.driver.advertiser.vod_targeting.AdvertiserTargetingBlazeUtils.dataExtractionUtils
//import org.apache.spark.sql.{DataFrame, SparkSession}
//import org.apache.spark.sql.functions.{coalesce, col, lit, sum, when}
//import org.apache.spark.sql.types.LongType
//
//object tempTransformUtils { //AdvertiserAggTransformUtils {
//
//  def percentageMatch(df: DataFrame, actual: String, expected: String, newCol: String): DataFrame = {
//    df.withColumn(newCol,
//      when(col(expected) =!= 0,
//        lit(100) * (col(actual).cast("double") / col(expected))
//      ).otherwise(100)
//    )
//  }
//
//  def checkForEmptyDataframe(df: DataFrame, context: SparkSession): DataFrame = {
//    if (df.head(1).isEmpty) {
//      println("DataFrame is empty.")
//      context.emptyDataFrame
//        .withColumn("impressions", lit(0).cast(LongType))
//        .withColumn("clicks", lit(0).cast(LongType))
//    } else {
//      println("DataFrame is not empty.")
//      df
//    }
//  }
//
//  def fetchRawShifuData(mode:String, cd:String, shifu_raw_inputPath:String, context: SparkSession):DataFrame={
//    var datePath: String = ""
//    var summaryDF: DataFrame = context.emptyDataFrame
//
//    if (mode.equalsIgnoreCase("daily")) {
//      datePath = "cd=" + cd + "/"
//      summaryDF = dataExtractionUtils.nonPaddedReadDateRangeParquetData(shifu_raw_inputPath, cd, context)
//    }
//    summaryDF
//  }
//
//  def getAdvertiserLiveShifuAggTransformDf(advertiserHourlySummaryShifuDriver: AdvertiserHourlySummaryShifuDriver, shifuDf: DataFrame): DataFrame ={
//    val blazeContentId = advertiserHourlySummaryShifuDriver.getLiveContentDetails()
//    // removing ENT campaigns
//    var shifuTransformedDf = advertiserHourlySummaryShifuDriver.enrichDataFrame(shifuDf, "ad_set") //Fetch the stream_category and tournament_id details
//      .withColumnRenamed("tournament_id", "tournament_id_dms")
//      .filter(col("stream_category") =!= "ENTERTAINMENT")
//      .withColumn("content_id", addEligibleContentId(blazeContentId, col("ad_placement")))
//
//    shifuTransformedDf = advertiserHourlySummaryShifuDriver.enrichDataFrame(shifuTransformedDf, "advertiser_details")
//    shifuTransformedDf = advertiserHourlySummaryShifuDriver.enrichDataFrame(shifuTransformedDf, "brand_details")
//    shifuTransformedDf = advertiserHourlySummaryShifuDriver.enrichDataFrame(shifuTransformedDf, "match_details")
//      .withColumnRenamed("tournament_id", "tournament_id_blaze")
//
//    shifuTransformedDf = getEligibleTournamentId(shifuTransformedDf)
//      .withColumn("tournament_id", col("tournament_id").cast(LongType))
//      .drop("tournament_id_dms", "tournament_id_blaze")
//
//    shifuTransformedDf = getMatchDetailsForGlobalAdFormat(shifuTransformedDf)
//
//    shifuTransformedDf = shifuTransformedDf
//      .filter(col("match_id").isNotNull)
//      .filter(col("tournament_id").isNotNull)
//      .filter(col("advertiser_id").isNotNull)
//      .filter(col("brand_id").isNotNull)
//      .agg(
//        sum("impressions").alias("impressions"),
//        sum("clicks").alias("clicks"))
//
//    println("shifu transformation end")
//    shifuTransformedDf.printSchema()
//    shifuTransformedDf
//  }
//
//  def getAdvertiserLiveBlazeAggTransformDf(advertiserHourlySummaryBlazeDriver: AdvertiserHourlySummaryBlazeDriver, blazeDf: DataFrame): DataFrame ={
//    var blazeTransformedDf = blazeDf
//      .filter(col("creative_type") === "SSAI"
//        || col("creative_type") === "Spot"
//        || col("creative_type") === "SOV"
//        || col("creative_type") === "LBand"
//        || col("creative_type") === "Aston")
//
//    if (!blazeTransformedDf.columns.contains("impressions")) {
//      blazeTransformedDf = blazeTransformedDf.withColumn("impressions", lit(0).cast(LongType))
//    }
//    if (!blazeTransformedDf.columns.contains("clicks")) {
//      blazeTransformedDf = blazeTransformedDf.withColumn("clicks", lit(0).cast(LongType))
//    }
//    blazeTransformedDf = blazeTransformedDf
//      .withColumn("impressions_count", coalesce(col("impressions"), lit(0).cast(LongType)))
//      .withColumn("clicks_count", coalesce(col("clicks"), lit(0).cast(LongType)))
//
//    blazeTransformedDf = advertiserHourlySummaryBlazeDriver.enrichDataFrame(blazeTransformedDf, "meta_details")
//    blazeTransformedDf = blazeTransformedDf
//      .filter(col("advertiser_id").isNotNull)
//      .filter(col("brand_id").isNotNull)
//      .agg(
//        sum("impressions_count").alias("impressions"),
//        sum("clicks_count").alias("clicks"))
//
//    blazeTransformedDf
//  }
//
//  def getLbandTransformDf:DataFrame={
//
//    //lband data transformation logic
//    SparkSession.builder().getOrCreate().emptyDataFrame
//  }
//}
//
//
//
//
//import org.apache.spark.sql.DataFrame
//
//trait Try_adv_qc {
//  def loadData(path: String): Seq[DataFrame]
//  def transform(df: Seq[DataFrame]): Seq[DataFrame]
//  def aggregate(df: Seq[DataFrame]): Long
//  def compare(actual: Long, expected: Long, tolerancePercent: Double = 0.0): Boolean
//  def logResult(name: String, actual: Long, expected: Long, passed: Boolean): Unit
//}
