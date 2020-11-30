package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath, Reads}
import play.api.libs.json.Reads.{max, min, pattern, verifying}

case class IfAwards(
                     payProfCalcDate: Option[String],
                     startDate: Option[String],
                     endDate: Option[String],
                     totalEntitlement: Option[Double],
                     workTaxCredit: Option[IfWorkTaxCredit],
                     childTaxCredit: Option[IfChildTaxCredit],
                     grossTaxYearAmount: Option[Double],
                     payments: Option[IfPayments]
                   )

object IfAwards {

  val datePattern = ("^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[-]02[-]29|((19|20)[0-9]{2}[-](0[469]|11)[-]" +
    "(0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[-](0[13578]|1[02])[-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[-]02[-]" +
    "(0[1-9]|1[0-9]|2[0-8])))$").r

  val minPaymentValue = 0.0
  val maxPaymentValue = 1000000000000000.0

  def isMultipleOfPointZeroOne(value: Double): Boolean = (value * 100.0) % 1 == 0

  def paymentAmountValidator:Reads[Double] =
    min[Double](minPaymentValue) andKeep max[Double](maxPaymentValue) andKeep verifying[Double](isMultipleOfPointZeroOne)

  implicit val awardsFormat: Format[IfAwards] = Format(
    (
      (JsPath \ "payProfCalcDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "startDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "endDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "totalEntitlement").readNullable[Double](paymentAmountValidator) and
        (JsPath \ "workTaxCredit").readNullable[IfWorkTaxCredit] and
        (JsPath \ "childTaxCredit").readNullable[IfChildTaxCredit] and
        (JsPath \ "grossYearTaxAmount").readNullable[Double](paymentAmountValidator) and
        (JsPath \ "payments").readNullable[IfPayments]
      )(IfAwards.apply _),
    (
      (JsPath \ "payProfCalcDate").writeNullable[String] and
        (JsPath \ "startDate").writeNullable[String] and
        (JsPath \ "endDate").writeNullable[String] and
        (JsPath \ "totalEntitlement").writeNullable[Double] and
        (JsPath \ "workTaxCredit").writeNullable[IfWorkTaxCredit] and
        (JsPath \ "childTaxCredit").writeNullable[IfChildTaxCredit] and
        (JsPath \ "grossYearTaxAmount").writeNullable[Double] and
        (JsPath \ "payments").writeNullable[IfPayments]
      )(unlift(IfAwards.unapply))
  )
}
