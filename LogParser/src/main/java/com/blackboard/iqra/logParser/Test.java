package com.blackboard.iqra.logParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Test {

	public static void main(String[] args) throws InvalidFormatException, IOException {
		Workbook wb = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\aarij\\Downloads\\hhh.xlsx")));
		
		Sheet sheet = wb.getSheetAt(0);
		
		for(Row row : sheet) {
			String value  =row.getCell(0).getStringCellValue().stripLeading();
			String [] a = value.split("        ");
			
			System.out.println(a[0]+"--"+a[1]);
		}
	}

}
