package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath, Reads}
import play.api.libs.json.Reads.{max, min, pattern, verifying}

case class IfApplication(id: Double, ceasedDate: Option[String], entStartDate: Option[String], entEndDate: Option[String], awards: Option[Seq[IfAwards]])

object IfApplication {

  val datePattern = ("^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[-]02[-]29|((19|20)[0-9]{2}[-](0[469]|11)[-]" +
    "(0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[-](0[13578]|1[02])[-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[-]02[-]" +
    "(0[1-9]|1[0-9]|2[0-8])))$").r

  def isMultipleOfOne(value: Double): Boolean = value % 1 == 0

  def applicationIdValidator:Reads[Double] =
    min[Double](0) andKeep max[Double](999999999999.0) andKeep verifying[Double](isMultipleOfOne)


  implicit val applicationFormat: Format[IfApplication] = Format(
    (
      (JsPath \ "id").read[Double](applicationIdValidator) and
        (JsPath \ "ceasedDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "entStartDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "entEndDate").readNullable[String](pattern(datePattern, "invalid date")) and
        (JsPath \ "awards").readNullable[Seq[IfAwards]]
      )(IfApplication.apply _),
    (
      (JsPath \ "id").write[Double] and
        (JsPath \ "ceasedDate").writeNullable[String] and
        (JsPath \ "entStartDate").writeNullable[String] and
        (JsPath \ "entEndDate").writeNullable[String] and
        (JsPath \ "awards").writeNullable[Seq[IfAwards]]
      )(unlift(IfApplication.unapply))
  )
}
