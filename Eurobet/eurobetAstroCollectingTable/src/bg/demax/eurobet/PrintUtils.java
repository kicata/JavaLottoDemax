package bg.demax.eurobet;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;


public class PrintUtils {
	private PrintUtils printInstance = null;
	
	private PrintUtils() {
		
	}
	
	public PrintUtils getInstance(){
		this.printInstance = new PrintUtils();
		
		return printInstance;
	}
	//###################Align
	public static PdfPCell getCenterAlignCell(String text,Font font){
		PdfPCell cell = new PdfPCell(new Paragraph(text,font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return cell;
	}
	
	public static PdfPCell getRightAlignCell(String text,Font font){
		PdfPCell cell = new PdfPCell(new Paragraph(text,font));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		return cell;
	} 
	
	public static PdfPCell getLeftAlignCell(String text,Font font){
		PdfPCell cell = new PdfPCell(new Paragraph(text,font));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		return cell;
	}
	
	public static PdfPCell getFullJustAlignCell(String text,Font font){
		PdfPCell cell = new PdfPCell(new Paragraph(text,font));
		cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		return cell;
	} 
	//################Indent
	public static PdfPCell getLeftAlignIndentCell(String text,Font font,float indent){
		PdfPCell cell = new PdfPCell(new Paragraph(text,font));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setIndent(indent);
		return cell;
	}
	public static PdfPCell getEmptyCell(float height){
		
		PdfPCell cell = new PdfPCell(new Paragraph(""));
		cell.setFixedHeight(height);
		cell.setBorder(0);
		return cell;
	}
}
