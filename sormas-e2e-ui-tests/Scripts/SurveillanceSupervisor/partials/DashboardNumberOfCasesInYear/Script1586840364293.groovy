import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable

WebUI.setText(findTestObject('Login/input_username_username'), GlobalVariable.gUsernameSecondSurveillanceSupervisor)

WebUI.setText(findTestObject('Login/input_password_password'), GlobalVariable.gPasswordSecondSurveillanceSupervisor)

WebUI.click(findTestObject('Login/div_Log in'))

WebUI.click(findTestObject('Login/MainView/menu_Dashboard'))

WebUI.waitForElementPresent(findTestObject('Surveillance/Dashboard/caseRangeWeek'), 3)

WebUI.click(findTestObject('Surveillance/Dashboard/caseRangeWeek'))

WebUI.click(findTestObject('Surveillance/Dashboard/caseRangeYear'))

numberOfCases = WebUI.getText(findTestObject('Surveillance/Dashboard/numberOfCases'))

// WebUI.click(findTestObject('Login/span_Logout_link'))
return numberOfCases

