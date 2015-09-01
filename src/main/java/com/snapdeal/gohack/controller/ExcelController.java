package com.snapdeal.gohack.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.snapdeal.gohack.serviceImpl.ExcelBuilder;
import com.snapdeal.gohack.serviceImpl.PdfBuilder;


@RestController
public class ExcelController {

	@Autowired
	private ExcelBuilder excelBuilder;

	@Autowired
	private PdfBuilder pdfBuilder;


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


	@RequestMapping(value = "/downloadPdf", method = RequestMethod.GET)

	public void downloadpdf(HttpServletResponse response,HttpServletRequest request) throws IOException, DocumentException {
		final String fileName="Idea.pdf";
		final ServletContext servletContext = request.getSession().getServletContext();
	    final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
	    final String temperotyFilePath = tempDirectory.getAbsolutePath();
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition","attachment; filename="+fileName);
	
		pdfBuilder.buildPdf(temperotyFilePath+"\\"+fileName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = convertPDFToByteArrayOutputStream(temperotyFilePath+"\\"+fileName);
        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
	




	}private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {

		InputStream inputStream = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			inputStream = new FileInputStream(fileName);
			byte[] buffer = new byte[1024];
			baos = new ByteArrayOutputStream();

			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return baos;
	}
}

