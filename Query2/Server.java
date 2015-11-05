import io.vertx.core.Vertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.net.JksOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.DeploymentOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.math.BigInteger;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Server extends AbstractVerticle {
		
	private static HBase hbase = new HBase();
	private HashMap<String, String> map = new HashMap<String, String>();
	private String teamId = "QiDeLongDongQiang,642224241148\n";

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(30000));
		DeploymentOptions options = new DeploymentOptions().setInstances(20);
		vertx.deployVerticle("com.mycompany.MyOrderProcessorVerticle", options);		
		
		Server s = new Server();
		try {
			s.start();
		} catch (Exception e) {
			System.out.println("error, exception!");
		}
	}

	@Override
	public void start() throws Exception {
		System.out.println("*********** start **************");

		HttpServer server = vertx.createHttpServer();
		server.requestHandler(
				req -> {
					String request = req.uri();
					String response = getResult(request);					
					req.response().putHeader("content-type", "text/html")
							.end(response);
				}).listen(8080);
	}
	
	public String getResult(String url){
		String key = getQueryKey(url);
		if(map.containsKey(key)){		
			return teamId + map.get(key) + ";";	
		}
		String result = hbase.execute(key);

		// replace all the '$fuck$' back to '\n'
		int left = 0;
		while(left != -1){
			left = result.indexOf("$fuck$");
			if(left != -1){
				result = result.substring(0,left) + "\n" + result.substring(left+6);
			}
		}
		map.put(key, result);
		return teamId + result + ";";
	}

	// parse the request and get the a row key of the HBase
	public String getQueryKey(String input) {			
		int idIndex = input.indexOf("userid");
		int idOff = 7;
		int timeIndex = input.indexOf("&");
		int timeOff = 12;
		if (idIndex != -1 && timeIndex != -1) {
			String id = input.substring(idIndex + idOff, timeIndex);
			String time = input.substring(timeIndex + timeOff);
			
			String dbReq = String.format("%s,%s", id,time);
			
			return dbReq;
			
		}else{
			return "";
		}
	}
}