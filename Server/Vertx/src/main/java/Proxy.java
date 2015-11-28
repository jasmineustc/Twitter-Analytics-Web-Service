
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Proxy extends AbstractVerticle {
	private static final int hostnum=2;
	private static String[] DNS = new String[hostnum];
    private int count = 0;

  // Convenience method so you can run it in your IDE

  @Override
  public void start() throws Exception {
	  
    HttpClient client = vertx.createHttpClient(new HttpClientOptions());
    
    System.out.println("*********** start **************");
    DNS[0] = "ec2-54-172-96-74.compute-1.amazonaws.com";
    DNS[1] = "ec2-54-164-78-143.compute-1.amazonaws.com";
    
    vertx.createHttpServer().requestHandler(req -> {
	      System.out.println("Proxying request: " + req.uri());
	      HttpClientRequest c_req = client.request(req.method(), 80, DNS[count], req.uri(), c_res -> {
	        System.out.println("Proxying response: " + c_res.statusCode());
	        req.response().setChunked(true);
	        req.response().setStatusCode(c_res.statusCode());
	        req.response().headers().setAll(c_res.headers());
	        c_res.handler(data -> {
	          System.out.println("Proxying response body: " + data.toString("ISO-8859-1"));
	          req.response().write(data);
	        });
	        c_res.endHandler((v) -> req.response().end());
	      });
	      c_req.setChunked(true);
	      c_req.headers().setAll(req.headers());
	      req.handler(data -> {
	        System.out.println("Proxying request body " + data.toString("ISO-8859-1"));
	        c_req.write(data);
	      });
	      req.endHandler((v) -> c_req.end());
	      count += 1;
	      count=count%hostnum;
    }).listen(8080);
  }
}
