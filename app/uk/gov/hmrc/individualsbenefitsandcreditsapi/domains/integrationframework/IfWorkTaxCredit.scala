package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads.{max, min, verifying}
import play.api.libs.json.{Format, JsPath, Reads}

case class IfWorkTaxCredit(amount: Option[Double], entitlementYTD: Option[Double], paidYTD: Option[Double])

object IfWorkTaxCredit {

  val minPaymentValue = 0.0
  val maxPaymentValue = 1000000000000000.0

  def isMultipleOfPointZeroOne(value: Double): Boolean = (value * 100.0) % 1 == 0

  def paymentAmountValidator:Reads[Double] =
    min[Double](minPaymentValue) andKeep max[Double](maxPaymentValue) andKeep verifying[Double](isMultipleOfPointZeroOne)

  implicit val workTaxCreditFormat: Format[IfWorkTaxCredit] = Format(
    (
      (JsPath \ "amount").readNullable[Double](paymentAmountValidator) and
        (JsPath \ "entitlementYTD").readNullable[Double](paymentAmountValidator) and
        (JsPath \ "paidYTD").readNullable[Double](paymentAmountValidator)
      )(IfWorkTaxCredit.apply _),
    (
      (JsPath \ "amount").writeNullable[Double] and
        (JsPath \ "entitlementYTD").writeNullable[Double] and
        (JsPath \ "paidYTD").writeNullable[Double]
      )(unlift(IfWorkTaxCredit.unapply))
  )
}
