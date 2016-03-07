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
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

public class Perso {

	public static void main(String[] args) throws FileNotFoundException {
		
		final String SOURCE ="./resource/Krystslovica_split_test_214_I_0000001_0055600_I_1456493399113.pdf";
		final String TEMPLATE = "./resource/Eurobet Crossword XEIKON 26in x 500 LICE PODLOJKA 50% BLACK 27.01.2016  CLEAN.pdf";
		final String outputPath ="./readyPdf/inspect.txt";
		Rectangle pageSize = getSize(TEMPLATE);
		Document doc = new Document(pageSize);
		PdfImportedPage templPage, lPage, rPage;
		int totalPages=
		
		PrintWriter writer= new PrintWriter(new FileOutputStream(outputPath));
		
		try {
			
			inspect(writer, SOURCE);
		} catch (IOException e) {
			// TODO Auto-generated catch blockpdfIn
			e.printStackTrace();
		}
		double x1 = 12;
		double y1 = 15;
		double x2 = 12;
		double y2 = 15;
		String fileTitle = "Krystoslovica";
		int tiraz = 500000;
		int tPerPage = 18;
		
		
		
	
	
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
