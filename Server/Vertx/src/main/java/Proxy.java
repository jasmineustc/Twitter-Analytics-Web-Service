import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class Proxy extends AbstractVerticle {
	// jdbc client.
	private JDBCJava jdbc;
	// catch
	private String teamId = "QiDeLongDongQiang,642224241148\n";
	private BigInteger X = new BigInteger(
			"8271997208960872478735181815578166723519929177896558845922250595511921395049126920528021164569045773");

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar cal = Calendar.getInstance();
	HashMap<String, Integer> KeyStore1 = new HashMap<String, Integer>();
	private static final int hostnum = 10;
	private static String[] DNS = new String[hostnum];
    private int count = 0;
	
	 // q5 in mem-cache
	private static UserCountList q5list = initializeQ5("/home/ubuntu/q5data/q5merge.csv");

	@Override
	public void start() throws Exception {
		System.out.println("*********** start **************");
		jdbc = new JDBCJava();
		HttpClient client = vertx.createHttpClient(new HttpClientOptions());
		DNS[0] = "ec2-52-91-188-25.compute-1.amazonaws.com";
        DNS[1] = "ec2-52-91-103-204.compute-1.amazonaws.com";
        DNS[2] = "ec2-52-91-234-233.compute-1.amazonaws.com";
        DNS[3] = "ec2-52-23-206-253.compute-1.amazonaws.com";
        DNS[4] = "ec2-54-152-32-146.compute-1.amazonaws.com";
        DNS[5] = "ec2-54-84-225-132.compute-1.amazonaws.com";
        DNS[6] = "ec2-54-165-88-70.compute-1.amazonaws.com";
        DNS[7] = "ec2-54-165-176-21.compute-1.amazonaws.com";
        DNS[8] = "ec2-54-152-14-32.compute-1.amazonaws.com";
        DNS[9] = "ec2-52-91-17-7.compute-1.amazonaws.com";

		// connection
		HttpServer server = vertx.createHttpServer();
		server.requestHandler(req -> {
			String uri = req.uri();
			if (uri.indexOf("q1") == -1 && uri.indexOf("q5") == -1) {
				int dnsNum = getDnsNum(uri,count);
				HttpClientRequest c_req = client.request(
                        req.method(),
                        80,
                        DNS[dnsNum],
                        req.uri(),
                        c_res -> {
                            req.response().setChunked(true);
                            req.response().setStatusCode(
                                    c_res.statusCode());
                            req.response().headers()
                                    .setAll(c_res.headers());
                            c_res.handler(data -> {
                                req.response().write(data);
                            });
                            c_res.endHandler((v) -> req.response()
                                    .end());
                        });
                c_req.setChunked(false);
                c_req.headers().setAll(req.headers());
                req.endHandler((v) -> c_req.end());
                count += 1;
                count = count % hostnum;
			}else{
				try {
					uri = URLDecoder.decode(uri, "UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}

				// parse key according to different query
				String key = getQueryKey(uri);
				String response = "";
				if (!key.equals("")) {
					if (key.startsWith("q5")) {
						response = String.valueOf(q5list.getCount(key));
					} else if(key.indexOf("q1")==-1){
						//response = jdbc.query(key);
					}
					response = buildResult(response, key);
				}
				req.response()
						.putHeader("content-type", "text/html; charset=UTF-8")
						.end(response);
			}

			}).listen(8080);
	}

	/**
	 * Get the dns num for q2-q4
	 * @param uri
	 * @param count
	 * @return
	 */
	private int getDnsNum(String uri,int count){
		int dnsNum = count;
		if(uri.indexOf("q2")!=-1){
			int idIndex = uri.indexOf("userid=");
			int andIndex = uri.indexOf("&");
			String userid = uri.substring(idIndex+7, andIndex);
			dnsNum = userid.hashCode()%10;
		}else if(uri.indexOf("q6")!=-1){
			int idIndex = uri.indexOf("tweetid=");
			int andIndex = uri.indexOf("&tag");
			String tweetid = uri.substring(idIndex+8, andIndex);
			dnsNum = tweetid.hashCode()%10;
		}else{
			dnsNum = count;
		}
		return dnsNum;
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
	
	public static UserCountList initializeQ5(String filename){
		UserCountList list = new UserCountList();
		
		// round 2: read score list
		BufferedReader reader = null;
		long uid;
		int sum;
		int count = 0;
		String line;
		try {
			reader = new BufferedReader(new FileReader(new File(filename)));
			
			while ((line = reader.readLine()) != null) {
				count++;
				if (count % 5000000 == 0) {
				    System.out.print(count / 5000000 + " ");
				}
				String[] seg = line.split("\t");
				if (seg.length != 3)
					continue;
				uid = Long.parseLong(seg[0]);
				sum = Integer.parseInt(seg[1]);
				list.add(uid, sum);
			}
			reader.close();
		} catch (Exception e) {
			System.out.print("Loading q5 file failed.");
		}

		
		System.out.println("\nQ5: " + count + " loaded! (should be 53767998)");
		return list;
	}
}


class UserCountList {
	// maxID 2594997268
	// minID 12
	// -2147483648 ~ 2147483647
	private int[] id = null;
	private int[] count = null;
	private int size = 0;
	private static final int TOTAL = 53767998+1;
	private static final long MINID = 12;
	private static final long MAXID = 2594997268L;
	private final int UID_SHIFT = 1000000000;

	public UserCountList(){
		id = new int[TOTAL];
		count = new int[TOTAL];
		id[0] = 0;
		count[0] = 0;
		size++;
	}
	
	public void add(long uid, int sum){
		int newid = (int)(uid-UID_SHIFT);
		id[size] = newid;
		count[size] = sum;
		size++;
	}
	
	private int binSearchUidLeft(int[] array, int target, int beginPos, int endPos) {
		// [...)
		while (1 < endPos - beginPos) {
			int mid = (beginPos + endPos) / 2;
			if (target < array[mid]) {
				endPos = mid;
			} else {
				beginPos = mid;
			}
		}
		if (target == array[beginPos]) {
			return beginPos - 1;
		} else {
			return beginPos;
		}
					
	}
	
	private int binSearchUid(int[] array, int target, int beginPos, int endPos) {
		// [...)
		while (1 < endPos - beginPos) {
			int mid = (beginPos + endPos) / 2;
			if (target < array[mid]) {
				endPos = mid;
			} else {
				beginPos = mid;
			}
		}
		return beginPos;			
	}
	
	public int getCount(String q5str) {
		String[] seg = q5str.split(",");
		
		long left = Long.parseLong(seg[1]);
		long right = Long.parseLong(seg[2]);
		
		return search(left, right);
	}
	
	private int search(long left, long right) {
		if(left < MINID){
			left = MINID;
		}
		if(right > MAXID){
			right = MAXID;
		}			
		int leftpos = binSearchUidLeft(id, (int)(left - UID_SHIFT), 1, TOTAL);
		int rightpos = binSearchUid(id, (int)(right - UID_SHIFT), 1, TOTAL);
		return count[rightpos] - count[leftpos];
	}
}