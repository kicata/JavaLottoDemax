package bg.demax.eurobet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bg.demax.lottoWebUtils.FileHandler;

public class Program {

	public static void main(String[] args) throws IOException {
		final String inputPath = "./resourse/Krystoslovica_11.4_2301500001_2302000000.csv";
		final String userInputFilePath = "./resourse/userInput_Edale.csv";
		final String outputPath = "./resourse/Krystoslovica_11.4_2301500001_2302000000.csv";
		final String outputExtrPath = "./csv/extracted.csv";
		final String outputPdfPath = "./pdf/Krystoslovica_11.4_Table.pdf";
		final String outputStatisticUniquePath = "./report/Krystoslovica_11.4_unique.txt";
		final String outputStatisticWinPath = "./report/Krystoslovica_11.4_wins.txt";
		final String outputStatisticDeepPath = "./report/Krystoslovica_11.4_deep.txt";
		final String outputWinRowReportPath="./report/winRowsReport_Krystoslovica_11.4.txt";
		final String inpEncoding = "utf-8";
		final String delimiter = ",";
		int fontSize = 5;
		int tiraj = 500000;
		String pattern = "q,Ч,О,В,К,А,q,О,q,q,q,q,q,q,q,О,"
				+ "q,q,К,q,Л,q,А,q,Ж,q,Р,А,Л,О,q,Е,q,К,q,И,q,И,q,q,q,q,Й,q,В,З,В,О,Д,q,А,Ф,Е,К,Т,А,q,"
				+ "О,q,О,q,Н,q,q,А,q,Л,q,П,И,Р,А,Т,q,q,q,С,А,q,и,q,q,q,Р,q,Б,q,Л,Н,е,С,q,q,q,А,q,О,С,А,Г,q,q,"
				+ "К,А,С,К,Е,Т,q,М,q,q,q,q,q,q,Т,q,И,q,А,П,О,Л,Я,Н,А";
		
		List<Integer> pageIndexes = Arrays.asList(1,2,3,4,101,102,103,104,201,202,203,204,301,302,303,304,401,402,403,404,
				501,502,503,504,601,602,603,604,701,702,703,704,801,802,803,804,901,902,903,904,1001,1002,1003,1004,
				1101,1102,1103,1104,1201,1202,1203,1204,1301,1302,1303,1304,1401,1402,1403,1404,1501,1502,1503,1504,
				1601,1602,1603,1604,1701,1702,1703,1704,1801,1802,1803,1804,1901,1902,1903,1904  ); 
		List<Integer> pageIndexes1 = Arrays.asList(1,2,3,4,5,6,7,8,9,61,62,63,64,65,66,67,68,69,121,122,123,124,125,126,127,128,129,181,182,183,184,185,186,187,188,189,241,242,243,244,245,246,247,248,249,
				301,302,303,304,305,306,307,308,309); 
		
		try {
			//FileHandler.convertToComaSeparated(inputPath, outputPath,
			//inpEncoding, delimiter);
			//FileHandler.uniqueValsPerColumnReport(inputPath,
			//outputStatisticUniquePath, delimiter, 6, 0);
			//FileHandler.winStatisticReport(inputPath, outputStatisticWinPath, outputWinRowReportPath, 7, delimiter);
			FileHandler.extractPdfTable(outputPath, outputExtrPath, outputPdfPath, inpEncoding, fontSize,pageIndexes1);
			//FileHandler.checkDeepStepRepetition(inputPath,
			 //outputStatisticDeepPath, pattern, delimiter, 27, 0, tiraj);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
   
}
