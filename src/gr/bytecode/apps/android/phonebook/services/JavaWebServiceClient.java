package gr.bytecode.apps.android.phonebook.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.http.util.ByteArrayBuffer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementation of the @WebServiceClient interface
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class JavaWebServiceClient implements WebServiceClient {

	/**
	 * The end-point URL
	 */
	private String endpoint = "http://phonebook.bytecode.gr";

	/**
	 * Path to the data
	 */
	private String dataPath = "/phonebook.php";

	/**
	 * Path to the data
	 */
	private String dataCheckPath = "/checkdata.php";

	/**
	 * Default constructor
	 */
	public JavaWebServiceClient() {

		super();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gr.bytecode.apps.android.phonebook.services.IWebServiceClient#getSizeInBytes
	 * ()
	 */
	@Override
	public int getSizeInBytes() {

		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gr.bytecode.apps.android.phonebook.services.IWebServiceClient#
	 * isNewDataAvailable()
	 */
	@Override
	public boolean isNewDataAvailable(String currentVersion) {

		try {

			// prepare the url string
			String urlStr = endpoint + dataCheckPath + "?version=" + currentVersion;

			// set the URL
			URL url = new URL(urlStr);

			// download data
			String data = downloadUrlData(url);

			if (data != null) {

				// Default Jackson Json ObjectMapper
				ObjectMapper mapper = new ObjectMapper();

				// create a Jackson json parser
				JsonParser jp = mapper.getJsonFactory().createJsonParser(data);

				// get a JsonNode
				JsonNode rootNode = mapper.readTree(jp);

				// locate the result in the json structure
				int hasNewData = rootNode.path("result").asInt();

				// return true if the result is a positive integer
				return hasNewData > 0;
			}

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gr.bytecode.apps.android.phonebook.services.WebServiceClient#downloadData
	 * ()
	 */
	@Override
	public String downloadData() {

		String data = null;

		try {

			// set the URL
			URL url = new URL(endpoint + dataPath);

			// download data
			data = downloadUrlData(url);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * Fetch
	 * 
	 * @param url
	 * @return
	 */
	private String downloadUrlData(URL url) {

		try {

			// prepare the connection
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

			// we love compression
			urlConn.setRequestProperty("Accept-Encoding", "gzip");

			// initiate the connection
			HttpURLConnection httpConn = (HttpURLConnection) urlConn;
			httpConn.setAllowUserInteraction(false);
			httpConn.connect();

			// initiate the InputStream
			InputStream in = null;

			// download data
			if (httpConn.getContentEncoding() != null) {
				String contentEncoding = httpConn.getContentEncoding().toString();
				if (contentEncoding.contains("gzip")) {
					in = new GZIPInputStream(httpConn.getInputStream());
				}
				// else it is encoded and we do not want to use it...
			} else {
				in = httpConn.getInputStream();
			}

			// read the stream
			return readStream(in);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param in
	 * @throws IOException
	 */
	private String readStream(InputStream in) throws IOException {

		// prepare the input buffer
		BufferedInputStream bis = new BufferedInputStream(in);
		ByteArrayBuffer baf = new ByteArrayBuffer(1000);

		int read = 0;
		int bufSize = 1024;
		byte[] buffer = new byte[bufSize];

		// read the stream
		while (true) {
			read = bis.read(buffer);
			if (read == -1) {
				break;
			}
			baf.append(buffer, 0, read);
		}
		String queryResult = new String(baf.toByteArray());

		// return result
		return queryResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gr.bytecode.apps.android.phonebook.services.WebServiceClient#
	 * setDownloadUpdateCallcack
	 * (gr.bytecode.apps.android.phonebook.services.Callback)
	 */
	@Override
	public void setDownloadUpdateCallcack(Callback<Integer> callback) {

		// TODO Auto-generated method stub

	}

}
