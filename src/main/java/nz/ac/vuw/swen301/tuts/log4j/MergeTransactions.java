package nz.ac.vuw.swen301.tuts.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Date;

/**
 * The purpose of this class is to read and merge financial transactions, and print a summary:
 * - total amount
 * - highest/lowest amount
 * - number of transactions
 *
 * This version uses Log4J for logging instead of System.out and System.err.
 *
 */
public class MergeTransactions {

	private static List<DateFormat> SUPPORTED_DATE_FORMATS = Arrays.asList(
			new SimpleDateFormat("dd-MM-yyyy"),
			new SimpleDateFormat("dd/MM/yyyy")
	);
	private static NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.getDefault());


	private static final Logger fileLogger = Logger.getLogger("FILE");
	private static final Logger transactionLogger = Logger.getLogger("TRANSACTIONS");

	public static void main(String[] args) {

		PropertyConfigurator.configure("C:\\Users\\limin\\Tutorial6_Log4J\\log4j.properties");

		List<Purchase> transactions = new ArrayList<Purchase>();


		readFile("transactions1.csv", transactions);
		readFile("transactions2.csv", transactions);
		readFile("transactions3.csv", transactions);
		readFile("transactions4.csv", transactions);

		// 记录交易数据
		transactionLogger.info(transactions.size() + " transactions imported");
		transactionLogger.info("Total value: " + CURRENCY_FORMAT.format(computeTotalValue(transactions)));
		transactionLogger.info("Max value: " + CURRENCY_FORMAT.format(computeMaxValue(transactions)));
	}

	private static void readFile(String fileName, List<Purchase> transactions) {
		File file = new File(fileName);
		if (!file.exists()) {
			fileLogger.warn("File " + fileName + " does not exist.");
			return;
		}
		fileLogger.info("Import data from " + fileName);
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				try {
					String[] values = line.split(",");
					Purchase purchase = new Purchase(
							values[0],
							Double.parseDouble(values[1]),
							parseDate(values[2])
					);
					transactions.add(purchase);


					transactionLogger.debug(String.format("Imported transaction: Retailer=%s, Amount=%.2f, Date=%s",
							purchase.getRetailer(), purchase.getAmount(), purchase.getDate().toString()));
				} catch (NumberFormatException | ParseException e) {
					fileLogger.error("Error parsing data from " + fileName + ": " + line, e);
				}
			}
		} catch (IOException e) {
			fileLogger.error("Problem reading file " + fileName, e);
		}
	}

	private static Date parseDate(String dateString) throws ParseException {
		for (DateFormat format : SUPPORTED_DATE_FORMATS) {
			try {
				return format.parse(dateString);
			} catch (ParseException e) {

			}
		}
		throw new ParseException("Unparseable date: " + dateString, 0);
	}

	private static double computeTotalValue(List<Purchase> transactions) {
		double total = 0.0;
		for (Purchase p : transactions) {
			total += p.getAmount();
		}
		return total;
	}

	private static double computeMaxValue(List<Purchase> transactions) {
		double max = 0.0;
		for (Purchase p : transactions) {
			max = Math.max(max, p.getAmount());
		}
		return max;
	}
}
