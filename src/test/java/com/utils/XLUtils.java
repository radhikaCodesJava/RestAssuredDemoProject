package com.utils;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLUtils {
	
	public static FileInputStream fis;
	
	public static FileOutputStream fos;
	
	public static XSSFWorkbook wbook;
	
	public static XSSFSheet wsheet;
	
	public static XSSFRow row;
	
	public static XSSFCell cell;
	
	public static int getRowCount(String XLFileName, String XLSheetName) throws IOException
	{
		
		fis=new FileInputStream(XLFileName);
		
		wbook=new XSSFWorkbook(fis);
		
		wsheet=wbook.getSheet(XLSheetName);
		
		int RowCount=wsheet.getLastRowNum();
		
		wbook.close();
		fis.close();
		 
		return RowCount;
	}
	
	public static int getCellCount(String XLFileName, String XLSheetName, int rownum) throws IOException
	{
		
		fis=new FileInputStream(XLFileName);
		
		wbook=new XSSFWorkbook(fis);
		
		wsheet=wbook.getSheet(XLSheetName);
		
	    row=wsheet.getRow(rownum);
		
		//int celCount=wsheet.getLastRowNum();
		int cellCount=row.getLastCellNum();
		
		wbook.close();
		fis.close();
		 
		return cellCount;
	}
	
	
	public static String GetCellData(String XLFileName, String XLSheetName, int rownum, int cellnum) throws IOException
	{
		
		fis=new FileInputStream(XLFileName);
		
		wbook=new XSSFWorkbook(fis);
		
		wsheet=wbook.getSheet(XLSheetName);
		
	    row=wsheet.getRow(rownum);
		
		cell=row.getCell(cellnum);
		
		String data;
		try {
			DataFormatter formatter=new DataFormatter();
			String cellData=formatter.formatCellValue(cell);
			return cellData;
		} 
		catch (Exception e)
		{
			data="";
		}
		
		wbook.close();
		fis.close();
		return data;
		
	}
	
	public static void SetCellData(String XLFileName, String XLSheetName, int rownum, int cellnum, String data) throws IOException
	{
		
		fis=new FileInputStream(XLFileName);
		
		wbook=new XSSFWorkbook(fis);
		
		wsheet=wbook.getSheet(XLSheetName);
		
	    row=wsheet.getRow(rownum);
		
		cell=row.getCell(cellnum);
		
		cell.setCellValue(data);
		
		fos=new FileOutputStream(XLFileName);
		
		wbook.write(fos);
		
		wbook.close();
		
		fis.close();
		
		fos.close();
		
		
	}

}
