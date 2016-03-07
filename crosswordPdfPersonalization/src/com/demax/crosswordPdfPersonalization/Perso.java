package com.demax.crosswordPdfPersonalization;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

public class Perso {

	public static void main(String[] args) throws FileNotFoundException {
		
		final String SOURCE ="./resource/Krystslovica_split_test_214_I_0000001_0055600_I_1456493399113.pdf";
		final String TEMPLATE = "./resource/Eurobet Crossword XEIKON 26in x 500 LICE PODLOJKA 50% BLACK 27.01.2016  CLEAN.pdf";
		String filename = "./readyPdf/";
		final String outputPath ="./readyPdf/inspect.txt";
		
		Rectangle pageSize = getSize(TEMPLATE);
		
		PdfImportedPage templPage, lPage, rPage;
		
		String fileTitle = "Krystoslovica Z 11.2 ";

		int totalPages= getNumberOfPages(SOURCE);
		int step = totalPages / 2;
		int pagesPerFile = 1000;
		int fileCount = step / pagesPerFile;
		int lPageIndex = 1; 
		int rPageIndex = lPageIndex + step;
		String resultStr="";
		
		try {
			for (int i = 1; i <=fileCount; i++) {
				resultStr= i+" _ "+ i + pagesPerFile;
				filename += resultStr;
				Document doc = new Document(pageSize);
				RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
		 		RandomAccessSource src = fac.createBestSource(SOURCE);
				PdfReader reader = new PdfReader(new RandomAccessFileOrArray(src), null);
				PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filename));
				writer.setPdfVersion(PdfWriter.VERSION_1_4);
				PdfReader templReader = new PdfReader(TEMPLATE);
				templPage = writer.getImportedPage(templReader, 1);
				
				doc.open();
				PdfContentByte cbu = writer.getDirectContentUnder();
				PdfContentByte cbo = writer.getDirectContent();
				long time = System.currentTimeMillis();
				System.out.println("Start "+time);
				
				for (int j = i*pagesPerFile; j <=1000; i++) {
					if(i % 100 == 0){
						System.out.println("Processed page " + i);
					}
					
					cbu.addTemplate(templPage, 0, 0);
					//System.out.println("AddTempl");
					lPage = writer.getImportedPage(reader,lPageIndex);
					cbo.addTemplate(lPage, 0, 0);
					//System.out.println("Add Left");
					rPage = writer.getImportedPage(reader,rPageIndex);
					cbo.addTemplate(rPage, pageSize.getWidth()/2, 0);
					//System.out.println("Add Right");
					System.out.println("added page " + i);
					doc.newPage();
					lPageIndex++;
					rPageIndex++;
					
				}
				writer.flush();
				doc.close();
				System.out.println("Assembled file");
				System.out.println((System.currentTimeMillis() - time) + "ns per million");
			}	
			} catch (Exception e) {
				// TODO: handle exception
			}
				
				
			
			
			/*RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
	 		RandomAccessSource src = fac.createBestSource(SOURCE);
			PdfReader reader = new PdfReader(new RandomAccessFileOrArray(src), null);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(RESULT));
			writer.setPdfVersion(PdfWriter.VERSION_1_4);
			PdfReader templReader = new PdfReader(TEMPLATE);
			templPage = writer.getImportedPage(templReader, 1);
			doc.open();
			PdfContentByte cbu = writer.getDirectContentUnder();
			PdfContentByte cbo = writer.getDirectContent();
			long time = System.currentTimeMillis();
			System.out.println("Start "+time);
			for (int i = 0; i < 1000; i++) {
				if(i % 100 == 0){
					System.out.println("Processed page " + i);
				}
				
				cbu.addTemplate(templPage, 0, 0);
				//System.out.println("AddTempl");
				lPage = writer.getImportedPage(reader,lPageIndex);
				cbo.addTemplate(lPage, 0, 0);
				//System.out.println("Add Left");
				rPage = writer.getImportedPage(reader,rPageIndex);
				cbo.addTemplate(rPage, pageSize.getWidth()/2, 0);
				//System.out.println("Add Right");
				System.out.println("added page " + i);
				doc.newPage();
				lPageIndex++;
				rPageIndex++;
				
			}
			writer.flush();
			doc.close();
			System.out.println((System.currentTimeMillis() - time) + "ns per million");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		*/
		
		
		
	/*	
		PrintWriter printWriter= new PrintWriter(new FileOutputStream(outputPath));
		
		try {
			
			inspect(printWriter, SOURCE);
		} catch (IOException e) {
			// TODO Auto-generated catch blockpdfIn
			e.printStackTrace();
		}
		
		
		*/
		
		
	
	
	}
	
	private static Rectangle getSize(String filename){
		Rectangle pageSize = null;
		try {
			PdfReader reader = new PdfReader(filename);
			pageSize = reader.getPageSize(1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return pageSize;
		
	}
	
	private static int getNumberOfPages(String inputPath){
		int numPages = 0;
		try {
			RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
	 		RandomAccessSource src = fac.createBestSource(inputPath);
			PdfReader reader = new PdfReader(
			 new RandomAccessFileOrArray(src), null);
			numPages=reader.getNumberOfPages();
			reader.close();
		} catch (Exception e) {
			
			// TODO: handle exception
		}
		
		
		return numPages;
	}
	
	 public static void inspect(PrintWriter writer, String filename)
		        throws IOException {
		 
		 		RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
		 		RandomAccessSource src = fac.createBestSource(filename);
				PdfReader reader = new PdfReader(
				 new RandomAccessFileOrArray(src), null);
		       // PdfReader reader = new PdfReader(filename);
				
		        writer.println(filename);
		        writer.print("Number of pages: ");
		        writer.println(reader.getNumberOfPages());
		        Rectangle mediabox = reader.getPageSize(1);
		        writer.print("Size of page 1: [");
		        writer.print(mediabox.getLeft());
		        writer.print(',');
		        writer.print(mediabox.getBottom());
		        writer.print(',');
		        writer.print(mediabox.getRight());
		        writer.print(',');
		        writer.print(mediabox.getTop());
		        writer.println("]");
		        writer.print("Rotation of page 1: ");
		        writer.println(reader.getPageRotation(1));
		        writer.print("Page size with rotation of page 1: ");
		        writer.println(reader.getPageSizeWithRotation(1));
		        writer.print("Is rebuilt? ");
		        writer.println(reader.isRebuilt());
		        writer.print("Is encrypted? ");
		        writer.println(reader.isEncrypted());
		        writer.println();
		        writer.flush();
		        reader.close();
		        System.out.println("READY check result");
		    }
	
	

}
