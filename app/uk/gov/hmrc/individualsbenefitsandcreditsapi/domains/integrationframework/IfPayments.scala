package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

case class IfPayments(
                      periodStartDate: Option[String],
                      periodEndDate: Option[String],
                      startDate: Option[String],
                      endDate: Option[String],
                      status: Option[String],
                      postedDate: Option[String],
                      nextDueDate: Option[String],
                      frequency: Option[Int],
                      tcType: Option[String],
                      amount: Option[Double],
                      method: Option[String]
                     )

object IfPayments {

  val statusPattern = "^([ADSCX])$".r
  val methodPattern = "^([ROM])$".r
  val tcTypePattern = "^(ETC|ITC])$".r
  val datePattern = ("^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[-]02[-]29|((19|20)[0-9]{2}[-](0[469]|11)[-]" +
    "(0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[-](0[13578]|1[02])[-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[-]02[-]" +
    "(0[1-9]|1[0-9]|2[0-8])))$").r

  val minPaymentValue = 0.0
  val maxPaymentValue = 1000000000000000.0

  def isMultipleOfPointZeroOne(value: Double): Boolean = (value * 100.0) % 1 == 0

  def paymentAmountValidator:Reads[Double] =
    min[Double](minPaymentValue) andKeep max[Double](maxPaymentValue) andKeep verifying[Double](isMultipleOfPointZeroOne)


  implicit val paymentsFormat: Format[IfPayments] = Format(
    (
      (JsPath \ "periodStartDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "periodEndDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "startDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "endDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "status").readNullable[String](pattern(statusPattern, "invalid status")) and
        (JsPath \ "postedDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "nextDueDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "frequency").readNullable[Int](min[Int](1).keepAnd(max[Int](999))) and
        (JsPath \ "tcType").readNullable[String](pattern(tcTypePattern, "invalid tc type")) and
        (JsPath \ "amount").readNullable[Double](paymentAmountValidator) and
        (JsPath \ "method").readNullable[String](pattern(methodPattern, "invalid method"))
      )(IfPayments.apply _),
    (
      (JsPath \ "periodStartDate").writeNullable[String] and
        (JsPath \ "periodEndDate").writeNullable[String] and
        (JsPath \ "startDate").writeNullable[String] and
        (JsPath \ "endDate").writeNullable[String] and
        (JsPath \ "status").writeNullable[String] and
        (JsPath \ "postedDate").writeNullable[String] and
        (JsPath \ "nextDueDate").writeNullable[String] and
        (JsPath \ "frequency").writeNullable[Int] and
        (JsPath \ "tcType").writeNullable[String] and
        (JsPath \ "amount").writeNullable[Double] and
        (JsPath \ "method").writeNullable[String]
      )(unlift(IfPayments.unapply))
  )
}
