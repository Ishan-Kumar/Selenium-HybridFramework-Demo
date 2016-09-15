package com.automationlearning.xls.read;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class Read_xls 
{	
	public String path;
	public FileInputStream inputStream;  //Create an object of the file input stream class to read excell file
	public FileOutputStream outputStream;
	public Workbook objWorkbook = null ;
	public File file ;
	public Cell cell;
	public Row row;

	 /* public Xls_Reader(String path) Constructor specification :-
	 	1) Specify the path of the xls file and set a file input stream with the file.
	 	2) path -> Path of the xls file 
		3) FileInputStream -> To take input from specified xls file.
	 */

	public Read_xls(String path)
	{
		this.path=path;
		try 
		{

			inputStream = new FileInputStream(path);
			objWorkbook = new HSSFWorkbook(inputStream);

		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	 /*   
	 	Public int getRowCount(String sheetName) method specification :-
	 	1)  Return total number of rows in specified excel sheet.
	 	2)  sheetName -> name of the excel sheet. 
	 	3)  Workbook -> Refer to the whole xls file.
	 	4)  objSheet -> Refer to an individual sheet within that workbook.
	 */

	public int getRowCount(String sheetName)
	{
		objWorkbook.getSheet(sheetName);
		Sheet objSheet = objWorkbook.getSheet(sheetName);
		int rowCount = objSheet.getLastRowNum() - objSheet.getFirstRowNum();		
		return rowCount+1;
	}

	 /*   
	 	Public int getCellData(String sheetName, int rowNum, int colNum) method specification :-
	 	1)  Return Data inside a particular cell
	 	2)  sheetName -> name of the excel sheet. 
	 	3)  rowNum -> Refer to the Row Number in xls file
	 	4)  colNum -> Refer to the Col Number in xls file
	 */

	public String getCellData(String sheetName, int rowNum, int colNum)
	{
		Sheet objSheet = objWorkbook.getSheet(sheetName);
		row = objSheet.getRow(rowNum);
		return row.getCell(colNum).getStringCellValue();
	}		


	/*   
 		public String getCellData(String sheetName, String colName, int currentSuiteID) method specification :-
 		1)  Return Data inside a particular cell of a particular column
 		2)  sheetName -> name of the excel sheet. 
 		3)  colName -> Refer to the ColumnHeader in xls file
 		4)  currentSuiteID -> Refer to the rowNumber Number in xls file
	 */

	public String getCellData(String sheetName, String colName, int currentSuiteID)
	{
		Sheet objSheet = objWorkbook.getSheet(sheetName);
		row = objSheet.getRow(0);
		int colIndex =0;
		
		for(int i=0;i<row.getLastCellNum();i++){
			if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName)){
				colIndex= row.getCell(i).getColumnIndex();
			}
		}		
		if((objSheet.getRow(currentSuiteID-1).getCell(colIndex))!=null){
			return objSheet.getRow(currentSuiteID-1).getCell(colIndex).getStringCellValue().trim();
		} else
			return null;
	}

	 /*   
		public void setCellData(String sheetName, String colName, int rowNum, String status) method specification :-
		1)  sheetName -> name of the excel sheet. 
		2)  colName -> Refer to the ColumnHeader in xls file
		3)  rowNUM -> Refer to the rowNumber Number in xls file
		4)	Status -> Input Value for the cell
	 */

	public void setCellData(String sheetName, String colName, int rowNum, String status) 
	{
		Sheet objSheet = objWorkbook.getSheet(sheetName);
		row = objSheet.getRow(0);
		int colIndex =0;

		for(int i=0;i<row.getLastCellNum();i++)
		{
			if(row.getCell(i).getStringCellValue().equalsIgnoreCase(colName))
			{
				colIndex= row.getCell(i).getColumnIndex();
			}
		}

		objSheet.getRow(rowNum).createCell(colIndex).setCellValue(status);
		try 
		{
			outputStream = new FileOutputStream(path);
			objWorkbook.write(outputStream);			
			outputStream.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	/*   
		Public int isSheetExist(String sheetName) method specification :-
		Returns boolean value if the sheet is present in current workbook or not
	 */
	public boolean isSheetExist(String sheetName)
	{

		if(objWorkbook.getSheet(sheetName) == null)
		{
			return false ;
		}else
			return true;

	}


	/*   
 		Public int readSheet(String sheetName) method specification :-
 		1)  Print Sheet data in console
 		2)  sheetName -> name of the excel sheet. 
	 */

	public void readSheet(String sheetName)
	{

		Sheet objSheet = objWorkbook.getSheet(sheetName);

		int rowCount = objSheet.getLastRowNum() - objSheet.getFirstRowNum();

		// Create a loop over all the rows of excel file
		for(int i = 0; i< rowCount+1; i++)
		{
			row = objSheet.getRow(i);			
			for(int j=0; j < row.getLastCellNum();j++)
			{
				System.out.print(row.getCell(j).getStringCellValue() + " | "); 
			}
			System.out.println();

		}	
	}

	/*   
	 	Public ArrayList<String> getColumnData(String sheetName, String colName) method specification :-
	 	1)  Return Data of a particular column
	 	2)  sheetName -> name of the excel sheet. 
	 	3)  colName -> Refer to the Name of the column for which data is required
	 */

	public ArrayList<String> getColumnData(String sheetName, String colName)
	{
		Sheet objSheet = objWorkbook.getSheet(sheetName);
		int rowCount = objSheet.getLastRowNum() - objSheet.getFirstRowNum();
		int colIndex = 0;
		ArrayList<String> columnData = new ArrayList<String>();
		row = objSheet.getRow(0);

		for(int i=0;i<row.getLastCellNum();i++)
		{
			if(row.getCell(i).getStringCellValue().equalsIgnoreCase(colName))
			{
				colIndex= row.getCell(i).getColumnIndex();
			}

		}		

		for(int i=1;i<rowCount+1;i++)
		{
			row = objSheet.getRow(i);
			columnData.add(row.getCell(colIndex).getStringCellValue());		
		}

		return columnData;
	}



}