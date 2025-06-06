/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.services

import org.scalatest.PrivateMethodTester
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.{B, L}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class NodeSpec extends UnitSpec with MockitoSugar with PrivateMethodTester {

  "L (Leaf Node)" should {
    "return its value correctly" in {
      val leaf = L("leaf")
      assert(leaf.get == "leaf")
      assert(leaf.listChildren.isEmpty)
      assert(!leaf.hasChild("child"))
      assert(leaf.getChild("child").isEmpty)
      assert(leaf.toString == "leaf")
    }
  }

  "B (Branch Node) " should {
    "return its value and children correctly" in {
      val branch = B("branch", Seq(L("child1"), L("child2")))
      assert(branch.get == "branch")
      assert(branch.listChildren == Seq("child1", "child2"))
      assert(branch.hasChild("child1"))
      assert(branch.getChild("child1").contains(L("child1")))
      assert(!branch.hasChild("nonexistent"))
      assert(branch.getChild("nonexistent").isEmpty)
      assert(branch.toString == "branch(child1,child2)")
    }
  }
}
