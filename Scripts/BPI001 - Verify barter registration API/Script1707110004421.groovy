import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.apache.commons.lang.RandomStringUtils

/*------------------------------------------------------------------------------------------------
 * Package → Add classes 
 *
 * STEPS:
 * - Test to sending request  without authorization token → Shall failed (verify response code : 400 + error message if any )
 * - Test to sending request with invalid authorization token → Shall failed (verify response code : 401 + error message if any  )
 * - Test to sending request with invalid package id  → shall failed (verify response code : 404 + error message if any)
 * - Test to sending request add class with valid authorization token , valid package id → Create success (verify response code : 200 + success message if any  ) 
 * - After add class success → Verify those classes inside the package from superadmin are correctly added
 * 
 *
 * TEARDOWN:
 * - Delete the created classes from superadmin
 *------------------------------------------------------------------------------------------------*/


/*--------------------------------------------------------
 * TEST DATA
 * -------------------------------------------------------*/

def invalidToken = RandomStringUtils.randomAlphanumeric(100)
def invalidPackageUUID = UUID.randomUUID().toString()
def existingExternalCourseInstanceId = "urn:learningbase:package:class:test-api-package-post-class-0001"

def validPackageUUID = "64d6bd65-8f9a-41fc-8e38-25311aeac811"
def validExternalCourseInstanceId = "urn:learningbase:package:class:" + UUID.randomUUID().toString()

def mandatoryPackageErrorMessage = "Request is empty"
def invalidPackageErrorMessage = "Invalid PackageId."
def mandatoryExternalCourseErrorMessage = "The ExternalCourseInstanceId field is required."
def existingExternalCourseErrorMessage = "The same External Course Instance Id exists in database."

def selectedOrg = "DEFAULT"
def packageCode = "PKG-API-POST"

/*--------------------------------------------------------
 * EXECUTION
 * -------------------------------------------------------*/

// GET token
def getTokenResponse = WS.sendRequest(findTestObject('LBAT_API/Token_Get', [('clientId') : GlobalVariable.ValidClientId, ('clientSecret') : GlobalVariable.ValidClientSecret]))
WS.verifyResponseStatusCode(getTokenResponse, 200)
def validToken = WS.getElementPropertyValue(getTokenResponse, 'Token')


// Test to sending request  without authorization token → Shall failed (verify response code : 400 + error message if any )
def withoutTokenResponse = WS.sendRequest(findTestObject('LBAT_API/Package_Post_Classes', [('id') : '', 
							('token') : '', ('externalCourseInstanceId') : '']))

WS.verifyResponseStatusCode(withoutTokenResponse, 400)
WS.verifyElementPropertyValue(withoutTokenResponse, 'ErrorMessages[0]', GlobalVariable.API_ErrorMessage_TokenNotFound)


// Test to sending request with invalid authorization token → Shall failed (verify response code : 401 + error message if any  )
def invalidTokenResponse = WS.sendRequest(findTestObject('LBAT_API/Package_Post_Classes', [('id') : '', 
							('token') : invalidToken, ('externalCourseInstanceId') : '']))

WS.verifyResponseStatusCode(invalidTokenResponse, 401)
WS.verifyElementPropertyValue(invalidTokenResponse, 'ErrorMessages[0]', GlobalVariable.API_ErrorMessage_TokenIncorrect)


// Test to sending request with invalid package id  → shall failed (verify response code : 404 + error message if any)

// Test mandatory package UUID
def mandatoryPakcageUUIDResponse = WS.sendRequest(findTestObject('LBAT_API/Package_Post_Classes', [('id') : '',
									('token') : validToken, ('externalCourseInstanceId') : '']))

WS.verifyResponseStatusCode(mandatoryPakcageUUIDResponse, 400)
WS.verifyElementPropertyValue(mandatoryPakcageUUIDResponse, 'ErrorMessages[0]', mandatoryPackageErrorMessage)


// Test mandatory ExternalCourseInstanceId
def mandatoryExternalCourseResponse = WS.sendRequest(findTestObject('LBAT_API/Package_Post_Classes', [('id') : invalidPackageUUID,
										('token') : validToken, ('externalCourseInstanceId') : '']))

WS.verifyResponseStatusCode(mandatoryExternalCourseResponse, 400)
WS.verifyElementPropertyValue(mandatoryExternalCourseResponse, 'ErrorMessages[0]', mandatoryExternalCourseErrorMessage)


// Test invalid package UUID
def invalidPakcageUUIDResponse = WS.sendRequest(findTestObject('LBAT_API/Package_Post_Classes', [('id') : invalidPackageUUID,
								('token') : validToken, ('externalCourseInstanceId') : existingExternalCourseInstanceId]))

WS.verifyResponseStatusCode(invalidPakcageUUIDResponse, 404)
WS.verifyElementPropertyValue(invalidPakcageUUIDResponse, 'ErrorMessages[0]', invalidPackageErrorMessage)


// Test existing ExternalCourseInstanceId
def existingExternalCourseResponse = WS.sendRequest(findTestObject('LBAT_API/Package_Post_Classes', [('id') : validPackageUUID,
									('token') : validToken, ('externalCourseInstanceId') : existingExternalCourseInstanceId]))

WS.verifyResponseStatusCode(existingExternalCourseResponse, 400)
WS.verifyElementPropertyValue(existingExternalCourseResponse, 'ErrorMessages[0]', existingExternalCourseErrorMessage)


// Test to sending request add class with valid authorization token , valid package id → Create success (verify response code : 200 + success message if any  )
def validRequestResponse = WS.sendRequest(findTestObject('LBAT_API/Package_Post_Classes', [('id') : validPackageUUID,
							('token') : validToken, ('externalCourseInstanceId') : validExternalCourseInstanceId]))

WS.verifyResponseStatusCode(validRequestResponse, 200)
def classId = WS.getElementPropertyValue(validRequestResponse, 'id')


// After add class success → Verify those classes inside the package from superadmin are correctly added

// Login as super admin and go to packages menu
WebUI.callTestCase(findTestCase('Packages (PKG)/Free Package/PKG9001 - Generic Test login and click Packages menu'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.selectOptionByLabel(findTestObject('Page_Packages/select_SelectOrganization'), selectedOrg, true)
WebUI.delay(GlobalVariable.DelayMedium)

WebUI.click(findTestObject('Page_Packages/tab_published'))
WebUI.delay(GlobalVariable.DelayMedium)

WebUI.setText(findTestObject('Page_Packages/input_SearchPublishedTab'), packageCode)
WebUI.delay(GlobalVariable.DelayMedium)

TestObject packageLinkObject = new TestObject('publishedLinkObject')
String packageXpath = "//a[contains(text(),'" + packageCode + "')]"
packageLinkObject.addProperty("xpath", com.kms.katalon.core.testobject.ConditionType.EQUALS, packageXpath)

WebUI.waitForElementClickable(packageLinkObject, GlobalVariable.DelayShort)
WebUI.click(packageLinkObject)
WebUI.delay(GlobalVariable.DelayMedium)

WebUI.scrollToElement(findTestObject('Page_Packages/textarea_PackageDescription'), GlobalVariable.DelayShort)
WebUI.delay(GlobalVariable.DelayMedium)
WebUI.click(findTestObject('Page_Packages/Manage_Classes/a_ManageClasses'), FailureHandling.CONTINUE_ON_FAILURE)
WebUI.delay(GlobalVariable.DelayMedium)

TestObject classLinkObject = new TestObject('createLinkObject')
String classXpath = "//a[@id='" + classId + "']"
classLinkObject.addProperty("xpath", com.kms.katalon.core.testobject.ConditionType.EQUALS, classXpath)

WebUI.verifyElementPresent(classLinkObject, GlobalVariable.DelayMedium)
WebUI.waitForElementClickable(classLinkObject, GlobalVariable.DelayMedium)
WebUI.click(classLinkObject)
WebUI.delay(GlobalVariable.DelayLong)


/*--------------------------------------------------------
 * TEARDOWN : Delete the created classes from superadmin
 * -------------------------------------------------------*/

WebUI.click(findTestObject('Page_Packages/Manage_Classes/button_DeleteClass'))
WebUI.delay(GlobalVariable.DelayShort)
WebUI.click(findTestObject('Page_Packages/Manage_Classes/button_ConfirmDeleteClass'))
WebUI.delay(GlobalVariable.DelayMedium)
