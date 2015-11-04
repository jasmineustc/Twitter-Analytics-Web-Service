
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
	
	public String executePositive(String startTime, String endTime, String uid, String n) {
		try {
			Scan scan = new Scan();
			scan.addColumn(Bytes.toBytes("data"), Bytes.toBytes("timestamp"));
			scan.addColumn(Bytes.toBytes("data"), Bytes.toBytes("userid"));
			scan.addColumn(Bytes.toBytes("data"), Bytes.toBytes("bannedtext"));
			scan.addColumn(Bytes.toBytes("data"), Bytes.toBytes("twitterid"));
			
			// start time filter
			SingleColumnValueFilter startTimeFilter = new SingleColumnValueFilter(Bytes.toBytes("data"),
			        Bytes.toBytes("timestamp"), CompareOp.GREATER_OR_EQUAL, new ByteArrayComparable(Bytes.toBytes(startTime)));
			filterList.addFilter(filter);
			
			// start time filter
			SingleColumnValueFilter endTimeFilter = new SingleColumnValueFilter(Bytes.toBytes("data"),
			        Bytes.toBytes("timestamp"), CompareOp.LESS_OR_EQUAL, new ByteArrayComparable(Bytes.toBytes(endTime)));
			filterList.addFilter(filter);
						
			// positive score filter
			SingleColumnValueFilter scoreFilter = new SingleColumnValueFilter(Bytes.toBytes("data"),
			        Bytes.toBytes("score"), CompareOp.GREATER, new BinaryComparator(Bytes.toBytes(0)));
			
			// positive score filter
			SingleColumnValueFilter useridFilter = new SingleColumnValueFilter(Bytes.toBytes("data"),
			        Bytes.toBytes("userid"), CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(uid)));
		
			FilterList filterList = new FilterList(Operator.MUST_PASS_ALL, startTimeFilter, endTimeFilter, scoreFilter, useridFilter);
			scan.setFilter(filterList);
			
			ResultScanner resultScanner = table.getScanner(scan);
			StringBuilder str = new StringBuilder();
            try {
                for (Result r : resultScanner) {
                	System.out.println(r);
                }
            } finally {
                if (resultScanner != null) resultScanner.close();
                if (table != null) table.close();
            }
            
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

    public static void creatTable(String tableName, String[] family) throws Exception {

        HTableDescriptor desc = new HTableDescriptor(tableName);
        
        for (int i = 0; i < family.length; i++) {
            HColumnDescriptor temp = new HColumnDescriptor(family[i]);
            temp.setInMemory(true)
            desc.addFamily(temp);
        }
        
        admin.createTable(desc);
        System.out.println("create table Success!");
    }

}
