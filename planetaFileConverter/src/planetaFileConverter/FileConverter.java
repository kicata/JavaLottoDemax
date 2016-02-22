package planetaFileConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
public class FileConverter {

	public static void main(String[] args) {
		final String inputPath = "./resource/Ticket_2500000001_2501000000.txt";
		final String outputPath = "./resource/Ticket_2500000001_2501000000Modify.csv";
		final String impEncoding = "utf-8";
		
		
		addingPlSymbol(inputPath, outputPath, impEncoding);

	}
	
	public static void addingPlSymbol(String inputPath, String outputPath, String impEncoding){

		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputPath), impEncoding));
				BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(outputPath), "utf-8"))) {
			String line;
			int lineCounter = 1;
			int index= 0;

			while ((line = bfr.readLine()) != null) {
				if (index == 0) {
					line+=",painer";
				}
				
				if (index == 101) {
						index = 1;
				}
				//adding data 
				if (index >= 1 && index <=22) {
					line+=",1";
					
				}else if (index >= 23 && index <=40) {
					line+=",2";
					
				}else if (index >= 41 && index <= 55) {
					line+=",3";
					
				}else if (index >= 56 && index <= 67) {
					line+=",4";
					
				}else if (index >= 68 && index <= 78) {
					line+=",5";
					
				}else if (index >= 79 && index <= 88) {
					line+=",6";
					
				}else if (index >= 89 && index <= 95) {
					line+=",7";
					
				}else if (index >= 96 && index <= 100) {
					line+=",8";
					
				}
				bfw.write(line);
				bfw.newLine();
				index++;
				lineCounter++;
			}
			bfw.flush();
			System.out.println("Check csv");
			System.out.println("Written lines " + lineCounter);
		} catch (IOException ioe) {
			System.out.println(ioe.toString());
		}

	}

}
