package bg.demax.lotto.reprint.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

public class ReprintFileBuilder {

	public static void main(String[] args) {
		final String DIRECTORY = "/home/k.asenov/gihubRepo/JavaLottoDemax/crosswordPdfPersonalization/readyPdf/";
		final String OUTPUT = "./reprint/Krystoslovica_Popravki_Z_8.2.pdf";
		final String RESOURCE = "./source/popravki.txt"; 
		final String ENCODING = "UTF-8";

		try {
			new ReprintFileBuilder().reprintFileFromSource(DIRECTORY, OUTPUT, RESOURCE, ENCODING);
			
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void reprintFileFromSource(String directory, String output, String resource, String encoding) throws DocumentException, IOException{
		System.out.println("Start Assemling file ");
		long start = System.nanoTime();
		PdfImportedPage curnPageToAdd;
		List<Integer> pages = readCorrectionFile(resource, encoding);
		Map<Integer,String> files = getFileData(directory);
		Rectangle pageSize = getPageSizeFromSource(directory);
		Document doc =new Document(pageSize);
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(output));
		doc.open();
		PdfContentByte cb = writer.getDirectContent();
		
		for (Integer page : pages) {
			
			String fileNameToRead = findFileName(page, files); 
			PdfReader reader = getCurrentReader(fileNameToRead);
			int pageNumInFile= getRealPageNumber(page);
			curnPageToAdd = writer.getImportedPage(reader, pageNumInFile);
			cb.addTemplate(curnPageToAdd, 0, 0);
			doc.newPage();
			System.out.println("Added Page " + page);
		}
		doc.close();
		long end = System.nanoTime();
		System.out.println("READY Check PDF");
		System.out.println("Assembled for " +(start-end)/1000000000+" sec");
		
	}
	
	private Integer getRealPageNumber(int page){
		int realPageNum;
		if(page > 1000){
			String pageStr = Integer.toBinaryString(page);
			realPageNum = Integer.parseInt(pageStr.substring(pageStr.length()-3));
			if (realPageNum == 0) {
				realPageNum = 1000;
			}
			
		}else{
			realPageNum = page;
		}
		
		return realPageNum;
	}
	
	private PdfReader getCurrentReader(String inputPath) throws IOException{
		RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
		RandomAccessSource src = fac.createBestSource(inputPath);
		PdfReader reader = new PdfReader(new RandomAccessFileOrArray(src), null);
		
		return reader;
	}
	
	private String findFileName(int pageNumber, Map<Integer,String> files){
		String fileName = "";
		for (Integer key: files.keySet()) {
			if (pageNumber <= key) {
				fileName = files.get(key);
				break;
			}
			
		}
			
		return fileName;
	}

	private Rectangle getPageSizeFromSource(String directory){
		Rectangle pageSize = null;
		String fileName;
		
		
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();
		fileName = directory + listOfFiles[0].getName();
		
		try {
			RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
			RandomAccessSource src = fac.createBestSource(fileName);
			PdfReader reader = new PdfReader(new RandomAccessFileOrArray(src), null);
			
			pageSize = reader.getPageSize(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error reading size of the pdf file");
		}
		
		return pageSize;
	}
	
	private List<Integer> readCorrectionFile(String input, String encoding){
		List<Integer> pages = new ArrayList<>();
		
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
				new FileInputStream(input), encoding))) {
			String line;
			int lineCounter = 1;
			
			while ((line = bfr.readLine()) != null) {
				int pageNum = Integer.parseInt(line);
				pages.add(pageNum);
				lineCounter++;
			}
			System.out.println("Readed lines " + lineCounter);
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Error readidng user correction file");
		}
		
		Collections.sort(pages);
		return pages;
	}
	
	private Map<Integer, String> getFileData(String directory){
		Map<Integer, String> fileLocator = new TreeMap<>(); 
		
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  String fileName = listOfFiles[i].getName();
		    	  String extension = fileName.substring(fileName.length()-3);
		    	  
		    	  if (extension.equals("pdf")) {
		    		  String[]numPart =  fileName.substring(0, fileName.indexOf(" ")).split("_");
		    		  int bigValue = Integer.parseInt(numPart[1]);
		    		  String filePath = directory + fileName;
		    		  fileLocator.put(bigValue, filePath);
		    	  }
		    	  
		        System.out.println("Added FileName " + listOfFiles[i].getName());
		        
		      } 
		    }

		return fileLocator;
		
	}

}
