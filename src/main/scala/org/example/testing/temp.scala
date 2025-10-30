//import com.hotstar.ads_data.config.ApplicationProperties
//import com.hotstar.ads_data.driver.advertiser.LbandAstonArBug.LbandAstonArBugImpressionDriver
//import com.hotstar.ads_data.driver.advertiser.common.AdvertiserCommonUtils
//import com.hotstar.ads_data.driver.advertiser.{AdvertiserHourlySummaryBlazeDriver, AdvertiserHourlySummaryShifuDriver}
//import com.hotstar.ads_data.driver.qc.advertiser_reporting.tempTransformUtils.{fetchRawShifuData, getAdvertiserLiveBlazeAggTransformDf, getAdvertiserLiveShifuAggTransformDf}
//import com.hotstar.ads_data.utils.SparkUtil
//import com.typesafe.config.Config
//import org.apache.log4j.Logger
//import org.apache.spark.sql.functions.{col, count, lit, sum}
//import org.apache.spark.sql.types.LongType
//import org.apache.spark.sql.{DataFrame, SparkSession}
//
//import java.time.{LocalDate, ZoneOffset}
//import java.time.format.DateTimeFormatter
//
////should i need to make it an other type? This needs to be an common check class. ideally how should i keep and class or trait or how?
//
//////  TODO: compare function -> map<string, agg<1,2>, diff_thresold, diff_percentage>
//class AdvertiserAggQcDriver(config: Config, context: SparkSession, datePath: String){
//  val logger: Logger = Logger.getLogger(getClass.getName)
//  private val balzeSource = "blaze"
//  private val shifuSource = "shifu"
//  val cd: String = System.getProperty("cd")
//  val mode: String = System.getProperty("mode", "hourly")
//  private val qcDiffPercentageThreshold = config.getDouble("qc.qc_diff_percentage_threshold")
//  private val qcDiff = config.getLong("qc.qc_diff_threshold")
//
//  private val advertiserHourlySummaryBlazeDriver = new AdvertiserHourlySummaryBlazeDriver(balzeSource, config: Config, context: SparkSession)
//  private val advertiserHourlySummaryShifuDriver = new AdvertiserHourlySummaryShifuDriver(shifuSource, config: Config, context: SparkSession)
//  private val lbandAstonArBugImpressionDriver = new LbandAstonArBugImpressionDriver(config: Config, context: SparkSession)
//  val commonUtils = new AdvertiserCommonUtils()
//
//  def load():Seq[Seq[DataFrame]]={
//
//    val shifuDf = getLiveShifuSourceData //returns raw and advertiserAgg level df's
//
//    //it data is all filtered out, atleast it must give empty dataframe with columns right. This is not coming as expected, i checked the conditions in config are also looking fine.!
//    val blazeDf = getLiveBlazeSourceData
//    val lbandDf = getLiveLbandSourceData
//    val shifu_blaze_uploader_s3Df = getLiveShifuBlazeUploaderData //returns uploader s3 data
//    val osDumpDf = getLiveOpensearchDumpData("uploader.opensearch.config")
//    //    val emptyDf = context.createDataFrame(Seq((0L, 0L))).toDF("impressions", "clicks")
//    //    val shifuDf = Seq(emptyDf, emptyDf)
//    //    val blazeDf = Seq(emptyDf, emptyDf)
//
//    Seq(shifuDf, blazeDf, lbandDf, Seq(shifu_blaze_uploader_s3Df),Seq(osDumpDf))
//  }
//
//  def transform(df:Seq[Seq[DataFrame]]):DataFrame={
//    val columns = List("Type", "shifu_imp", "blaze_imp", "lband_imp", "shifu_clicks", "blaze_clicks", "lband_clicks", "all sources imp", "all sources clicks")
//    val type_list = List("Raw Data Agg", "S3 Agg Data", "Raw Data Uploader", "S3 Uploader Data","OS Dump Data")
//    var rows = Seq.empty[(String, Option[Long], Option[Long], Option[Long], Option[Long], Option[Long], Option[Long], Option[Long], Option[Long])]
//
//    for(i <- type_list) {
//      i match {
//        case "Raw Data Agg" =>
//          val shifu_imp = Some(df.head(0).first().getAs[Long]("impressions"))
//          val blaze_imp = Some(df(1)(0).first().getAs[Long]("impressions"))
//          val shifu_clicks = Some(df.head(0).first().getAs[Long]("clicks"))
//          val blaze_clicks = Some(df(1)(0).first().getAs[Long]("clicks"))
//          rows = rows :+ (i, shifu_imp, blaze_imp, None, shifu_clicks, blaze_clicks, None, None, None)
//        case "S3 Agg Data" =>
//          val shifu_imp = Some(df.head(1).first().getAs[Long]("impressions"))
//          val blaze_imp = Some(df(1)(1).first().getAs[Long]("impressions"))
//          val shifu_clicks = Some(df.head(1).first().getAs[Long]("clicks"))
//          val blaze_clicks = Some(df(1)(1).first().getAs[Long]("clicks"))
//          rows = rows :+ (i, shifu_imp, blaze_imp, None, shifu_clicks, blaze_clicks, None, None, None)
//        case "Raw Data Uploader" =>
//          val shifu_blaze_imp = Some(df.head(0).first().getAs[Long]("impressions") + df(1)(0).first().getAs[Long]("impressions"))
//          val lband_imp = Some(df(2)(0).first().getAs[Long]("impressions"))
//          val shifu_blaze_clicks = Some(df.head(0).first().getAs[Long]("clicks") + df(1)(0).first().getAs[Long]("clicks"))
//          val lband_clicks = Some(df(2)(0).first().getAs[Long]("clicks"))
//          rows = rows :+ (i, shifu_blaze_imp, shifu_blaze_imp, lband_imp, shifu_blaze_clicks, shifu_blaze_clicks, lband_clicks, None, None)
//        case "S3 Uploader Data" =>
//          val shifu_blaze_imp = Some(df(3)(0).first().getAs[Long]("impressions"))
//          val lband_imp = Some(df(2)(1).first().getAs[Long]("impressions"))
//          val shifu_blaze_clicks = Some(df(3)(0).first().getAs[Long]("clicks"))
//          val lband_clicks = Some(df(2)(1).first().getAs[Long]("clicks"))
//          rows = rows :+ (i, shifu_blaze_imp, shifu_blaze_imp, lband_imp, shifu_blaze_clicks, shifu_blaze_clicks, lband_clicks, None, None)
//        case "OS Dump Data"=>
//          val all_source_imp = Some(df(4)(0).first().getAs[Long]("impressions"))
//          val all_source_clicks = Some(df(4)(0).first().getAs[Long]("clicks"))
//          rows = rows :+ (i, None, None, None, None, None, None, all_source_imp, all_source_clicks)
//      }
//    }
//    // Convert to DataFrame
//    import context.implicits._
//    val qcDf = rows.toDF(columns: _*)
//    println("Final Df -> ", qcDf.show(false))
//    qcDf
//  }
//
//  private def getLiveShifuSourceData:Seq[DataFrame]={
//    val shifu_raw_inputPath = config.getString("shifu" + ".input_path")
//
//    val rawShifuDf = advertiserHourlySummaryShifuDriver.load("", fetchRawShifuData(mode, cd, shifu_raw_inputPath, context))
//    val rawAggTransformedShifuDf = getAdvertiserLiveShifuAggTransformDf(advertiserHourlySummaryShifuDriver, rawShifuDf)
//
//    val shifuAdvertiserReportingAggLevelDf = context.read.parquet(config.getString("hourly_output_path") + shifuSource + "/" + datePath)
//      .agg(
//        sum("impressions").alias("impressions"),
//        sum("clicks").alias("clicks"))
//
//    Seq(rawAggTransformedShifuDf, shifuAdvertiserReportingAggLevelDf)
//  }
//
//  private def getLiveBlazeSourceData:Seq[DataFrame]={
//    val streamTypeVodValues = commonUtils.streamTypeVodValues
//    var rawBlazeDf = advertiserHourlySummaryBlazeDriver.load(datePath)
//    var rawAggTransformedBlazeDf = context.emptyDataFrame
//    if (rawBlazeDf.isEmpty) {
//      rawBlazeDf = Option(context.emptyDataFrame)
//      rawAggTransformedBlazeDf = context.createDataFrame(Seq((0L, 0L))).toDF("impressions", "clicks")
//      println("blaze empty df -> ")
//      rawAggTransformedBlazeDf.printSchema()
//    }
//    else {
//      rawAggTransformedBlazeDf = getAdvertiserLiveBlazeAggTransformDf(advertiserHourlySummaryBlazeDriver, rawBlazeDf.get, streamTypeVodValues)
//      println("blaze transformed df -> ")
//      rawAggTransformedBlazeDf.printSchema()
//    }
//    val blazeAdvertiserReportingAggLevelDf = context.read.parquet(config.getString("hourly_output_path") + balzeSource + "/" + datePath)
//      .agg(
//        sum("impressions").alias("impressions"),
//        sum("clicks").alias("clicks"))
//
//    Seq(rawAggTransformedBlazeDf, blazeAdvertiserReportingAggLevelDf)
//  }
//
//  private def getLiveLbandSourceData:Seq[DataFrame]={
//    val lband_raw_path = config.getString("lband_astron_arbug.input_path")
//    var rawLbandDf = lbandAstonArBugImpressionDriver.load(lband_raw_path)
//    if(rawLbandDf.head(1).isEmpty){
//      rawLbandDf = context.createDataFrame(Seq((0L, 0L))).toDF("impressions", "clicks")
//      println("lband empty df -> ")
//      rawLbandDf.printSchema()
//    }
//
//    rawLbandDf = rawLbandDf
//      .filter(col("advertiser_id").isNotNull)
//      .filter(col("brand_id").isNotNull)
//      .filter(col("stream_type") === "SPORT_LIVE")
//      .agg(count("*") as "impressions")
//      .withColumn("clicks", lit(0).cast(LongType))
//
//    val lbandAdvertiserReportingLevelDf = context.read.parquet(config.getString("lband_astron_arbug.impression.output_path")+datePath)
//      .agg(
//        sum("impressions").alias("impressions"),
//        sum("clicks").alias("clicks"))
//
//    Seq(rawLbandDf, lbandAdvertiserReportingLevelDf)
//  }
//
//  private def getLiveOpensearchDumpData(path:String):DataFrame={
//    val alias = config.getString("uploader.summary.alias")
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//    val localDate = LocalDate.parse(cd, formatter)
//    val epochMillisDate = localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli
//
//    val query =s"""
//                  |{
//                  |  "query": {
//                  |    "term": {
//                  |      "cd": "$epochMillisDate"
//                  |    }
//                  |  }
//                  |}""".stripMargin
//    var options = commonUtils.openSearchOptions(path, config)
//    options += "opensearch.resource" -> alias
//    options += "opensearch.query" -> query
//
//    val Osdf = context.read
//      .format("org.opensearch.spark.sql")
//      .options(options)
//      .load()
//
//    println("Os data -> ")
//    Osdf.printSchema()
//    println(Osdf.show())
//    Osdf.agg(
//      sum("impressions").alias("impressions"),
//      sum("clicks").alias("clicks"))
//  }
//
//  private def getLiveShifuBlazeUploaderData:DataFrame = {
//    context.read.parquet(config.getString("uploader.summary.output_path") + datePath)
//      .agg(
//        sum("impressions").alias("impressions"),
//        sum("clicks").alias("clicks"))
//  }
//
//  def write(df:DataFrame):Unit= {
//    val isAggMatching = checkAggLive(df.filter(col("Type").isin("Raw Data Agg", "S3 Agg Data")))
//    val isUploaderMatching = checkUploaderLive(df.filter(col("Type").isin("Raw Data Uploader", "S3 Uploader Data","OS Dump Data")))
//
//    df.write.mode("overwrite").option("header", "true").csv(config.getString("qc.output_path") + datePath)
//    //    if (!isMatchTrue(df)) {
//    //      throw new Exception("QC check failed for Advertiser Reporting Summary.")
//    //    }
//  }
//
//  def aggregateData(df:Seq[Seq[DataFrame]]):DataFrame={
//    val rawAggregationImp = Seq(aggregate("impressions", df.head(0), "impressions"), //blaze
//      aggregate("impressions", df.head(2), "impressions"), //shifu
//      aggregate("impressions", df.head(3), "impressions")) //lband
//
//    val rawAggregationClicks = Seq(aggregate("clicks", df.head(1), "clicks"),
//      aggregate("clicks", df.head(2), "clicks"),
//      aggregate("clicks", df.head(3), "clicks"))
//
//    val s3AggregationImp = Seq(aggregate("impressions", df(1).head, "impressions"),
//      aggregate("impressions", df(1)(1), "impressions"),
//      aggregate("impressions", df(1)(2), "impressions"),
//      aggregate("impressions", df(1)(3), "impressions")) //shifu_blaze_s3
//
//    val s3AggregationClicks = Seq(aggregate("clicks", df(1).head, "clicks"),
//      aggregate("clicks", df(1)(1), "clicks"),
//      aggregate("clicks", df(1)(2), "clicks"),
//      aggregate("clicks", df(1)(3), "clicks")) //shifu_blaze_s3
//
//    val osDumpAggregation = Seq(aggregate("impressions", df(2).head, "impressions"),
//      aggregate("clicks", df(2).head, "clicks"))
//
//    println("Raw Imp -> ", rawAggregationImp)
//    println("Raw Clicks -> ", rawAggregationClicks)
//    println("S3 Imp -> ", s3AggregationImp)
//    println("S3 Clicks -> ", s3AggregationClicks)
//    println("OS Dump -> ", osDumpAggregation)
//    createDf(Seq(rawAggregationImp, rawAggregationClicks, s3AggregationImp, s3AggregationClicks, osDumpAggregation))
//  }
//
//  private def createDf(aggregatedData:Seq[Seq[Long]]):DataFrame ={
//    val columns = config.getStringList(content_category+".imp_qc_output_column").asScala.toList
//    val type_list = List("Raw Agg Data", "S3 Agg Data", "OS Dump Data")
//    var rows = Seq.empty[(String, Option[Long], Option[Long], Option[Long], Option[Long], Option[Long], Option[Long], Option[Long], Option[Long], Option[Long], Option[Long])]
//
//    for (i <- type_list) {
//      i match{
//        case "Raw Agg Data"=>
//          val shifu_imp = Some(aggregatedData.head(1))
//          val blaze_imp = Some(aggregatedData.head(0))
//          val lband_imp = Some(aggregatedData.head(2))
//          val shifu_clicks = Some(aggregatedData(1)(1))
//          val blaze_clicks = Some(aggregatedData(1)(0))
//          val lband_clicks = Some(aggregatedData(1)(2))
//          val shifu_blaze_imp = Some(aggregatedData.head(0) + aggregatedData.head(1))
//          val shifu_blaze_clicks = Some(aggregatedData(1)(0) + aggregatedData(1)(1))
//          val total_imp = Some(aggregatedData.head.sum)
//          val total_clicks = Some(aggregatedData(1).sum)
//          rows = rows :+ (i, shifu_imp, blaze_imp, lband_imp, shifu_clicks, blaze_clicks, lband_clicks, shifu_blaze_imp, shifu_blaze_clicks, total_imp, total_clicks)
//        case "S3 Agg Data"=>
//          val shifu_imp = Some(aggregatedData(2)(1))
//          val blaze_imp = Some(aggregatedData(2)(0))
//          val lband_imp = Some(aggregatedData(2)(2))
//          val shifu_clicks = Some(aggregatedData(3)(1))
//          val blaze_clicks = Some(aggregatedData(3)(0))
//          val lband_clicks = Some(aggregatedData(3)(2))
//          val shifu_blaze_imp = Some(aggregatedData(2)(3))
//          val shifu_blaze_clicks = Some(aggregatedData(3)(3))
//          val total_imp = Some(aggregatedData(2).head + aggregatedData(2)(1) + aggregatedData(2)(2))
//          val total_clicks = Some(aggregatedData(3).head + aggregatedData(3)(1) + aggregatedData(3)(2))
//          rows = rows :+ (i, shifu_imp, blaze_imp, lband_imp, shifu_clicks, blaze_clicks, lband_clicks, shifu_blaze_imp, shifu_blaze_clicks, total_imp, total_clicks)
//        case "OS Dump Data"=>
//          val os_imp = Some(aggregatedData(4)(0))
//          val os_clicks = Some(aggregatedData(4)(1))
//          rows = rows :+ (i, None, None, None, None, None, None, None, None, os_imp, os_clicks)
//      }
//    }
//    // Convert to DataFrame
//    import context.implicits._
//    val qcDf = rows.toDF(columns: _*)
//    println("Final Df -> ", qcDf.show(false))
//    qcDf
//  }
//
//}
//
//object AdvertiserAggQcDriver {
//  val logger = Logger.getLogger(getClass.getName)
//  val config = ApplicationProperties.getConf("advertiser_reporting_summary").getConfig("advertiser_reporting_summary")
//
//  val appName = config.getString("app_name")
//  val sparkObjects = new SparkUtil(appName, ApplicationProperties.getConf().getString("application.logLevel"))
//  val sparkSession = sparkObjects.getSparkSession
//  sparkSession.sparkContext.hadoopConfiguration.set("fs.s3a.multiobjectdelete.enable", "false")
//
//  def main(args: Array[String]): Unit = {
//
//    val cd = System.getProperty("cd")
//    var datePath = ""
//
//    if (System.getProperty("mode").equalsIgnoreCase("daily")) {
//      datePath = "cd=" + cd + "/"
//    }
//    val advertiserAggQcDriver = new AdvertiserAggQcDriver(config, sparkSession, datePath)
//    val df = advertiserAggQcDriver.load()
//    val transformedDf = advertiserAggQcDriver.transform(df)
//    //    advertiserAggQcDriver.write(transformedDf)
//  }
//
//
//  //test aggregate function
//  def aggregateData1(df: Seq[Seq[DataFrame]]): DataFrame = {
//    val aggregatedResults = categories.map { category =>
//      val categoryConfig = dataSources.getConfig(category)
//      val groupIndex = categoryConfig.getInt("group")
//      val impIndices = categoryConfig.getIntList("impressions").asScala.toList
//      val clickIndices = categoryConfig.getIntList("clicks").asScala.map(_.toInt)
//
//      val impAggregates = impIndices.map(i => aggregate("impressions", df(groupIndex)(i), "impressions"))
//      val clickAggregates = clickIndices.map(i => aggregate("clicks", df(groupIndex)(i), "clicks"))
//
//      val combinedImp = if (categoryConfig.hasPath("combine.shifu_blaze_imp"))
//        Some(categoryConfig.getIntList("combine.shifu_blaze_imp").asScala.map(i => impAggregates(i)).sum)
//      else None
//
//      val combinedClicks = if (categoryConfig.hasPath("combine.shifu_blaze_clicks"))
//        Some(categoryConfig.getIntList("combine.shifu_blaze_clicks").asScala.map(i => clickAggregates(i)).sum)
//      else None
//
//      val totalImp = if (impAggregates.nonEmpty) Some(impAggregates.sum) else None
//      val totalClicks = if (clickAggregates.nonEmpty) Some(clickAggregates.sum) else None
//
//      // Pad missing values with None to match outputColumns
//      val row = category match {
//        case "Raw Agg Data" | "S3 Agg Data" =>
//          val rowData = List(
//            Some(category),
//            impAggregates.lift(1), // shifu
//            impAggregates.lift(0), // blaze
//            impAggregates.lift(2), // lband
//            clickAggregates.lift(1),
//            clickAggregates.lift(0),
//            clickAggregates.lift(2),
//            combinedImp,
//            combinedClicks,
//            totalImp,
//            totalClicks
//          )
//          rowData
//        case "OS Dump Data" =>
//          List(Some(category)) ++ List.fill(8)(None) ++ impAggregates.headOption ++ clickAggregates.headOption
//      }
//      row
//    }
//
//    // Convert rows to DataFrame
//    import context.implicits._
//    val rows = aggregatedResults.map {
//      case Seq(a, b, c, d, e, f, g, h, i, j, k) =>
//        (a, b, c, d, e, f, g, h, i, j, k)
//    }
//
//    rows.toDF(outputColumns: _*)
//  }
//}



//i will tell the use case. tournament_id is coming from shifu in one function and blaze from other function.

//  I have a requirement where i want to join the df with shifu data and if the tournament_id is not present in it, then fetch from blaze table. Give me 3 ways of doing this. You can also segregate the functions and create an common tournament details function.


//conf ->
//enrichment_details{
//  meta_details {
//    format = "jdbc"
//    enrich_connection = "postgres_ad_model_conn"
//    jdbc {
//      query = "select c.id, type, demand_source, hs_agency_id, hs_brand_id, tournament_id, t.name as tournament_name, hs_advertiser_id from campaign c join tournament t on t.id = tournament_id"
//    }
//    column_rename = {
//      hs_brand_id = brand_id
//      hs_agency_id = agency_id
//      hs_advertiser_id = advertiser_id
//      type = campaign_type
//    }
//    right_df_join_field = "campaign_id"
//    left_df_join_field = "id"
//    join_type = "left"
//  }
//
//  advertiser_name {
//    format = "sql"
//    enrich_connection = ""
//    jdbc {
//      query = "select id, name from hudi.in_selfserve_ads_advertiser_s3"
//    }
//    column_rename = {
//      name = advertiser_name
//      id = hs_advertiser_id
//    }
//    right_df_join_field = "advertiser_id"
//    left_df_join_field = "hs_advertiser_id"
//    join_type = "left"
//  }
//
//  brand_name {
//    format = "sql"
//    enrich_connection = ""
//    jdbc {
//      query = "select id, name from hudi.in_selfserve_ads_universal_brand_s3"
//    }
//    column_rename = {
//      name = brand_name
//      id = hs_brand_id
//    }
//    right_df_join_field = "brand_id"
//    left_df_join_field = "hs_brand_id"
//    join_type = "left"
//  }
//
//  blaze_ad_length {
//    format = "sql"
//    enrich_connection = ""
//    jdbc {
//      query = "SELECT CAST(ROUND(duration / 1000, 0) AS BIGINT) AS ad_length, ad.creative_id as ad_id FROM asset a join ad ad on ad.id = a.id"
//    }
//    column_rename = {
//      ad_length = ad_length
//    }
//    right_df_join_field = "ad_id"
//    left_df_join_field = "ad_id"
//    join_type = "left"
//  }
//
//  blaze_content_details {
//    format = "sql"
//    enrich_connection = ""
//    jdbc {
//      query = "select contentid as content_id, title as content_title, contenttype as content_type from in_cms.match_update_s3"
//    }
//    column_rename = {
//    }
//    right_df_join_field = "content_id"
//    left_df_join_field = "content_id"
//    join_type = "left"
//  }
//
//  location_cluster_details {
//    format = "jdbc"
//    enrich_connection = "postgres_ad_model_conn"
//    jdbc {
//      query = "select name, ssai_tag from attribute_value where attribute_id = 2 and  enabled = true"
//    }
//    column_rename = {
//      ssai_tag = city_tag
//      name = location_cluster_name
//    }
//    right_df_join_field = "city"
//    left_df_join_field = "city_tag"
//    join_type = "left"
//  }
//
//  state_mapping {
//    format = "jdbc"
//    enrich_connection = "mysql_mds_conn"
//    jdbc {
//      query = "select short_name from state"
//    }
//    column_rename = {
//      short_name = mds_state
//    }
//    right_df_join_field = "state"
//    left_df_join_field = "mds_state"
//    join_type = "left"
//  }
//}

