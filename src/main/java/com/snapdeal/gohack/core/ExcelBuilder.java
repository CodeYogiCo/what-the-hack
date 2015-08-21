package com.snapdeal.gohack.core;


import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExcelBuilder {
 
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  
    protected HSSFWorkbook buildExcelDocument(){
    	
    	List<Idea> listofIdeas= jdbcTemplate.query("SELECT *  FROM user_ideas AS t1 INNER JOIN idea_status "
				+ "AS t2 ON t1.ideaNumber = t2.ideaNumber ",
				new BeanPropertyRowMapper(Idea.class));
		
        HSSFWorkbook workbook= new HSSFWorkbook();
        // create a new Excel sheet
        HSSFSheet sheet = workbook.createSheet("Ideas");
        sheet.setDefaultColumnWidth(30);
         
        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
       
         
        // create header row
        HSSFRow header = sheet.createRow(0);
         
        header.createCell(0).setCellValue("Topic");
        header.getCell(0).setCellStyle(style);
         
        header.createCell(1).setCellValue("Submitted By");
        header.getCell(1).setCellStyle(style);
         
        header.createCell(2).setCellValue("Objective");
        header.getCell(2).setCellStyle(style);
        
        
         
        header.createCell(3).setCellValue("Section");
        header.getCell(3).setCellStyle(style);
         
        header.createCell(4).setCellValue("Votes");
        header.getCell(4).setCellStyle(style);
        
        header.createCell(5).setCellValue("Status");
        header.getCell(5).setCellStyle(style);
        
        
        header.createCell(6).setCellValue("Description");
        header.getCell(6).setCellStyle(style);
      
         
        // create data rows
        int rowCount = 1;
         
        for (Idea idea : listofIdeas) {
            HSSFRow aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(idea.getIdeaOverview());
            aRow.createCell(1).setCellValue(idea.getEmail());
            aRow.createCell(2).setCellValue(idea.getObjective());
            aRow.createCell(3).setCellValue(idea.getSection());
            aRow.createCell(4).setCellValue(idea.getIdeaUpVote()-idea.getIdeaDownVote());
            aRow.createCell(5).setCellValue(idea.getIdeaStatus());
            aRow.createCell(6).setCellValue(idea.getDescription());
                
            
        }
        return workbook;
    }
}
	