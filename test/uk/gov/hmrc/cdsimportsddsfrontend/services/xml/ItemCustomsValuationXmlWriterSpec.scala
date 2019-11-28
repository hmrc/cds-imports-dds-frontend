package uk.gov.hmrc.cdsimportsddsfrontend.services.xml

import org.scalatest.{Matchers, OptionValues, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{ChargeDeduction, CurrencyAmount, ItemCustomsValuation}
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ItemCustomsValuationXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

import scala.xml.Utility

class ItemCustomsValuationXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "ItemCustomsValuation XML writer" should {
    "generate the ItemCustomsValuation XML element" when {
      "all values are present" in {

        val itemCustomsValuation = ItemCustomsValuation(
          methodCode = Some("12"),
          chargeDeduction = Some(ChargeDeduction(typeCode = "AS",
            currencyAmount = CurrencyAmount(currency = "GBP", amount = "100")))
        )

        val expectedXml = Utility.trim({
          <CustomsValuation>
            <MethodCode>12</MethodCode>
            <ChargeDeduction>
              <ChargesTypeCode>AS</ChargesTypeCode>
              <OtherChargeDeductionAmount currencyID="GBP">100</OtherChargeDeductionAmount>
            </ChargeDeduction>
          </CustomsValuation>
        })

        itemCustomsValuation.toXml shouldBe Some(expectedXml)
      }
    }

//    "not generate the ItemCustomsValuation XML element" when {
//      "non of the child values are present" in {
//        val origin = ItemCustomsValuation()
//        origin.toXml shouldBe None
//      }
//    }

  }

}
