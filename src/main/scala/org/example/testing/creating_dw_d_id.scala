package org.example.testing

import org.apache.commons.codec.digest.DigestUtils
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{col, lit, lower, udf}
import org.example.testing.creating_dw_d_id.Platform

class creating_dw_d_id (){
  val getSHA256Func: (String) => String = (pId: String) => {
    getSHA256(pId)
  }

  val getSHA1Func: (String) => String = (id: String) => {
    getSHA1(id)
  }

  val getDwDIdFunc: (String, String, String, String, String) => String = (deviceId: String, aaid: String, idfa: String, cookieId: String, advertisingId: String) => {
    getDwDId(deviceId, aaid, idfa, cookieId, advertisingId)
  }

  val getSHA1Udf: UserDefinedFunction = udf(getSHA1Func)

  val getSHA256Udf: UserDefinedFunction = udf(getSHA256Func)

  val getDwDIdUdf: UserDefinedFunction = udf(getDwDIdFunc)



  def getSHA256(id: String): String = {
    if (Option(id).getOrElse("").isEmpty) "" else  DigestUtils.sha256Hex(id)
  }

  def getSHA1(id: String): String = {
    if (Option(id).getOrElse("").isEmpty) "" else  DigestUtils.sha1Hex(id)
  }

  def getDwDId(deviceId: String, aaid: String, idfa: String, cookieId: String, advertisingId: String): String = {
    val ids = List(aaid, idfa, cookieId, advertisingId, deviceId)
    val did = Option(deviceId).getOrElse("").concat(getFirstNotEmptyValueFromList(ids))
    getSHA256(did)
  }

  val getDwDIdFunc_lband: (String, String, String, String) => String = (deviceId: String, aaid: String, idfa: String, cookieId: String) => {
    getDwDId_lband(deviceId, aaid, idfa, cookieId)
  }

  def getDwDId_lband(deviceId: String, aaid: String, idfa: String, cookieId: String): String = {
    val ids = List(aaid, idfa, cookieId, deviceId)
    val did = Option(deviceId).getOrElse("").concat(getFirstNotEmptyValueFromList(ids))
    getSHA256(did)
  }
  val getDwDIdUdf_lband: UserDefinedFunction = udf(getDwDIdFunc_lband)

  def getFirstNotEmptyValueFromList(ids: List[String]): String = {
    ids.collectFirst({case x if Option(x).isDefined && !x.isEmpty => x}).getOrElse("")
  }

  def cleanUserAdIdentifierUdf(advIdSupportedPlatform: Map[String, Array[String]]) = udf[String, String, String, String, String] ((advertisingId, lat, platform, idType) => {
    var returnVal = ""
    print(advIdSupportedPlatform(idType).contains(Option(platform).getOrElse(Platform.android.toString)))
    if(advIdSupportedPlatform(idType).contains(Option(platform).getOrElse(Platform.android.toString))) {
      returnVal = if (lat.trim.equalsIgnoreCase("true")) "0" else advertisingId
    }
    returnVal
  })
}

object creating_dw_d_id extends App {

  object Platform extends Enumeration {
    type Platform = Value
    val android, ios, web, mweb, androidtv, firestick, webos, firetv, tizentv = Value
  }

  val advIdSupportedPlatform = Map(
    ("aaid", Array(Platform.android.toString, Platform.androidtv.toString, Platform.firestick.toString, Platform.ios.toString)),
    ("idfa", Array(Platform.ios.toString))
  )
  val spark = SparkSession.builder()
    .appName("blog.knoldus.com")
    .master("local")
    .getOrCreate()

  import spark.implicits._

  val sample_lband = Seq(
    ("d0d9b34f54107f8d", "b8a780fd-442b-4455-bf53-6124e973c7e6"),
    ("3c3ffbd1b0526f53", ""),
    ("beaa82a986155d83", "3df1f872-7182-4e24-9b11-4d7db3a1b54b"),
    ("ddc868c7fa1a05f6", "00000000-0000-0000-0000-000000000000"),
    ("647a23b1e4cc8a3a","16f8c1c8-e842-4dd0-b351-c20e90e829fe"),
    ("1702275bd069a364","5dea4fc0-5d94-40d0-b802-8decb7fd4d38")
  ).toDF("deviceId", "adv_id")
//4d9b1e837537f59454e9527ae010984158715d288d4529f84e2467fd82f0cae6
  val create = new creating_dw_d_id()

  val df = sample_lband.withColumn("dw_d_id", create.getDwDIdUdf_lband(col("deviceId"), col("adv_id"), lit(""), lit("")))
    .withColumn("dw_device_id", create.getSHA256Udf(col("deviceId")))
  print(df.show(false))


    val sample_ad_imp = Seq(
      ("d0d9b34f54107f8d", "b8a780fd-442b-4455-bf53-6124e973c7e6", ""),
      ("d0d9b34f54107f8d", "", ""),
      ("3c3ffbd1b0526f53", null, null),
      ("beaa82a986155d83", "3df1f872-7182-4e24-9b11-4d7db3a1b54b", ""),
      ("beaa82a986155d83", "", ""),
      ("ddc868c7fa1a05f6", "00000000-0000-0000-0000-000000000000", ""),
      ("ddc868c7fa1a05f6", "0", ""),
      ("081f6f16e2ef777f", "00000000-0000-0000-0000-000000000000", ""),
      ("647a23b1e4cc8a3a","16f8c1c8-e842-4dd0-b351-c20e90e829fe", ""),
      ("647a23b1e4cc8a3a","", ""),
      ("1702275bd069a364","5dea4fc0-5d94-40d0-b802-8decb7fd4d38", ""),
      ("1702275bd069a364","", ""),
      ("4ba65b8d4b55cbe2", "0", ""),
      ("56afae-1cee91-3c564e-1e6e2", "56afae-1cee91-3c564e-1e6e2", ""),
      ("5f6732-116c0a-5bcdd1-6386f7", "5f6732-116c0a-5bcdd1-6386f7", ""),
      ("56afae-1cee91-3c564e-1e6e2", "5f6732-116c0a-5bcdd1-6386f7", ""),
      ("36aeae-1aaabb-20d880-5ba66a", "36aeae-1aaabb-20d880-5ba66a", ""),
      ("5228103aa735be99", "6a6f3020-6877-49fb-92bd-3788dab73c7a", ""),
      ("4e166c-8a817c-277390-414220", "4e166c-8a817c-277390-414220", ""),
      ("cf48fad4611fe588","68b3aeab-fb73-4fd1-9689-1380c4363914",""),
      ("1ecedca2a9803286", "3edd8daa-f3e8-4cc3-b732-ede1975d6f00", ""),
      ("cdedfd450e8231c8", "", ""), ("17ddd6f83d57c979","a35b6db5-c8b2-4215-a8c1-b6a71a06892c",""),
      ("2d56347be82b4173","0",""), ("3e38f8-6f77af-8f19e5-50f2f4", "3e38f8-6f77af-8f19e5-50f2f4", ""),
      ("623534-238af9-4cc5-7d55c6", "3e38f8-6f77af-8f19e5-50f2f4", "")
    ).toDF("device_id", "aaid", "idfa")

  val sample_ad_imp_1 = Seq(
        ("d0d9b34f54107f8d", "b8a780fd-442b-4455-bf53-6124e973c7e6", "", "firetv", "false"),
        ("d0d9b34f54107f8d", "", "", "firetv", "false"),
        ("3c3ffbd1b0526f53", null, null, "AndroidTv", "false"),
        ("beaa82a986155d83", "3df1f872-7182-4e24-9b11-4d7db3a1b54b", "", "iOS", "false"),
        ("beaa82a986155d83", "", "", "firetv", "false")
      ).toDF("device_id", "aaid", "idfa", "platform", "aaid_lat")

  val df_ad_imp = sample_ad_imp
//    .withColumn("aaid_check", create.cleanUserAdIdentifierUdf(advIdSupportedPlatform)(col("aaid"), col("aaid_lat"), lower(col("platform")), lit("aaid")))
//    .withColumn("idfa", create.cleanUserAdIdentifierUdf(advIdSupportedPlatform)(col("idfa"), col("idfa_lat"), col("platform"), lit("idfa")))
    .withColumn("dw_d_id", create.getDwDIdUdf(col("device_id"), col("aaid"), col("idfa"), col("device_id"),  lit("")))
    .withColumn("dw_device_id", create.getSHA256Udf(col("device_id")))
  print(df_ad_imp.show(30,false))
}
