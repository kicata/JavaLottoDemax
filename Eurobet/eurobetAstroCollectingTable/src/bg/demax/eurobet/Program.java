package bg.demax.eurobet;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import bg.demax.eurobet.PrintUtils;

public class Program {

	public static void main(String[] args) {
		final String PDF_OUPUT_PATH="./pdf/Кръстословица_11.4.pdf";
		final String GAME_NAME="Евробет-КРЪСТОСЛОВИЦА тираж 7";
		String firstTicketNum = "2301500001";
		int step = 60;
		int tiraj = 500000;
		int banderolCount = tiraj/step;
		int ostatyk = tiraj%step;
		int bandInBox = 8;
		int colCount = 3;
		List<String> banderolString = generateBanderolStringNoPrefix(firstTicketNum, banderolCount, step, ostatyk);
		System.out.println(banderolString.get(banderolString.size()-1));
		List<String> boxString = generateBoxStr(banderolString, bandInBox);
		System.out.println(boxString.get(boxString.size()-1));
		genPdfFromData(boxString, PDF_OUPUT_PATH, colCount,GAME_NAME);
		
	}
		
	public static void genPdfFromData(List<String> boxStrCont, String pdfFilePath, int colCount, String gameName){

		Document document=new Document();
		Rectangle rect = new Rectangle(PageSize.A4);
		document.setPageSize(rect);
		try {
			int ostatyk=boxStrCont.size() % colCount;
			PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
			document.open();
			String font ="/usr/share/fonts/truetype/ubuntu-font-family/Ubuntu-L.ttf";
			BaseFont ubuntuBaseCyrilicFont = BaseFont.createFont(font, "Cp1251", false);
			Font ubuntuNormal = new Font(ubuntuBaseCyrilicFont,10,Font.NORMAL);
			Font ubuntuBold = new Font(ubuntuBaseCyrilicFont,10,Font.BOLD);
			ubuntuBold.setColor(0,0,0);
			
			PdfPTable mainTable = new PdfPTable(1);
			mainTable.setWidthPercentage(98);
			mainTable.setSplitRows(true);
			mainTable.getDefaultCell().setBorder(0);
			//manage columnWidth
			
			float columnWidth = 205 / colCount;
			float[] colWidth= new float[colCount];
			
			for (int colInd = 0; colInd < colCount; colInd++) {
				colWidth[colInd]= columnWidth;
			}
			
			PdfPTable nestedTable = new PdfPTable(colWidth);
			for (int i = 0; i < boxStrCont.size(); i++) {
				String data = boxStrCont.get(i); 
				PdfPCell normalCell = PrintUtils.getCenterAlignCell(data, ubuntuNormal);
				nestedTable.addCell(normalCell);
			}
	
			//add empty cell to get last row in nested table
			if(ostatyk != 0){
				for (int i = 0; i < colCount-ostatyk; i++) {
					PdfPCell normalCell = PrintUtils.getCenterAlignCell("  ", ubuntuNormal);
					nestedTable.addCell(normalCell);
				}
			}
			
			mainTable.addCell(nestedTable);
			document.add(mainTable);
			document.close();
			System.out.println("check PDF");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static List<String> generateBoxStr(List<String> banderolStr, int bandInBox){
		List<String>boxStrContainer = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < banderolStr.size(); i++) {
			if (i%bandInBox==0 && i!=0) {
				if (sb.length()>0) {
					sb.setLength(sb.length() - 1);
				}
				
				String result = sb.toString();
				boxStrContainer.add(result);
				sb=new StringBuilder();
			}
			sb.append(banderolStr.get(i));
			sb.append("\r\n");
			
			
		}
		String result=sb.toString();
		boxStrContainer.add(result);
		
		return boxStrContainer;
	}
	
	private static List<String> generateBanderolString(String firstTicketNum, int banderolCount,int step, int ostatyk){
		List<String> banderolStrContainer = new ArrayList<>();
		
		String prefix = "0";
		int length = Integer.toString(banderolCount).length();
		int first = Integer.parseInt(firstTicketNum);		
		int second = first+step-1;
		for (int i = 1; i <= banderolCount; i++) {
			StringBuilder sb = new StringBuilder();
			String bandNumStr = addZeroes(i, length);
			sb.append(bandNumStr);
			sb.append("      ");
			sb.append(prefix);
			sb.append(first);
			sb.append(" - ");
			sb.append(prefix);
			sb.append(second);
			String result = sb.toString();
			
			banderolStrContainer.add(result);
			first=second+1;
			second=first+step-1;
			
		}
		if (ostatyk!=0){
			StringBuilder sb=new StringBuilder();
			sb.append(++banderolCount);
			sb.append("      ");
			sb.append(prefix);
			sb.append(first);
			sb.append(" - ");
			sb.append(prefix);
			sb.append((first+ostatyk)-1);
			String result = sb.toString();
			
			banderolStrContainer.add(result);
		}
		
		return banderolStrContainer;
	}
	
	private static List<String> generateBanderolStringNoPrefix(String firstTicketNum, int banderolCount,int step, int ostatyk){
		List<String> banderolStrContainer = new ArrayList<>();
		
		int length = Integer.toString(banderolCount).length();
		Long first = Long.parseLong(firstTicketNum);		
		Long second = first+step-1;
		for (int i = 1; i <= banderolCount; i++) {
			StringBuilder sb = new StringBuilder();
			String bandNumStr = addZeroes(i, length);
			sb.append(bandNumStr);
			sb.append("      ");
			sb.append(first);
			sb.append(" - ");
			sb.append(second);
			String result = sb.toString();
			
			banderolStrContainer.add(result);
			first=second+1;
			second=first+step-1;
			
		}
		if (ostatyk!=0){
			StringBuilder sb=new StringBuilder();
			sb.append(++banderolCount);
			sb.append("      ");
			//sb.append(prefix);
			sb.append(first);
			sb.append(" - ");
			//sb.append(prefix);
			sb.append((first+ostatyk)-1);
			String result = sb.toString();
			
			banderolStrContainer.add(result);
		}
		
		return banderolStrContainer;
	}
	
	private static String addZeroes(int num, int length){
		String tocken="";
		String prefix="0";
		StringBuilder sb = new StringBuilder();
		int curnNumlen = Integer.toString(num).length();
		int zeroCount = length-curnNumlen;
		if (zeroCount>0) {
			for (int i = 0; i < zeroCount; i++) {
				sb.append(prefix);
			}
			
			sb.append(num);
			tocken=sb.toString();
			
		} else {
			tocken=Integer.toString(num);
		}
		
		return tocken;
		
	}
	
	

}
