package com.automationlearning.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.automationlearning.xls.read.Read_xls;

public class DriverScript 
{
	public static Logger appLogs;

	//Master suit.xlsx
	public Read_xls suiteXLS;
	public int currentSuiteID;
	public String currentTestSuit;

	//Single Test Suite.xlsx
	public Read_xls currentTestSuitXLS;
	public int currentTestCaseID;
	public String currentTestCaseName;
	public int currentTestStepID;
	public String currentKeyword;
	public int currentTestDatasetID;

	public Keywords keywords;
	public Method[] method;
	public String keyword_Execution_results;
	
	//Variables Holding Results
	ArrayList<String> resultSet;
	LinkedHashMap<String, ArrayList<String>> TestCasesResults;
	LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> TestSuitesResults;
	static LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>>> MasterSuiteSheetResults;
	static htmlReporting htmlReport;
	
	//Properties
	public static Properties config;
	public static Properties OR;
	
	//for holding data for Keywords execution
	public String data;
	public String object;
	
	/* Initialize Collections API in 
	 * Class Constructor
	 */
	public DriverScript()
	{
		keywords = new Keywords();
		method = keywords.getClass().getMethods();			
		MasterSuiteSheetResults = new LinkedHashMap<>();	
	}
	
	public static void main(String[] args) 
	{
		//Initializing Properties file location
		try 
		{
			FileInputStream fileStream = new FileInputStream(System.getProperty("user.dir") +"//src//com//automationlearning//config//config.properties" );
			config = new Properties();
			config.load(fileStream);
			
			fileStream = new FileInputStream(System.getProperty("user.dir") +"//src//com//automationlearning//config//OR.properties" );
			OR = new Properties();
			OR.load(fileStream);
			
		} catch (FileNotFoundException e) {
			System.out.println("Something is Wrong with config properties file location");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DriverScript test =new DriverScript();
		test.start();
		
		htmlReport = new htmlReporting();
		htmlReport.checkResultDir();	
		htmlReport.HtmlReport(MasterSuiteSheetResults);
		
	}

	// Initialize the app logs
	public void start()
	{
		appLogs = Logger.getLogger("devpinoyLogger");
		appLogs.debug("Properties file loaded. Starting Test Scripts");
		appLogs.debug("Loading Suite.xls");
		suiteXLS = new Read_xls(System.getProperty("user.dir") +"//src//com//automationlearning//xls//Suite.xls" );
		System.out.println(suiteXLS.toString());
		System.out.println(suiteXLS.getRowCount(Constants.Test_Suit_Sheet));

		ArrayList<String> rMode = suiteXLS.getColumnData("Test_Suite", "Runmode");
		System.out.println(rMode);

		/*	1. Check the run mode of Test Suite
			2. Runmode of test cases in Test suite
			3. Execute Keywords of the test cases serially 
			4. Execute keywords as many times as given
			Number of Datasets set to Y
		 */

		// Iterate in Master suite.xls
		for(currentSuiteID=2; currentSuiteID<= (suiteXLS.getRowCount(Constants.Test_Suit_Sheet)); currentSuiteID++ )
		{	
			currentTestSuit = suiteXLS.getCellData(Constants.Test_Suit_Sheet, Constants.Test_Suit_Id, currentSuiteID);
			System.out.println("MasterSheet: " + suiteXLS.getCellData(Constants.Test_Suit_Sheet,Constants.Test_Suit_Id, currentSuiteID)  +suiteXLS.getCellData(Constants.Test_Suit_Sheet,Constants.Runmode, currentSuiteID));
			appLogs.debug("Iterating in current TestSuit at row :" + currentSuiteID + "--> " +currentTestSuit);
			
			String TestSuites = suiteXLS.getCellData(Constants.Test_Suit_Sheet,Constants.Test_Suit_Id, currentSuiteID);
			
			// Iterate in TestSuite Based on Runmode
			if(suiteXLS.getCellData(Constants.Test_Suit_Sheet, Constants.Runmode, currentSuiteID).equals(Constants.RunMode_Yes))
			{
				currentTestSuitXLS = new Read_xls(System.getProperty("user.dir") +"//src//com//automationlearning//xls//" + currentTestSuit + ".xls" );
				System.out.println(currentTestSuitXLS.getRowCount(Constants.TestCases_Sheet));
				
				//Initialize hashmap before iterating in the sheets 
				TestSuitesResults = new LinkedHashMap<>();
				
				MasterSuiteSheetResults.put(TestSuites, TestSuitesResults);
				
				
				//Iterate Through all the test cases in the suite 
				for (currentTestCaseID = 2; currentTestCaseID<= currentTestSuitXLS.getRowCount(Constants.TestCases_Sheet); currentTestCaseID++) 
				{
					appLogs.debug (currentTestSuitXLS.getCellData(Constants.TestCases_Sheet, Constants.Test_Suit_Id, currentTestCaseID) + "--->" + currentTestSuitXLS.getCellData(Constants.TestCases_Sheet, Constants.Runmode, currentTestCaseID));
					//currentSuitXLS.readSheet("Test Cases");
					
					currentTestCaseName = currentTestSuitXLS.getCellData(Constants.TestCases_Sheet, Constants.Test_Case_Id, currentTestCaseID); 					
					System.out.println(currentTestCaseName);
					System.out.println(currentTestSuitXLS.getCellData(Constants.TestCases_Sheet, Constants.Runmode, currentTestCaseID));
					
					
					//Verify if Testcase contains Y
					if(currentTestSuitXLS.getCellData(Constants.TestCases_Sheet, Constants.Runmode, currentTestCaseID).equals(Constants.RunMode_Yes))
					{
						appLogs.debug("Executing Test cases -- " + currentTestCaseName );
						
						//Verify if there is any test data sheet present for the test case executing 
						if(currentTestSuitXLS.isSheetExist(currentTestCaseName))
						{
							
							//Run as many times as number of test data sets with Runmode Y
							for(currentTestDatasetID=2; currentTestDatasetID<= currentTestSuitXLS.getRowCount(currentTestCaseName); currentTestDatasetID++)
							{	
								
								appLogs.debug("Iterations Number: " + (currentTestDatasetID-1));
								//Checking the RunMode for the current Dataset
								if(currentTestSuitXLS.getCellData(currentTestCaseName, Constants.Runmode, currentTestDatasetID).equals(Constants.RunMode_Yes))
								{
									//Iterate when there is Test Data Sheet and post the results in XLS
									executeKeywords();		
								}
							}
						}
						else
						{
							//Iterate when there is no Test Data Sheet and post the results in XLS
							executeKeywords();			
						}
					}	
				}				
			}		
		}
		System.out.println("Result Set After sheet execution: " + MasterSuiteSheetResults);
	}	

	/*   
	 * public void executeKeywords() method specification :-
	 * This is a function to iterate all the keywords in the sheet
	 * This will also add results in an array results which is
	 * in XLS Reports methods	
	 */

	public void executeKeywords()
	{
		
		//Create a new hashmap object everytime a sheet a new testcase is run
		TestCasesResults =  new LinkedHashMap<>();
		
		//Iterating through all Keywords in Test Steps sheet
		for(currentTestStepID=2; currentTestStepID<=(currentTestSuitXLS.getRowCount(Constants.TestSteps_Sheet)); currentTestStepID++)
		{
			//Holding data in a variable of column "Data" 
			data = currentTestSuitXLS.getCellData(Constants.TestSteps_Sheet, Constants.Data, currentTestStepID);
			object = currentTestSuitXLS.getCellData(Constants.TestSteps_Sheet, Constants.Object, currentTestStepID);
			String TestSteps = currentTestSuitXLS.getCellData(Constants.TestSteps_Sheet, Constants.Test_Step_Id, currentTestStepID);
			
			//Verify if TCID in test steps is equal to Name of the test cases executing
			if(currentTestCaseName.equals(currentTestSuitXLS.getCellData(Constants.TestSteps_Sheet, Constants.Test_Case_Id, currentTestStepID)))
			{	
				currentKeyword = currentTestSuitXLS.getCellData(Constants.TestSteps_Sheet, Constants.Keywords, currentTestStepID);
				appLogs.debug(currentKeyword);
				
				/* Reflection API: check if the a corresponding method for every keyword 
				 * in Keywords.java file, if exist then invoke it*/
				
				for (Method iterator : method) 
				{
					if(iterator.getName().equals(currentKeyword))
					{
						try 
						{
							keyword_Execution_results = (String) iterator.invoke(keywords, object, data);
							appLogs.debug(keyword_Execution_results);
							
							//Load all the results in an array
							resultSet = new ArrayList<String>();
							resultSet.add(keyword_Execution_results); // This will throw error when tests with no datasheet is executed
							
							//Load all the results  with there respective test ID's into hashmap
							TestCasesResults.put(TestSteps , resultSet);
							
							//Load all the results along with the testcase name
							TestSuitesResults.put(currentTestCaseName, TestCasesResults);
							
							
						} catch (IllegalAccessException
								| IllegalArgumentException
								| InvocationTargetException e) 
						{
							System.out.println("There is some error in keywords invoking");
							e.printStackTrace();
						}	
					}	
				} 
			}				
		}	
		
	}
	
}
