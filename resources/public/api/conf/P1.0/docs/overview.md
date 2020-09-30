This API allows government departments to get information from HM Revenue and Customs (HMRC) about individuals’ benefits. The data will help with processes like:
* means testing
* fraud detection and prevention
* debt recovery

Data will be filtered using different scopes so that only permitted data is shared.

Use this API to get information about an individual’s tax credits like:
* entitlement
* pay frequency
* gross amount 

The responses exclude fields when they do not have a value. 

This API is a HAL HATEOAS RESTful API. This promotes discoverability and is self documenting.

A HATEOAS API makes it clear to client software what can be done when an action is completed. In other words, what you need to know when you need to know it. New functionality can be added without breaking your client software.

This API is still under development and many enhancements are planned. We recommend following the HATEOAS approach from the outset, so that your work is not affected by changes.

Follow links as they are presented to you in the API at runtime. This will prevent you from building state into your client, and will decouple you from changes to the API.

The default Media Type for responses is hal+json. 