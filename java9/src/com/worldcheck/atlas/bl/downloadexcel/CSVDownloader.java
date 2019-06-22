package com.worldcheck.atlas.bl.downloadexcel;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public class CSVDownloader {
	private static ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.downloadexcel.CSVDownloader");
	private static final String CSV_FILE_PREFIX = "Worldcheck_";
	private static final String CSV_FILE_SUFFIX = ".csv";
	private static final String UNDERSCORE = "_";
	private static PropertyReaderUtil propertyReader;
	private static final String CONTENT_TYPE = "text/csv; charset=UTF-8";
	private static final String NULL_REPLACEMENT = "";
	private static final String NULL_STRING = "null";
	private static final String COMMA_STRING = ",";

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public static String exportCSV(List<String> lstHeader, List<List<String>> dataList, String csvFileName,
			HttpServletResponse response) throws CMSException {
		logger.debug("in CSVDownloader lstHeader " + lstHeader.size());
		logger.debug("dataList " + dataList.size());
		String fileName = "";

		try {
			String tempCsvFilePath = "";
			tempCsvFilePath = propertyReader.getTempExcelFilePath();
			File dir = new File(tempCsvFilePath);
			if (!dir.exists()) {
				(new File(tempCsvFilePath)).mkdirs();
			}

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss.SSS");
			fileName = tempCsvFilePath + "\\" + "Worldcheck_" + csvFileName + "_" + sdf.format(new Date()) + ".csv";
			File csvFile = new File(fileName);
			if (csvFile.exists()) {
				csvFile.delete();
			}

			response.setContentType("text/csv; charset=UTF-8");
			FileOutputStream outputStream = new FileOutputStream(fileName);
			byte[] bom = new byte[]{-17, -69, -65};
			outputStream.write(bom);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
			Iterator i$ = lstHeader.iterator();

			while (i$.hasNext()) {
				String hdr = (String) i$.next();
				writer.append("\"" + hdr + "\"");
				writer.append(",");
			}

			writer.append('\n');
			String csvValue = "";
			Iterator i$ = dataList.iterator();

			while (i$.hasNext()) {
				List<String> data = (List) i$.next();
				Iterator iterator = data.iterator();

				while (iterator.hasNext()) {
					String dataValue = (String) iterator.next();
					csvValue = "\"" + getNullReplacedValue(dataValue) + "\"";
					writer.append(csvValue);
					writer.append(",");
				}

				writer.append('\n');
			}

			writer.flush();
			writer.close();
			logger.debug("filename in CSV Downloader:-" + fileName);
			return fileName;
		} catch (IOException var17) {
			throw new CMSException(logger, var17);
		} catch (Exception var18) {
			throw new CMSException(logger, var18);
		}
	}

	private static String getNullReplacedValue(String cellValue) {
		return cellValue != null && !cellValue.equalsIgnoreCase("null") ? cellValue.replaceAll("\"", "\"\"") : "";
	}
}