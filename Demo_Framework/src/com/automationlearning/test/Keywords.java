package com.automationlearning.test;

import static com.automationlearning.test.DriverScript.appLogs;
import static com.automationlearning.test.DriverScript.config;
import static com.automationlearning.test.DriverScript.OR;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

//This class contains one function per keyword
public class Keywords 
{

	public WebDriver driver;
	
	/* 
	 * public String openBrowser(String object, String data) method specification :-
	 * This method will open the browser based on the browser type defined in config properties
	 * Object --> Blank
	 * data--> browserType
	 */	
	public String openBrowser(String object, String data)
	{
		appLogs.debug("Opening the Browsers");
		if(config.getProperty(data).equalsIgnoreCase("firefox"))
			driver = new FirefoxDriver();	
		else if(config.getProperty(data).equalsIgnoreCase("Chrome"))
		{	
			System.setProperty("webdriver.chrome.driver",
					"//Users//ishan//Desktop//TestAuto//My_Framework//chromedriver"); 
			driver = new ChromeDriver();
		}
		return Constants.Pass;
	}
	
	
	/* 
	 * public String navigate(String object, String data) method specification :-
	 * This method will navigate to the given URL in config File
	 * Object --> Blank
	 * data--> SiteURL
	 */	
	
	public String navigate(String object, String data)
	{
		appLogs.debug("Navigating to URL");
		try{
		driver.navigate().to(config.getProperty(data));
		}catch(Exception e){
			return Constants.Fail + "----> Not able to navigate, Error is: " + e ;
		}
		return Constants.Pass;
	}
	
	/*
	 * public String clickLink(String object, String data) method specification
	 * :- This method will click on the link defined in the OR.properties -->
	 * Xpath of the Link is from the OR Sheet data--> Blank
	 */

	public String clickLink(String object, String data) 
	{
		appLogs.debug("Clicking on the Link");
		try {
			driver.findElement(By.xpath(OR.getProperty(object))).click();

		} catch (Exception e) {
			return Constants.Fail + "----> Not able to click, Error is: " + e;
		}
		return Constants.Pass;
	}
	
	/*
	 * public String assertLinkText(String object, String data)
	 * This method will verify the link defined in the OR.properties -->
	 * and will compare the data from the sheet--> 
	 */
	public String assertText(String object, String data)
	{
		appLogs.debug("Verifying Content");
		try {
			String actual = driver.findElement(By.xpath(OR.getProperty(object))).getText().trim();
			String expected = OR.getProperty(data);
			if(actual.equals(expected))
				return Constants.Pass;
			else
				return Constants.Fail;
		} catch (Exception e) {
			return Constants.Fail + "----> Verification Failed, Error is: " + e;
		}
	}
	
	/*
	 * public String enterText(String object, String data)
	 * This method will locate the input box and
	 * will enterText defined in the OR.properties --> 
	 */
	public String enterText(String object, String data)
	{
		appLogs.debug("Entering the value");
		try {
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(OR.getProperty(data));
			return Constants.Pass;
			
		} catch (Exception e) {
			return Constants.Fail + "----> Input value Failed, Error is: " + e;
		}
	}
	
	/*
	 * public String click(String object, String data)
	 * This method will locate the input box and
	 * will enterText defined in the OR.properties --> 
	 */
	public String click(String object, String data)
	{
		appLogs.debug("Clicking on any element");
		try {
			driver.findElement(By.xpath(OR.getProperty(object))).click();
			return Constants.Pass;
			
		} catch (Exception e) {
			return Constants.Fail + "----> Click Failed, Error is: " + e;
		}
	}
	
	
	/*
	 * public String quit(String object, String data)
	 * This method will quit the current browser
	 */
	public String quit(String object, String data)
	{
		appLogs.debug("Tests quit");
		try {
			driver.quit();
			return Constants.Pass;
		} catch (Exception e) {
			return Constants.Fail + "----> Click Failed, Error is: " + e;
		}
	}
	
	

	public String clickButton()
	{
		appLogs.debug("clicking on the Button");
		return Constants.Pass;	
	}


	public String selectRadio()
	{
		appLogs.debug("Selecting a Radio button");
		return Constants.Pass;

	}

	


}
