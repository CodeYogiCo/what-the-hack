package com.snapdeal.gohack.serviceImpl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooterPageEvent extends PdfPageEventHelper {


	public void onEndPage(PdfWriter writer, Document document) {
		try{
			Image imgfoot = Image.getInstance("./src/main/resources/static/images/snapdeal.gif"); 

			imgfoot.scaleAbsoluteHeight(20);
			imgfoot.scaleAbsoluteWidth(20);
			imgfoot.scalePercent(10);
			imgfoot.setAbsolutePosition(0, 0); 

			PdfContentByte cbfoot = writer.getDirectContent(); 
			PdfTemplate tpl = cbfoot.createTemplate(100,100); 
			tpl.addImage(imgfoot); 
			cbfoot.addTemplate(tpl,(document.getPageSize().getWidth()-document.leftMargin()-document.rightMargin())/2, 0); 

		}
		catch(Exception e){

		}
	}

}