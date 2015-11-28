import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class Server extends AbstractVerticle {
	// jdbc client.
	private JDBCJava jdbc;
	// catch
	private HashMap<String, String> map = new HashMap<String, String>();
	private String teamId = "QiDeLongDongQiang,642224241148\n";
	private BigInteger X = new BigInteger(
			"8271997208960872478735181815578166723519929177896558845922250595511921395049126920528021164569045773");

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar cal = Calendar.getInstance();

	HashMap<String, Integer> KeyStore1 = new HashMap<String, Integer>();

	@Override
	public void start() throws Exception {
		System.out.println("*********** start **************");
		jdbc = new JDBCJava();

		// connection
		HttpServer server = vertx.createHttpServer();
		server.requestHandler(req -> {
			String uri = req.uri();

			// translate askii to UTF-8
				try {
					uri = URLDecoder.decode(uri, "UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}

				// parse key according to different query
				String key = getQueryKey(uri);
				String response = "";
				if (!key.equals("")) {
					response = jdbc.query(key);
					// build result according to key
					response = buildResult(response, key);
				}

				req.response()
						.putHeader("content-type", "text/html; charset=UTF-8")
						.end(response);

			}).listen(8080);
	}

	/***************************************************************************
	 * Parse Key and Build Result
	 **************************************************************************/

	/**
	 * parse the request to generate the key in order to query in database
	 * 
	 * @param input
	 *            request string
	 * @return query key
	 */
	public String getQueryKey(String input) {

		int q1 = input.indexOf("q1");
		int q2 = input.indexOf("q2");
		int q3 = input.indexOf("q3");
		int q4 = input.indexOf("q4");
		int q5 = input.indexOf("q5");
		int q6 = input.indexOf("q6");

		if (q1 != -1) {
			return parseQ1(input);

		} else if (q2 != -1) {
			return parseQ2(input);

		} else if (q3 != -1) {
			return parseQ3(input);
		} else if (q4 != -1) {
			return parseQ4(input);

		} else if (q5 != -1) {
			return parseQ5(input);
		} else if (q6 != -1) {
			return parseQ6(input);
		}
		return "";
	}

	private String parseQ6(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	private String parseQ5(String input) {
		// q5?userid_min=u_id&userid_max=u_id
		//q5,min,max
		int min = input.indexOf("userid_min=");
		int max = input.indexOf("userid_max=");
		if (min != -1 && max != -1) {
			return "q5," + input.substring(min + 11, max - 1) + ","
					+ input.substring(max + 11);
		} else {
			return "";
		}
	}

	private String parseQ4(String input) {
		// q4?hashtag=hashtag&n=number
		int hashtag = input.indexOf("hashtag=");
		int n = input.indexOf("n=");
		if (hashtag != -1 && n != -1) {
			String tag = input.substring(hashtag + 8, n - 1);
			String num = input.substring(n + 2);
			return "q4," + num + "," + tag;
		} else {
			return "";
		}
	}

	private String parseQ2(String input) {
		// q2?userid=1000002559&tweet_time=2014-06-01:17:10:32
		int idIndex = input.indexOf("userid");
		int idOff = 7;
		int timeIndex = input.indexOf("&");
		int timeOff = 12;
		if (idIndex != -1 && timeIndex != -1) {
			String id = input.substring(idIndex + idOff, timeIndex);
			String time = input.substring(timeIndex + timeOff);
			time = time.substring(0, 10) + " " + time.substring(11);
			String dbReq = String.format("%s,%s", id, time);
			return "q2," + dbReq;
		} else {
			return "";
		}
	}

	private String parseQ3(String input) {
		// q3?start_date=yyyy-mm-dd&end_date=yyyy-mm-dd&userid=1234567890&n=7
		int start_date = input.indexOf("start_date");
		int end_date = input.indexOf("end_date");
		int userid = input.indexOf("userid");
		int n = input.indexOf("n=");
		if (start_date != -1 && end_date != -1 && userid != -1 && n != -1) {
			String startdate = input.substring(start_date + 11, end_date - 1);
			String enddate = input.substring(end_date + 9, userid - 1);
			String id = input.substring(userid + 7, n - 1);
			String num = input.substring(n + 2);
			String dbReq = String.format("%s,%s,%s,%s", id, startdate, enddate,
					num);
			return "q3," + dbReq;
		} else {
			return "";
		}
	}

	private String parseQ1(String input) {
		
			dateFormat.setTimeZone(TimeZone.getTimeZone("PRT"));
			String put = dateFormat.format(cal.getTime()) + "\n";

			int index = input.indexOf("&");
			int keyIndex = input.indexOf("key");
			if (index != -1 && keyIndex != -1) {
				String message = input.substring(index + 9);
				int l = message.length();
				int n = (int) Math.sqrt(l);

				String xy = input.substring(keyIndex + 4, index);
				int Z;
				if (KeyStore1.containsKey(xy)) {
					Z = KeyStore1.get(xy);
				} else {
					BigInteger XY = new BigInteger(xy);
					String Y = XY.divide(X).toString();
					Z = Integer.valueOf(Y.substring(Y.length() - 2)) % 25 + 1;
					KeyStore1.put(xy, Z);
				}
				message = getText(message, n);
				message = moveBit(message, Z);
				return "q1," + put + message + "\n";
			} else {
				return "";
			}
		
	}

	/**
	 * 
	 * @param response
	 *            String, query result
	 * @param key
	 *            String, formant ''
	 * @return
	 */
	private String buildResult(String response, String key) {
		if (key.indexOf("q1") != -1) {
			return teamId + key.substring(3);
		} else if (key.indexOf("q2") != -1) {
			return teamId + response.replace("$fuck$", "\n") + ";";
		} else if (key.indexOf("q3") != -1) {
			return teamId + response.replace("$fuck$", "\n");
		} else if (key.indexOf("q4") != -1) {
			return teamId + response.replace("$fuck$", "\n");
		} else if (key.indexOf("q5") != -1) {
			return teamId + response+";";
		} else if (key.indexOf("q6") != -1) {
			// TODO: q6
			return "";
		} else {
			// invalid key
			return "";
		}
	}

	/********************************************************************
	 * Q1 Helper Function
	 ********************************************************************/

	/**
	 * Return the String move key
	 * 
	 * @param s
	 *            - String need to move bit
	 * @param key
	 *            - number of move bit
	 * @return String - return the original String
	 */
	public String moveBit(String s, int key) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			int ascii = (int) s.charAt(i) - key;
			ascii = (ascii < 65) ? ascii + 26 : ascii;
			builder.append((char) ascii);
		}
		return builder.toString();
	}

	/**
	 * Return the caesarify from diagonalize
	 * 
	 * @param text
	 *            - diagonalized text
	 * @param n
	 *            - square size
	 * @return String - return the original string
	 */
	public String getText(String text, int n) {
		StringBuilder builder = new StringBuilder();
		int i, k, begin;
		char[] textChar = text.toCharArray();
		for (int sum = 0; sum <= (n - 1) * 2; sum++) {
			begin = (sum > n - 1) ? sum - n + 1 : 0;
			for (i = begin; i <= sum && i < n; i++) {
				k = i * n + sum - i;
				builder.append(textChar[k]);
			}
		}
		return builder.toString();
	}
}
