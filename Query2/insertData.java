import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;


public class insertData {
	
	private static HBaseAdmin admin;
	private static Configuration conf;
	private static String hostDNS;
	private static String tableName;

	public static void main(String[] args) throws Exception { 
//		hostDNS = "ec2-52-91-60-86.compute-1.amazonaws.com";
		hostDNS = "localhost";
		conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", hostDNS);
        conf.set("hbase.zookeeper.property.clientPort","2181");
        conf.set("hbase.master", hostDNS+":60000");
        
	 	admin = new HBaseAdmin(conf);
	 	admin.checkHBaseAvailable(conf);
	 	System.out.println("Admin build!");
	 	
	 	tableName = "test";
	 	String inputPath = args[0];
	 	
	 	InsertData(inputPath,tableName);
	}
	
	public static void InsertData(String inputPath, String tableName) throws Exception {

        String[] family = { "data" };  // f for family
        String[] column = { "post" };  // c for column

        if (!admin.tableExists(tableName))
            creatTable(tableName, family);
        else
            System.out.println(tableName + " exist!");
        
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        table.setAutoFlush(false);
        table.setWriteBufferSize(64 * 1024 * 1024); // 64M buffer

        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(inputPath), "UTF-8"));
        
        ArrayList<Put> putList = new ArrayList<Put>();
      
        int count = 0;
        String line = null;
        while ((line = br.readLine()) != null) {
            
            if (line.length() == 0) {
            	System.out.println("String length 0");
                continue;
            }
  
            String[] seg = line.split("\t");

            if (seg.length != 2) {
            	System.out.println("Seg length != 2");
                continue;
            }
            
            count++;
            
            String key = seg[0];
            String value = seg[1];
            
            Put put = new Put(Bytes.toBytes(key));
            put.setWriteToWAL(false);

            put.add(Bytes.toBytes(family[0]),
                    Bytes.toBytes(column[0]), Bytes.toBytes(value));
            
            putList.add(put);
            
            if(count % 100000 == 0) {
            	count = 0;
                table.put(putList);
                putList.clear();
                System.out.println("Putting data...");
            }

        }
        
        if (putList.size() > 0) {
        	System.out.println("Putting data...");
            table.put(putList);
        }
        table.flushCommits();
        table.close();
        
        br.close();
        System.out.println("Insert data done.");
    }
	
	public static void creatTable(String tableName, String[] family) throws Exception {        
        HTableDescriptor desc = new HTableDescriptor(tableName);
        
        for (int i = 0; i < family.length; i++) {
            HColumnDescriptor temp = new HColumnDescriptor(family[i]);
            temp.setInMemory(true);
            desc.addFamily(temp);
        }
        
        admin.createTable(desc);
        System.out.println("create table Success!");
    }
}

