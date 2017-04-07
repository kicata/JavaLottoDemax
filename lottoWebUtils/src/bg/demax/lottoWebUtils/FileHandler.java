package bg.demax.lottoWebUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class FileHandler {

	public static void convertToComaSeparated(String inputPath,
			String outputPath, String inpEncoding, String delimiter)
			throws IOException {

		int membersPerLine = getMembersPerLine(inputPath, inpEncoding, delimiter);

		if (membersPerLine == 0) {
			throw new IOException("Greshka pri 4etene na membersPerLine ");
		} else {
			String encoding = getInputFileEncoding(inputPath);

			try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
					new FileInputStream(inputPath), inpEncoding));
					BufferedWriter bfw = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(
									outputPath), "utf-8"))) {
				String line;
				int lineCounter = 1;

				while ((line = bfr.readLine()) != null) {
					line = replaceTab(line, delimiter, lineCounter,
							membersPerLine);
					bfw.write(line);
					bfw.newLine();
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

	public static void extractPdfTable(String inputPath,String outputfilePath,String outputPdfPath, 
			String encoding, int fontSize, List<Integer> indexes)
			throws UnsupportedEncodingException, FileNotFoundException,
			IOException {

		Collections.sort(indexes);
		ArrayList<String> dataRows = getFileContents(inputPath, encoding);
		String header = dataRows.get(0);
		extractDataInSepFile(dataRows, indexes, outputfilePath, header,
				encoding);

		genPdfTableFromData(outputfilePath, outputPdfPath, encoding, fontSize);

	}

	public static void uniqueValsPerColumnReport(String inputPath,
			String outputStatisticPath, String separator, int startColInd,
			int substFromEndColCount) {

		Map<Integer, List<String>> colData = getFirstRowColumnData(inputPath,
				separator, startColInd, substFromEndColCount);
		Map.Entry<Integer, List<String>> entry = colData.entrySet().iterator().next();
		int colCount = entry.getKey();
		List<String> colNames = entry.getValue();

		// create list of maps for needed columns
		List<Map<String, Integer>> statistic = new ArrayList<Map<String, Integer>>();

		for (int i = 0; i < colCount; i++) {
			statistic.add(new TreeMap<String, Integer>(
					new CustomSortComparator()));
		}
		// start reading file
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputPath), "utf-8"));
				BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(outputStatisticPath), "utf-8"))) {

			String line;
			int readedLineCounter = 1;

			bfw.write("Статистика:");
			bfw.newLine();

			while ((line = bfr.readLine()) != null) {
				if (readedLineCounter == 1) {
					readedLineCounter++;
					continue;

				} else {

					String[] splLine = line.split(separator);
					int length = splLine.length
							- (startColInd + substFromEndColCount);

					for (int i = startColInd, j = 0; j < length; i++, j++) {
						String key = splLine[i];

						if (statistic.get(j).containsKey(key)) {
							int value = statistic.get(j).get(key);
							statistic.get(j).put(key, ++value);
						} else {
							statistic.get(j).put(key, 1);
						}

					}

					readedLineCounter++;
					if (readedLineCounter % 1000 == 0) {
						System.out.println("Readed lines - "
								+ readedLineCounter);
					}
				}

			}

			bfw.write("Брой колони " + statistic.size());
			bfw.newLine();
			System.out.println("Finish reading...");
			System.out.println("Start writing report...");

			for (int i = 0, j = 1; i < statistic.size(); i++, j++) {
				bfw.write("Колона " + colNames.get(i));
				bfw.newLine();

				Map<String, Integer> masterKey = statistic.get(i);

				printMapKeys(bfw, masterKey);
				System.out.println("Written data for column " + j);
				bfw.flush();
			}
			System.out.println("Finish writing");

		} catch (IOException ioe) {
			System.out.println(ioe.toString());
		}

	}

	public static ArrayList<String> getFileContents(String inputPath,
			String encoding) {
		ArrayList<String> fileContend = new ArrayList<>();
		String line = null;

		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputPath), encoding))) {

			while ((line = bfr.readLine()) != null) {
				fileContend.add(line);
			}

		} catch (IOException ex) {
			System.out.println("There is a problem with file read");
			ex.printStackTrace();
		}

		return fileContend;
	}

	public static void checkForSpecValueReport(String inputPath,
			String outputPath, String needle, String separator, String encoding) {

		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputPath), encoding));
				BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(outputPath), encoding))) {

			String line;
			boolean isFound = false;
			int readedLineCounter = 1;
			int writtenLineCounter = 0;

			bfw.write("Проблем:");
			bfw.newLine();

			while ((line = bfr.readLine()) != null) {
				if (readedLineCounter == 1) {
					readedLineCounter++;
					continue;
				}
				isFound = isNeedleInData(line, separator, needle);
				if (isFound) {
					bfw.write(line);
					bfw.newLine();
					writtenLineCounter++;
				}

				readedLineCounter++;

			}
			if (writtenLineCounter > 0) {
				bfw.write("Problems found " + writtenLineCounter);
				bfw.newLine();
				bfw.flush();
				System.out.println("Check txt.");
				System.out.println("Problems found " + writtenLineCounter);
				System.out.println("Readed lines " + readedLineCounter);
			} else {

				System.out.println("NoProblem Found");
				System.out.println("Readed lines " + readedLineCounter);

			}

		} catch (IOException ioe) {
			System.out.println(ioe.toString());
		}

	}

	public static void winStatisticReport(String inputPath,
			String outputReportPath, String outputWinListPath, int ticketWinPosition,
			String separator) {

		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputPath), "utf-8"));
				BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(outputReportPath), "utf-8"));
				BufferedWriter bfw1 = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(outputWinListPath), "utf-8"))) {

			String line;
			int keyWin = 0;
			int totalWinValue = 0;
			int lineCounter = 1;
			Map<Integer, Integer> wins = new TreeMap<>();
			Map<Integer,String> singleWinContainer= new TreeMap<>();

			while ((line = bfr.readLine()) != null) {
				if (lineCounter == 1) {
					lineCounter++;
					continue;
				}

				String[] values = line.split(separator);
				keyWin = Integer.parseInt(values[ticketWinPosition - 1]);

				if (wins.containsKey(keyWin)) {

					wins.put(keyWin, wins.get(keyWin) + 1);

				} else {
					wins.put(keyWin, 1);
					singleWinContainer.put(keyWin, line);

				}

				lineCounter++;
			}
			bfw.write("Печалби:");
			bfw.newLine();
			for (Integer key : wins.keySet()) {
				Integer value = wins.get(key);
				totalWinValue += (value * key);
				String row = key.toString() + " - " + wins.get(key);
				bfw.write(row);
				bfw.newLine();
			}
			bfw.write("Обща стойност на печалбите: " + totalWinValue);
			bfw.newLine();
			
			
			for (Integer key : singleWinContainer.keySet()) {
				String winRow= singleWinContainer.get(key);
				bfw1.write(Integer.toString(key));
				bfw1.newLine();
				bfw1.write(winRow);
				bfw1.newLine();
				
			}
			bfw.flush();
			bfw1.flush();
			System.out.println("Check winStatistic report txt");
			System.out.println("Check winRow report txt");
			System.out.println("Readed lines " + lineCounter);
			System.out.println("Total win token count "+singleWinContainer.keySet().size());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	public static void checkDeepStepRepetition(String inputPath,
			String outputReportPath, String pattern, String separator,
			int strStartPos, int substrFromEnd, int tiraj) {
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputPath), "utf-8"));
				BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(outputReportPath), "utf-8"))) {

			String line;
			int deepCounter = 0;
			int lineCounter = 1;

			String sRow = readSpecificLineFromFile(inputPath, 2);
			String[] splRow = sRow.split(separator);
			String template = getTemplateStr(splRow, strStartPos,
					substrFromEnd, separator);

			while ((line = bfr.readLine()) != null) {
				if (lineCounter == 1) {
					lineCounter++;
					continue;
				}

				String[] values = line.split(",");
				String templStr = getTemplateStr(values, strStartPos,
						substrFromEnd, separator);

				if (template.toLowerCase().equals(templStr.toLowerCase())) {
					deepCounter += 1;
				}

				lineCounter++;

			}

			bfw.write("Брой талони в дълбочина: " + (tiraj / deepCounter));
			bfw.newLine();
			bfw.write("Брой срещания на темлейт: " + deepCounter);
			bfw.newLine();
			bfw.flush();
			System.out.println("Check txt");
			System.out.println("Readed lines " + lineCounter);
		} catch (IOException ioe) {
			System.out.println(ioe.toString());
		}

	}

	private static String readSpecificLineFromFile(String inputPath, int lineNum) {
		String line = "";
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputPath), "utf-8"))) {

			for (int i = 0; i < lineNum; i++) {
				line = bfr.readLine();
			}

		} catch (IOException ioe) {
			System.out.println(ioe.toString());
		}
		return line;
	}

	private static String getTemplateStr(String[] splRow, int startIndex,
			int substrFromEnd, String separator) {
		StringBuilder sb = new StringBuilder();
		int len = splRow.length - substrFromEnd;
		for (int i = startIndex; i < len; i++) {

			if (i != len - 1) {
				sb.append(splRow[i]);
				sb.append(separator);
			} else {
				sb.append(splRow[i]);
			}

		}

		return sb.toString();
	}

	private static boolean isNeedleInData(String line, String separator,
			String needle) {
		boolean isFound = false;
		String[] splStr = line.split(separator);
		for (int i = 0; i < splStr.length; i++) {
			if (splStr[i].equals(needle)) {
				isFound = true;
				break;
			}
		}
		return isFound;
	}

	private static void printMapKeys(BufferedWriter bfw,
			Map<String, Integer> map) throws IOException {
		/*
		 * ArrayList<String> keys= new ArrayList<>(); keys.addAll(map.keySet());
		 * Collections.sort(keys);
		 */

		StringBuilder sb = new StringBuilder();
		bfw.write("Брой уникални стойности в колона " + map.keySet().size());
		bfw.newLine();

		for (String key : map.keySet()) {
			sb.append(key);
			sb.append(",");
		}
		/*
		 * String str= sb.toString(); toWrite = str.substring(0,
		 * str.length()-2);
		 */
		String result = sb.toString();

		bfw.write(result);
		bfw.newLine();
		bfw.newLine();

	}

	private static Map<Integer, List<String>> getFirstRowColumnData(
			String inputPath, String separator, int startColInd,
			int substrFromEndColCount) {
		int count = 0;

		Map<Integer, List<String>> result = new HashMap<>();

		List<String> columns = new ArrayList<>();
		String line;

		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputPath), "utf-8"))) {
			line = bfr.readLine();
			String[] splRow = line.split(separator);
			count = splRow.length - (startColInd + substrFromEndColCount);

			for (int i = startColInd; i < splRow.length - substrFromEndColCount; i++) {
				columns.add(splRow[i]);
			}

			result.put(count, columns);

		} catch (Exception e) {
			System.out.println("Error reading first line");

		}

		return result;
	}

	private static void printMapByRow(BufferedWriter bfw,
			Map<String, Integer> map) throws IOException {

		for (String key : map.keySet()) {
			int value = map.get(key);
			bfw.write(key + " - " + value);
			bfw.newLine();

		}

		bfw.newLine();

	}

	private static void extractDataInSepFile(ArrayList<String> rows,
			List<Integer> indexes, String outputfilePath, String header,
			String encoding) throws UnsupportedEncodingException,
			FileNotFoundException, IOException {

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputfilePath), encoding))) {

			writer.write(header);
			writer.write(System.getProperty("line.separator"));

			for (int i = 0; i < indexes.size(); i++) {
				String curnLine = rows.get(indexes.get(i));
				writer.write(curnLine);
				writer.write(System.getProperty("line.separator"));

			}
			writer.flush();
			System.out.println("check outputCsv file");
		}

	}

	private static void genPdfTableFromData(String filePath,
			String pdfFilePath, String encoding, int fontSize) {

		ArrayList<String> fileContent = getFileContents(filePath, encoding);

		Document document = new Document();
		Rectangle rect = new Rectangle(PageSize.A3.rotate());
		document.setPageSize(rect);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
			document.open();
			String font = "/usr/share/fonts/truetype/ubuntu-font-family/Ubuntu-L.ttf";
			BaseFont ubuntuBaseCyrilicFont = BaseFont.createFont(font,
					"Cp1251", false);
			Font ubuntuNormal = new Font(ubuntuBaseCyrilicFont, fontSize,
					Font.NORMAL);
			Font ubuntuBold = new Font(ubuntuBaseCyrilicFont, fontSize,
					Font.BOLD);
			ubuntuBold.setColor(0, 0, 0);

			PdfPTable mainTable = new PdfPTable(1);
			mainTable.setWidthPercentage(98);
			mainTable.setSplitRows(true);
			mainTable.getDefaultCell().setBorder(0);
			// manage columnWidth
			int columnCount = fileContent.get(0).split(",").length;
			float columnWidth = 415 / columnCount;
			float[] colWidth = new float[columnCount];

			for (int colInd = 0; colInd < columnCount; colInd++) {
				colWidth[colInd] = columnWidth;
			}

			PdfPTable nestedTable = new PdfPTable(colWidth);
			for (int i = 0; i < fileContent.size(); i++) {
				String[] strArr = fileContent.get(i).split(",");
				if (i == 0) {
					for (int j = 0; j < strArr.length; j++) {
						PdfPCell headerCell = PrintUtils.getCenterAlignCell(
								strArr[j], ubuntuBold);
						nestedTable.addCell(headerCell);
					}
				} else {
					for (int j = 0; j < strArr.length; j++) {
						PdfPCell normalCell = PrintUtils.getCenterAlignCell(
								strArr[j], ubuntuNormal);
						nestedTable.addCell(normalCell);
					}
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

	private static ArrayList<Integer> parseUserInputToInt(String input) {
		ArrayList<Integer> intVals = new ArrayList<Integer>();
		String[] strVals = input.split(",");
		for (int i = 0; i < strVals.length; i++) {
			intVals.add(Integer.parseInt(strVals[i]));
		}
		return intVals;
	}

	private static String replaceTab(String line, String delimiter,
			int lineCounter, int membersPerLine) throws IOException {
		StringBuilder sb = new StringBuilder();
		String addOn = ",";
		// String emptyChar = "q";
		final int MAXSYMBCount = 15;
		String[] strArr = line.split(delimiter);
		int length = strArr.length;

		if (length != membersPerLine) {
			throw new IllegalArgumentException("Nevaliden broi simvoli na red "
					+ lineCounter);
		}

		for (int i = 0; i < length; i++) {
			if (strArr[i].length() > MAXSYMBCount && lineCounter != 1) {
				throw new IOException("Greshka pri 4etene na red "
						+ lineCounter);
			}

			if (i != length - 1) {
				// krystoslovica scenario;
				/*
				 * if (strArr[i].equals(" ")){ sb.append(emptyChar);
				 * sb.append(addOn);
				 * 
				 * }else{ sb.append(strArr[i]); sb.append(addOn);
				 * 
				 * }
				 */

				sb.append(strArr[i]);
				sb.append(addOn);

			} else {
				sb.append(strArr[i]);
			}

		}

		return sb.toString();
	}

	private static int getMembersPerLine(String inputPath, String encoding, String delimiter) throws FileNotFoundException {
		int membersPerLine = 0;
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputPath), encoding))) {
			String line = bfr.readLine();
			String[] strArr = line.split(delimiter);
			membersPerLine = strArr.length;

		} catch (Exception e) {
			
			throw new FileNotFoundException("Problem IO in finding members per line method");
			
		}
		return membersPerLine;
	}
	
	private static String getHeaderRow(String inputPath, String encoding){
		String header="";
		
		try ( BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), encoding))) {
			header = bfr.readLine();
			
			
		} catch (Exception e) {
			e.printStackTrace();;
		}
		
		return header;
		
	}

	private static String getInputFileEncoding(String inputPath) {

		String encoding = "";

		try (FileInputStream fis = new FileInputStream(inputPath);
				InputStreamReader isr = new InputStreamReader(fis)) {

			encoding = isr.getEncoding();
			System.out.print("Character Encoding: " + encoding);

		} catch (Exception e) {
			
			System.out.print("The stream is already closed");
		}

		return encoding;
	}

}
