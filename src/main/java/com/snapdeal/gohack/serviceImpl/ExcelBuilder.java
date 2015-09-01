package com.snapdeal.gohack.serviceImpl;


import java.util.List;

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

import com.snapdeal.gohack.model.Idea;

@Component
public class ExcelBuilder {
 
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  
    public HSSFWorkbook buildExcelDocument(){
    	
    	List<Idea> listofIdeas= jdbcTemplate.query("SELECT  t1.*,group_concat(t3.ideaTeamEmailId) as teamMembers FROM "
				+ "user_ideas AS t1 INNER JOIN idea_status AS t2 ON t1.ideaNumber = t2.ideaNumber "
                + "left join idea_team AS t3 ON t1.ideaNumber=t3.ideaNumber group by 1 "+
                " order by submittedOn desc",
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
               
        header.createCell(4).setCellValue("Collaborators");
        header.getCell(4).setCellStyle(style);
        
        
        header.createCell(5).setCellValue("Description");
        header.getCell(5).setCellStyle(style);
      
         
        // create data rows
        int rowCount = 1;
         
        for (Idea idea : listofIdeas) {
            HSSFRow aRow = sheet.createRow(rowCount++);
            aRow.createCell(0).setCellValue(idea.getIdeaOverview());
            aRow.createCell(1).setCellValue(idea.getEmail());
            aRow.createCell(2).setCellValue(idea.getObjective());
            aRow.createCell(3).setCellValue(idea.getSection());
            aRow.createCell(4).setCellValue(idea.getTeamMembers());
            aRow.createCell(5).setCellValue(idea.getDescription());
                
            
        }
        return workbook;
    }
}
	