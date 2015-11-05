
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class HBase {
	
	private HTable table;
	private Configuration conf;
	private String hostDNS;
	private String tableName;
	
	public HBase(){
        // conection configuration
		tableName = "test";
//		hostDNS = "ip-172-31-50-232.ec2.internal";
		hostDNS = "ec2-54-172-134-69.compute-1.amazonaws.com";
		conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", hostDNS);
        conf.set("hbase.zookeeper.property.clientPort","2181");
        conf.set("hbase.master", hostDNS+":60000");
        
        // try to get the table
        while (true) {
       	 try {
      		 	HBaseAdmin admin = new HBaseAdmin(conf);
      		 	admin.checkHBaseAvailable(conf);
      		    System.out.println("Build HBaseAdmin");
       		 	table = new HTable(conf, Bytes.toBytes(tableName));
	            
	            System.out.println("Connected to HBase: " + table.getTableName());
	            break;
           } catch (MasterNotRunningException e) {
	            e.printStackTrace();
	            continue;
           } catch (ZooKeeperConnectionException e) {
	            e.printStackTrace();
	            continue;
           } catch (IOException e) {
	            e.printStackTrace();
	            continue;
           }
        }  
	}
	
	public String execute(String uid) {
		try {
			StringBuilder sb = new StringBuilder();
			Result result;
			result = table.get(new Get(Bytes.toBytes(uid)));

			if (result.isEmpty()){
				return "";
			}
			for (KeyValue r : result.list()) {
				sb.append(Bytes.toString(r.getValue()));
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
