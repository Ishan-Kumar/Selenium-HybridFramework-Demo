package com.automationlearning.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class htmlReporting 
{
	public  FileWriter htmlFile = null;
	public  BufferedWriter out = null;


	/* Creates a new Results Directory, if not present. Deletes all the existing
	 * files form the Results Directory if already present. Creates a new
	 * Screenshot Directory inside the Reullts Directory
	 */
	public void checkResultDir() 
	{
		File dir = new File(System.getProperty("user.dir") + "/Results");

		// Delete all the previous files and folders in the Reuslts Directory if its not empty 
		if (dir.exists()) 
		{
			File[] allFiles = dir.listFiles();
			for (int i = 0; i < allFiles.length; i++) 
			{
				allFiles[i].delete();
			}
		} else {
			new File(System.getProperty("user.dir") + "/Results").mkdir();
		}//End of if block

		// Make a new screenshot directory inside the Results Directory
		new File(System.getProperty("user.dir") + "/Results/Screenshots").mkdir();
	}

	/* 
	 * HtmlReport( LinkedHashMap<String, LinkedHashMap<String, 
	 * LinkedHashMap<String, ArrayList<String>>>> MasterSuiteSheetResults)  method specification :-
	 * This method Will Make the results folder in given location and will generate Test report inside that folder.  
	 * MasterSuiteSheetResults --> Yes correct, All the execution results is in this Super mighty hashmap.
	 */

	public void HtmlReport( LinkedHashMap<String, LinkedHashMap<String, 
			LinkedHashMap<String, ArrayList<String>>>> MasterSuiteSheetResults) 
	{
		htmlFile = null;
		out = null;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		try 
		{
			htmlFile = new FileWriter(System.getProperty("user.dir") + "/Results/index.html", true);
			out = new BufferedWriter(htmlFile);
			out.newLine();

			out.write(
					"<html>\n"
							+ "		<head>\n"
							+ "		<title>Test Execution Report</title>\n"
							+ "		</head>\n"
							+ "			<body>\n"
							+ "				<br>\n"
							+ "				<h1 style='text-align:center;'> Automation Test Report: Generated On "
							+ 					dateFormat.format(date) + " at "
							+ 					timeFormat.format(date) + "</h1>\n" + "				<br>\n"
							+ "				<br>\n" + "</body>\n" + "		"
							+ "</html>\n"
					);

			for (String keys : MasterSuiteSheetResults.keySet()) 
			{
				out.write("	<html>\n"
						+ "		<head>\n" 
						+ "		<title>Test Execution Report</title>\n" 
						+ "		</head>\n" 
						+ "			<body>\n" + "				<br>\n"
						+ "				<table style=' width:20%; border: 1px solid gray;  margin-left: 100px; '>\n"
						+ "            <br>\n"
						+ "            <br>\n"
						+ "            <tr>\n"
						+ "				<td style='color:Black; border: hidden hidden solid hidden 1px black; align:center; background-color: #d9edf7'><b> FILE NAME </b></td>\n"
						+ "						<td style='color:Black; border: hidden hidden solid hidden 1px black 1px black; align:center; background-color:	#F8F8F8  '><b>"
						+                     keys + "</b></td>\n" + "</tr>\n" + "</table>\n " 
						+ "              <table style=' width:80%; border: 1px solid black; align:center;align:center;margin-left: 100px '>\n"
						+ "              	<thead>\n" + "						"
						+ "                  <tr>\n" 
						+ "						<th style=' width:10%; border: solid 1px black; background-color: #153E7E;"
						+ "							color: white;'> TestSuite NAME </th>\n" +
						" 							<th style='width:8%; border: solid 1px black; background-color: #153E7E;"
						+ " 							color: white;'> TestCaseID </th>\n" +
						"							<th style=' width:20%; border: solid 1px black; background-color: #153E7E;"
						+ "							color: white;'> STATUS </th>\n" + "						"
						+ "                  </tr>\n" 
						+ "                  </thead>\n" 
						+ "					<tbody>\n");


				//Store all the values in TestData paired with its key.
				LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> TestData= MasterSuiteSheetResults.get(keys);

				for(String TestCaseExecuted: TestData.keySet())
				{ 
					//Foreach TestData extract it's value in TestcaseData
					LinkedHashMap<String, ArrayList<String>> TestCaseData = TestData.get(TestCaseExecuted);

					//Extract Testcases ID and initialize iterator
					Set<String> TestCaseID = TestCaseData.keySet();
					Iterator<String> TestCaseID_Iterator = TestCaseID.iterator();

					//Extract Testcases Results and initialize iterator
					Collection<ArrayList<String>> ResultsData = TestCaseData.values();
					Iterator<ArrayList<String>>  TestCaseResults= ResultsData.iterator();

					while(TestCaseID_Iterator.hasNext())
					{
						String result = TestCaseResults.next().toString();

						out.write("<tr>\n");
						out.write("<td style='color:black; border: solid 1px black; align:center;'> "+ TestCaseExecuted+ "</td>\n");
						out.write("<td style='color:black; border: solid 1px black; align:center;'> "+ TestCaseID_Iterator.next() + "</td>\n");

						if(result.contains("Pass")){
							out.write("<td style='color:black; border: solid 1px black; align:center;'> "+ result + "</td>\n");
						}else if(result.contains("Fail")) {
							out.write("<td style='color:black; border: solid 1px black; align:center;'bgcolor=#FA5858> "+ result + "</td>\n");
						}

						out.write("</tr>\n");

					}// End of whileloop
				}//End of Forloop
			}// End of Mighty Foreach loop 
			out.close();
		}// Enough things are tried
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}//Mighty Method Ends
}// Class Ends
