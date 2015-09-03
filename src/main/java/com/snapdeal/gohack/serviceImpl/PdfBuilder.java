package com.snapdeal.gohack.serviceImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.snapdeal.gohack.model.Idea;


@Component
public class PdfBuilder {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static Font catFont = new Font(Font.FontFamily.HELVETICA, 18,
			Font.BOLD);
		
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
			Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);



	public void addMetaData(Document document) {
		document.addTitle("Wth");
		document.addSubject("wth ideas");
		document.addAuthor("wth@snapdeal.com");
		document.addCreator("wth@snapdeal.com");
	}

	public  void addTitlePage(Document document)
			throws DocumentException, MalformedURLException, IOException {
		Paragraph preface = new Paragraph();
		// We add one empty line
		addEmptyLine(preface, 1);
		// Lets write a big header
		preface.add(new Paragraph("What the Hack", catFont));

		// Will create: Report generated by: _name, _date
		preface.add(new Paragraph("Report generated by: " +"wth@snapdeal.com"+ ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				smallBold));

		Image img = Image.getInstance("./src/main/resources/static/images/Twitter_poster.jpg");

		float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
				- document.rightMargin() - 0) / img.getWidth()) * 100;
		img.scalePercent(scaler);
		//img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
		document.add(img);
		document.add(preface);
		// Start a new page
		document.newPage();
	}

	public void addContent(Document document) throws DocumentException {
		java.util.List<Idea> listofIdeas= jdbcTemplate.query("SELECT  t1.*,group_concat(t3.ideaTeamEmailId) as teamMembers FROM "
				+ "user_ideas AS t1 INNER JOIN idea_status AS t2 ON t1.ideaNumber = t2.ideaNumber "
                + "left join idea_team AS t3 ON t1.ideaNumber=t3.ideaNumber group by 1 "+
                " order by submittedOn desc",
				new BeanPropertyRowMapper(Idea.class));
		
		
		for(Idea eachIdea:listofIdeas){
			Paragraph paragraph = new Paragraph();
			PdfPTable table = new PdfPTable(1);
	        table.setWidthPercentage(100);
	        PdfPCell cell = new PdfPCell(new Phrase("Title :"+eachIdea.getObjective(),catFont));
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell);
	        paragraph.add(table);

			addEmptyLine(paragraph, 1);
			
			
			paragraph.add(new Phrase("Submitted By :",catFont));
			addEmptyLine(paragraph, 1);
			paragraph.add(new Phrase(eachIdea.getEmail()));
			addEmptyLine(paragraph, 1);
			
			paragraph.add(new Phrase("Section:",catFont));
			addEmptyLine(paragraph, 1);
			paragraph.add(new Phrase(eachIdea.getSection().toUpperCase()));
			addEmptyLine(paragraph, 1);
			
			paragraph.add(new Phrase("Objective :",catFont));
			addEmptyLine(paragraph, 1);
			paragraph.add(new Phrase(eachIdea.getObjective()));
			addEmptyLine(paragraph, 1);
			
			paragraph.add(new Phrase("Description",catFont));
			addEmptyLine(paragraph, 1);
			paragraph.add(new Phrase(eachIdea.getDescription()));
			addEmptyLine(paragraph, 1);
			
			paragraph.add(new Phrase("Collaborators",catFont));
			addEmptyLine(paragraph, 1);
			paragraph.add(new Phrase(eachIdea.getTeamMembers()));
			addEmptyLine(paragraph, 1);
				
			document.add(paragraph);
		}


	}

	public  void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}


	public Document buildPdf(String fileName) throws DocumentException, MalformedURLException, IOException {
		Document document = new Document();
		PdfWriter writer=PdfWriter.getInstance(document, new FileOutputStream(fileName));
		HeaderFooterPageEvent event = new HeaderFooterPageEvent();
		writer.setPageEvent(event);
		document.open();
		addMetaData(document);
		addTitlePage(document);
		addContent(document);
		document.close();
		return document;

	}

}
