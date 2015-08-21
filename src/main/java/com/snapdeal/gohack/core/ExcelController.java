package com.snapdeal.gohack.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcelController {

	@Autowired
	private ExcelBuilder excelBuilder;
	@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)

	public void downloadExcel(HttpServletResponse response) throws IOException {

		response.setContentType("application/vnd.ms-excel");
		/*response.setHeader("Content-disposition", 
	                "inline; filename=sample");*/

		response.setHeader("Content-Disposition","attachment; filename=Idea.xls");
		OutputStream out = response.getOutputStream();

		HSSFWorkbook workbook=excelBuilder.buildExcelDocument();
		workbook.write(out);
		out.flush();
		out.close();



	}
}

