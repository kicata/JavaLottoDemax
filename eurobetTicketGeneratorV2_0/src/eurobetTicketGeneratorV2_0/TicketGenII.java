package eurobetTicketGeneratorV2_0;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class TicketGenII {

	public static void main(String[] args) {
		final String destPath="./pdf/etiketi_BingoMilioni_8.5.pdf";
		final String destPathPerso="./pdf/labelsPersonalized.pdf";
		double xDist = Utilities.millimetersToPoints(70f);
		double yDist = Utilities.millimetersToPoints(50f);
		double startX = 0;
		double startY = Utilities.millimetersToPoints(23f) + (4*yDist);
		int step = 400;
		long startNum = 3002000001l; 
		int tiraz = 500000;
		int labelCount = tiraz / step;
		float sWidth = Utilities.millimetersToPoints(70f);
		float sHeight  = Utilities.millimetersToPoints(50f);
		Rectangle assembledPageSize = new Rectangle(PageSize.A4); 
		
		String[] lParam = {
					"ЕВРОБЕТ ООД\r\n",
					"Лотария България\r\n",
					"400 талона от разновидност\r\n",
					"BINGO МИЛИОНИ - 2лв.\r\n",
					"от No ",
					"до No ",
					"Заявка Nо 8.5"};
		
		
		try {
			ByteArrayOutputStream baos = personalizePdf(lParam, sWidth, sHeight, 
					labelCount, step, startNum, destPathPerso);
			
			assemblyInPagePrint(baos, assembledPageSize, destPath, 3, 5, startX, startY, xDist, yDist);
			
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public static ByteArrayOutputStream personalizePdf(String[] lParam, float width, float height, 
			int labelCount,int step, long startNum, String destPath ) throws DocumentException, IOException{
		System.out.println("Start personalizing");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		long endNum  = (startNum + step) - 1;
		Rectangle rect = new Rectangle(width,height);
		Document doc = new Document(rect,Utilities.millimetersToPoints(10),Utilities.millimetersToPoints(10),
				Utilities.millimetersToPoints(0),Utilities.millimetersToPoints(10));
		
	
		/*PdfWriter writer = PdfWriter.getInstance(doc,
				new FileOutputStream(destPath));*/
		PdfWriter writer = PdfWriter.getInstance(doc,
				baos);
		BaseFont hebarCondBase = BaseFont.createFont("fonts/HEBARCO3.TTF", "Cp1251",
				true);
		Font hebarCondNormal  = new Font(hebarCondBase, 11, Font.NORMAL);
		Font hebarCondBold  = new Font(hebarCondBase, 11, Font.BOLD);
		doc.open();
		PdfContentByte cb = writer.getDirectContent();
		
		for (int i = 0; i < labelCount; i++) {
			
			ArrayList<Phrase> text= new ArrayList<>();
			text.add(new Phrase(" \r\n",hebarCondNormal));
			text.add(new Phrase(lParam[0],hebarCondNormal));
			text.add(new Phrase(lParam[1], hebarCondBold));
			text.add(new Phrase(lParam[2], hebarCondBold));
			text.add(new Phrase(lParam[3], hebarCondBold));
			Phrase from = new Phrase();
			Chunk f = new Chunk(lParam[4],hebarCondNormal);
			Chunk faddOn = new Chunk(Long.toString(startNum) +"\r\n", hebarCondBold);
			from.add(f);
			from.add(faddOn);
			text.add(from);
			
			Phrase to = new Phrase();
			Chunk t=new Chunk(lParam[5],hebarCondNormal);
			Chunk taddOn=new Chunk(Long.toString(endNum) +"\r\n", hebarCondBold);
			to.add(t);
			to.add(taddOn);
			text.add(to);
			
			//text.add(new Phrase(lParam[4] + Long.toString(startNum) +"\r\n", hebarCondBold));
			//text.add(new Phrase(lParam[5] + Long.toString(endNum) + "\r\n", hebarCondBold));
			text.add(new Phrase(lParam[6], hebarCondNormal));
			
			Paragraph p = new Paragraph(14);
			p.addAll(text);
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			
			//an alternative way without setting document margins.
			
			/*ColumnText ct = new ColumnText(cb);
			ct.setSimpleColumn(0, Utilities.millimetersToPoints(10f), Utilities.millimetersToPoints(70f),
					Utilities.millimetersToPoints(50f));
			ct.addText(p);
			ct.setAlignment(Element.ALIGN_CENTER);
			ct.setLeading(14);
			ct.go();*/

			startNum += step;
			endNum = (startNum + step) - 1;
			doc.newPage();
		}
		doc.close();
		System.out.println("End Personalizing");
		return baos;
	}
	public static void assemblyInPagePrint(ByteArrayOutputStream baos, Rectangle pageSize, String destPath, int xRepeat, int yRepeat, 
		double startX, double startY, double xDist, double yDist ) throws DocumentException, IOException{
		System.out.println("StartAssembling");
		Document doc = new Document();
		doc.setPageSize(pageSize);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(destPath));
		PdfReader reader = new PdfReader(baos.toByteArray());
		PdfImportedPage page;
		int count = 1;
		doc.open();
		PdfContentByte cb = writer.getDirectContent();
		int numOfPages = reader.getNumberOfPages();
		int labelsPerPage = xRepeat*yRepeat;
		int totalPages = (int)Math.ceil(((float)numOfPages / (float)labelsPerPage));
		
		for (int pInd = 1; pInd <= totalPages; pInd++) {
			
			for (int i = 1; i <= yRepeat; i++) {
				
				for (int j = 1; j <= xRepeat; j++) {
					
					page = writer.getImportedPage(reader, count);
					cb.addTemplate(page, startX, startY);
					if (count < numOfPages ) {
						count++;
						startX += xDist;
					}else{
						break;
					}

				}
				if(count >= numOfPages){
					break;
				}
				startY -= yDist;
				startX = Utilities.millimetersToPoints(0f);
			
			}
			
			doc.newPage();
			startY = Utilities.millimetersToPoints(23f) + (4*yDist);

		}

		doc.close();
		System.out.println("check assembled PDF");

	}

	public static void assemblyInA4LabelsII(String[] lParam, String destPath,int labelCount, int xRepeat, int yRepeat, 
			int step, long start,double xDist, double yDist) throws IOException, DocumentException{
		System.out.println("Start assembling");
		long end = (start + step) - 1;
		Document doc = new Document();
		Rectangle rect = new Rectangle(PageSize.A4);
		doc.setPageSize(rect);
		PdfWriter writer = PdfWriter.getInstance(doc,
				new FileOutputStream(destPath));
		BaseFont hebarCondBase = BaseFont.createFont("fonts/HEBARCO3.TTF", "Cp1251",
				true);
		Font hebarCondNormal11  = new Font(hebarCondBase, 11, Font.NORMAL);
		Font hebarCondBold11  = new Font(hebarCondBase, 11, Font.BOLD);
		doc.open();
		PdfContentByte cb = writer.getDirectContent();
		
		float startX = Utilities.millimetersToPoints(0f);
		float startY = (float) (Utilities.millimetersToPoints(73f) + (4*yDist));
		int count = 1;
		
		while (count < labelCount) {
			
			for (int i = 1; i <= yRepeat; i++) {
				
				for (int j = 1; j <= xRepeat; j++) {
					
					ArrayList<Phrase> text= new ArrayList<>();
					text.add(new Phrase(lParam[0],hebarCondNormal11));
					text.add(new Phrase(lParam[1], hebarCondBold11));
					text.add(new Phrase(lParam[2], hebarCondBold11));
					text.add(new Phrase(lParam[3], hebarCondBold11));
					text.add(new Phrase(lParam[4] + Long.toString(start) +"\r\n", hebarCondNormal11));
					text.add(new Phrase(lParam[5] + Long.toString(end) + "\r\n", hebarCondNormal11));
					text.add(new Phrase(lParam[6], hebarCondNormal11));
					
					Paragraph allTxt = new Paragraph(10);
					
					allTxt.addAll(text);
					ColumnText ct = new ColumnText(cb);
					ct.setSimpleColumn(startX, startY, Utilities.millimetersToPoints(70f),
							Utilities.millimetersToPoints(30f));
					ct.addText(allTxt);
					ct.setAlignment(Element.ALIGN_CENTER);
					ct.go();
				
					if(count < labelCount){
						count++;
					}else{
						break;
					}
					
					start += step;
					end = (start + step) - 1;
					startX += xDist;
					
				}
				
				if(count >= labelCount){
					break;
				}else{
					startY -= yDist;
					startX = Utilities.millimetersToPoints(0f);
				}

			}
			
			if (count < labelCount) {
				doc.newPage();
				
				startY = (float) (Utilities.millimetersToPoints(73f) + (4*yDist));
			} else {
				break;
			}
		}
		
		doc.close();
		System.out.println("check assembled PDF");

	}

}
