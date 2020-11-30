package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework

import play.api.libs.json.{Format, JsPath}

case class IfApplications(applications: Seq[IfApplication])

object IfApplications {
  implicit val applicationsFormat: Format[IfApplications] = Format(
    (JsPath \ "applications").read[Seq[IfApplication]].map(x => IfApplications(x)),
    (JsPath \ "applications").write[Seq[IfApplication]].contramap(x => x.applications)
  )
}
