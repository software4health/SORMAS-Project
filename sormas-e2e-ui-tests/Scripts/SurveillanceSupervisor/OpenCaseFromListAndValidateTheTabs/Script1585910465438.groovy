import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

WebUI.callTestCase(findTestCase('Login/partials/LoginAsSurveillanceSupervisor'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.callTestCase(findTestCase('SurveillanceSupervisor/partials/SwitchToCases'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.click(findTestObject('Surveillance/SearchView/a_Search_Entry_link'))

WebUI.verifyElementPresent(findTestObject('Surveillance/CaseView/Case/input_Case_CaseIdUuid_inputBox'), 3)

WebUI.click(findTestObject('Object Repository/Surveillance/CaseView/span_CasePerson_tab'))

WebUI.verifyElementPresent(findTestObject('Surveillance/CaseView/Person/input_Person_PassportNumber_inputBox'), 3)

WebUI.click(findTestObject('Object Repository/Surveillance/CaseView/span_Hospitalization_tab'))

WebUI.verifyElementPresent(findTestObject('Surveillance/CaseView/Hospitatation/div_Date of visit or admission'), 
    3)

WebUI.click(findTestObject('Object Repository/Surveillance/CaseView/span_Symptoms_tab'))

WebUI.verifyElementPresent(findTestObject('Surveillance/CaseView/SymptomsTab/input_Comments_symptomsComments'), 3)

WebUI.click(findTestObject('Object Repository/Surveillance/CaseView/span_EpidemiologicalData_tab'))

WebUI.verifyElementPresent(findTestObject('Surveillance/CaseView/Epidemiological data/i_EpidemiologicalData_description_text'), 
    3)

WebUI.click(findTestObject('Object Repository/Surveillance/CaseView/span_Contacts_tab'))

WebUI.verifyElementPresent(findTestObject('Contacts/CasesView/NewContact/div_New contact'), 3)

if (isStandalone) {
    WebUI.closeBrowser()
}

