package splitPersoFileConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class SplFileConverter {

	public static void main(String[] args) {

		final String inputPath = "./resource/Ticket_2300000001_2300500000.csv";
		final String outputPath = "./resource/Ticket_2300000001_2300500000splited.csv";
		final String encoding = "utf-8";
		final int tiraz = 500000;
		final int splBy = 9;

		List<String> dataRows = readFileData(inputPath, encoding);
		splitConvert(dataRows, outputPath, encoding, tiraz, splBy);

	}

	public static void splitConvert(List<String> dataRows, String outputPath, String encoding, int tiraz, int splBy) {
		int len = dataRows.size();
		int half = len / 2;
		int lineCounter = 0;

		String header = dataRows.get(0);
		dataRows.remove(0);
		List<String> firstHalf = new ArrayList<>();
		firstHalf.addAll(dataRows.subList(0, half));

		/*System.out.println(firstHalf.get(0));
		System.out.println(firstHalf.get(firstHalf.size() - 1));*/

		List<String> secondHalf = new ArrayList<>();
		secondHalf.addAll(dataRows.subList(half, len - 1));

		/*System.out.println(secondHalf.get(0));
		System.out.println(secondHalf.get(secondHalf.size() - 1));*/

		try (BufferedWriter bfw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(outputPath), encoding))) {
			
			boolean isRemainderTime = false;
			int from = 0;
			int to = 0;

			bfw.write(header);
			bfw.newLine();

			for (int i = 0; i < half; i++) {
				from = i * splBy;
				to = from + splBy;

				for (int j = from; j < to; j++) {

					bfw.write(firstHalf.get(j));
					bfw.newLine();
					lineCounter++;

				}

				for (int j = from; j < to; j++) {

					bfw.write(secondHalf.get(j));
					bfw.newLine();
					lineCounter++;

				}

				if (lineCounter % 10000 == 0) {
					System.out.println("Written lines " + lineCounter);
				}

				if ((to + splBy) > half) {
					isRemainderTime = true;
					break;
				}
			}

			if (isRemainderTime) {
				lineCounter += writeListToFileByIndex(firstHalf, bfw, to);
				lineCounter += writeListToFileByIndex(secondHalf, bfw, to);

			}

			bfw.flush();
			System.out.println("Written lines " + lineCounter);
			System.out.println("Check csv");

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private static int writeListToFile(List<String> data, BufferedWriter bfw) throws IOException {
		int counter = 0;
		for (String row : data) {
			bfw.write(row);
			bfw.newLine();
			counter++;
		}

		return counter;
	}

	private static int writeListToFileByIndex(List<String> data, BufferedWriter bfw, int index) throws IOException {
		int counter = 0;
		for (int i = index; i < data.size(); i++) {
			bfw.write(data.get(i));
			bfw.newLine();
			counter++;
		}

		return counter;
	}

	private static List<String> readFileData(String inputPath, String encoding) {
		List<String> result = new ArrayList<>();

		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), encoding))) {
			String line;
			int lineCounter = 0;

			while ((line = bfr.readLine()) != null) {
				result.add(line);
				lineCounter++;
			}

			System.out.println("Readed lines " + lineCounter);
		} catch (IOException ioe) {
			System.out.println(ioe.toString());
		}

		return result;
	}

}
