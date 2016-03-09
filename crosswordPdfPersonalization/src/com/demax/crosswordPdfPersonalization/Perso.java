package com.demax.crosswordPdfPersonalization;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;


public class Perso {

	public static void main(String[] args) throws IOException, DocumentException {

		final String SOURCE = "./resource/Krystslovica_split_test_214_I_0000001_0055600_I_1456493399113.pdf";
		final String TEMPLATE = "./resource/Eurobet Crossword XEIKON 26in x 500 LICE PODLOJKA 50% BLACK 27.01.2016  CLEAN.pdf";
		final String DIRECTORY = "./readyPdf/";
		final String FILETITLE = "Krystoslovica_z11.2";
		final String ZAIAVKA = "Zaiavka 11.2";
		final BaseColor BLACK;
		
		
		Rectangle pageSize = getSize(TEMPLATE);
		PdfReader templReader = new PdfReader(TEMPLATE);
		
		BaseFont hebarCondBase = BaseFont.createFont("./fonts/HEBARCO3.TTF", "UTF-8", true);
		Font hebarCondNormal  = new Font(hebarCondBase, 10, Font.NORMAL);

		int totalPages = getNumberOfPages(SOURCE);
		int step = totalPages / 2;
		int pagesPerFile = 1000;
		int[] dataForIteration = getFullPartAndRest(pagesPerFile, step);
		int fileCount = dataForIteration[0];
		int rest = dataForIteration[1];
		int first;

		//for the full fileCount
		for (int i = 0; i < 1; i++) {
			first = (i * pagesPerFile) + 1;
			try {

				assemblePdfFile(totalPages,pagesPerFile, first, step, 
						DIRECTORY, FILETITLE, SOURCE, ZAIAVKA, 
						templReader, pageSize, hebarCondNormal);

			} catch (Exception e) {
				System.out.println("Exception in assembling full part on iteration "+i);
				e.printStackTrace();
			}

		}

		//for the rest
		first = (step - rest) + 1;
		
		try {
			assemblePdfFile(totalPages,rest, first, step, 
					DIRECTORY, FILETITLE, SOURCE, ZAIAVKA, 
					templReader, pageSize, hebarCondNormal);
			System.out.println("READY check directory");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("exception in ");
			e.printStackTrace();
			
		}

	}
	
	private static void assemblePdfFile(int totalPages, int pagesPerFile, int first, int step,
			String directory, String fileTitle, String inputPath, String zaiavka,
			PdfReader templReader, Rectangle pageSize, Font font) throws DocumentException, IOException{
		
	PdfImportedPage templPage, lPage, rPage;
	
	long time = System.currentTimeMillis();
	System.out.println("Start assembling file " + time);
	int lPageIndex = first; 
	int rPageIndex = lPageIndex + step;
	int pCount = first + pagesPerFile;
	boolean flag = false;
	String filename = assembleFileName(pagesPerFile, first, directory, fileTitle);

	Document doc = new Document(pageSize);
	PdfWriter writer = PdfWriter.getInstance(doc,new FileOutputStream(filename));
	writer.setPdfVersion(PdfWriter.PDF_VERSION_1_3);
	
	templPage = writer.getImportedPage(templReader,1);
	
	//memory save reader
	RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
	RandomAccessSource src = fac.createBestSource(inputPath);
	PdfReader reader = new PdfReader(new RandomAccessFileOrArray(src), null);

	doc.open();

	PdfContentByte cbu = writer.getDirectContentUnder();
	PdfContentByte cbo = writer.getDirectContent();

		for(int j = first; j< pCount;j++){
			
			if (j % 100 == 0) {
				reverse(flag);
				System.out.println("Processed page " + j);
			}
		
			cbu.addTemplate(templPage, 0, 0);
			
			lPage = writer.getImportedPage(reader, lPageIndex);
			cbo.addTemplate(lPage, 0, 0);
			
			rPage = writer.getImportedPage(reader, rPageIndex);
			cbo.addTemplate(rPage, pageSize.getWidth() / 2, 0);

			Image bc = createBarcode(cbo, Integer.toString(j), 50, 8);
			Image bc1 = createBarcode(cbo, Integer.toString(j), 50, 8);
			String text = constructTextMessage(j, step, pagesPerFile, pCount, zaiavka);
			
			//left part
			placeBarcode(doc, bc, 90, 5, 140);
			printMessageOnPosition(cbo, font, text+" LQVO", 5, 140, 90);

			//right part
			placeBarcode(doc, bc1, -90, 650, 70);
			printMessageOnPosition(cbo, font, text+" DQSNO", 650, 240, -90);

			//deepCounter Mark
			if (flag == true) {
				printLineOnPosition(5, 462, 5, 472, cbo, 10);
				printLineOnPosition(335, 462, 335, 472, cbo, 10);
			}else{
				printLineOnPosition(5, 452, 5, 462, cbo, 10);
				printLineOnPosition(335, 452, 335, 462, cbo, 10);
				
			}
			
			doc.newPage();
			lPageIndex++;
			rPageIndex++;
		
		}
		
	writer.flush();
	/*writer.close();
	reader.close();*/
	doc.close();

	System.out.println("Assembled file " + filename);
	System.out.println((System.currentTimeMillis()-time)+"ns per million");

	}
	
	private static boolean reverse(boolean value){
		if (value == true) {
			return false;
		}
		return true;
	}
	
    private static void placeBarcode(Document doc, Image codeImage, float rotation, float lX, float lY ) throws DocumentException{
    	codeImage.setRotationDegrees(rotation);
    	codeImage.setAbsolutePosition(Utilities.millimetersToPoints(lX), Utilities.millimetersToPoints(lY));
    	doc.add(codeImage);
    }
    
    
	private static Image createBarcode(PdfContentByte cb, String text, float width, float height) throws BadElementException{
		Barcode128 code128  = new Barcode128();
		code128.setCode(text.trim());
		code128.setCodeType(Barcode128.CODE128);
		code128.setBarHeight(height);
		code128.setFont(null);
        PdfTemplate template = cb.createTemplate(Utilities.millimetersToPoints(width), Utilities.millimetersToPoints(height));
        code128.placeBarcode(template, BaseColor.BLACK, null);
        return Image.getInstance(template);
    }

	private static void printLineOnPosition(float xStart, float yStart, float xEnd, float yEnd, PdfContentByte cb, float Width)
    {
        cb.setCMYKColorStroke(255, 255, 255, 255);//150
        cb.setLineWidth(Width);
        cb.moveTo(Utilities.millimetersToPoints(xStart), Utilities.millimetersToPoints(yStart));
        cb.lineTo(Utilities.millimetersToPoints(xEnd), Utilities.millimetersToPoints(yEnd));
        cb.stroke();
        
    }
	
	private static void printMessageOnPosition(PdfContentByte cb, Font font, String message, float sX, float sY, float rotation){
		
		Phrase text = new Phrase(message,font);
		ColumnText.showTextAligned(cb,Element.ALIGN_LEFT, text, Utilities.millimetersToPoints(sX), Utilities.millimetersToPoints(sY), rotation);
	}
	
	private static String constructTextMessage(int pageNum, int totalPages, int pagesPerFile, int count, String zaiavka){
		StringBuilder sb = new StringBuilder();
		//int pagePerHudred = ((((count - pageNum)- pagesPerFile) - 1)/100);
		int pagePerHudred;
		String value =Integer.toString(pageNum);
		if (value.length()>2) {
			pagePerHudred = Integer.parseInt(value.substring(value.length()-2));
		} else {
			pagePerHudred = pageNum;
		}
		 
		
		sb.append(pageNum);
		sb.append("/");
		sb.append(totalPages);
		sb.append(" v dulbochina ");
		sb.append(pagePerHudred);
		sb.append("/100 ");
		sb.append(zaiavka);
		
		return sb.toString();
		
	}

	private static String assembleFileName(int numPages, int first, String directory, String fileTitle) {
		StringBuilder sb = new StringBuilder();
		sb.append(directory);
		sb.append(first);
		sb.append("_");
		sb.append((first + numPages)-1);
		sb.append(" ");
		sb.append(fileTitle);
		sb.append(".pdf");

		return sb.toString();
	}

	private static int[] getFullPartAndRest(int pagesPerFile, int totalPages) {
		int[] data = new int[2];
		int rest = totalPages % pagesPerFile;
		int full = totalPages / pagesPerFile;
		data[0] = full;
		data[1] = rest;

		return data;
	}

	private static Rectangle getSize(String filename) {
		Rectangle pageSize = null;
		try {
			PdfReader reader = new PdfReader(filename);
			pageSize = reader.getPageSize(1);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("exception in getSize()");
			e.printStackTrace();
		}

		return pageSize;

	}

	private static int getNumberOfPages(String inputPath) {
		int numPages = 0;
		try {
			RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
			RandomAccessSource src = fac.createBestSource(inputPath);
			PdfReader reader = new PdfReader(new RandomAccessFileOrArray(src), null);
			numPages = reader.getNumberOfPages();
			reader.close();
		} catch (Exception e) {
			System.out.println("exception in getNumberOfPages()");
			e.printStackTrace();
			// TODO: handle exception
		}

		return numPages;
	}

	
}
