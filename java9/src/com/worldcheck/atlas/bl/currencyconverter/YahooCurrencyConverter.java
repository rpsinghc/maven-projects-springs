package com.worldcheck.atlas.bl.currencyconverter;

import com.worldcheck.atlas.dao.currencyconverter.CurrencyConverterDAOImpl;
import com.worldcheck.atlas.vo.currencyconverter.CurrencyConverterVO;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class YahooCurrencyConverter {
	CurrencyConverterDAOImpl currencyConverterDAOImpl = null;
	CurrencyConverterVO currencyConverterVO = null;

	public String convert(String currencyFrom, String currencyTo) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://quote.yahoo.com/d/quotes.csv?s=" + currencyFrom + currencyTo + "=X&f=l1d1&e=.csv");
		ResponseHandler responseHandler = new BasicResponseHandler();
		String responseBody = (String) httpclient.execute(httpGet, responseHandler);
		httpclient.getConnectionManager().shutdown();
		return responseBody;
	}

	public void convert(CurrencyPair[] currencyPairs) throws IOException, ParseException {
		List currencyExRates = new ArrayList();
		HttpClient httpclient = new DefaultHttpClient();
		StringBuffer sb = new StringBuffer();
		CurrencyPair[] arr$ = currencyPairs;
		int len$ = currencyPairs.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			CurrencyPair currencyPair = arr$[i$];
			sb.append("s=").append(currencyPair.getFrom()).append(currencyPair.getTo()).append("=X&");
		}

		HttpGet httpGet = new HttpGet("http://quote.yahoo.com/d/quotes.csv?" + sb + "f=l1d1t1&e=.csv");
		ResponseHandler responseHandler = new BasicResponseHandler();
		String responseBody = (String) httpclient.execute(httpGet, responseHandler);
		httpclient.getConnectionManager().shutdown();
		String[] lines = responseBody.split("\n");
		if (lines.length != currencyPairs.length) {
			throw new IllegalStateException("Currency data mismatch");
		} else {
			int i = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy h:m a");
			sdf.setTimeZone(TimeZone.getTimeZone("EST"));
			String[] arr$ = lines;
			int len$ = lines.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				String line = arr$[i$];
				CurrencyPair currencyPair = currencyPairs[i];
				if (!currencyPair.from.equals(currencyPair.to)) {
					String[] values = line.split(",");
					currencyPair.price = Float.parseFloat(values[0]);
					System.out.println("###############" + currencyPair.price);
					currencyExRates.add(currencyPair.price);
				}

				++i;
			}

			this.currencyConverterDAOImpl.addDataForCurrencyConvertor(this.currencyConverterVO);
		}
	}
}