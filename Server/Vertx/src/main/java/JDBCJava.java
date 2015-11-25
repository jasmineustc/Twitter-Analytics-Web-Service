import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class JDBCJava {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "123456";
	Connection conn = null;

	HashMap<String, String> KeyStore2 = new HashMap<String, String>();
	HashMap<String, String> KeyStore3 = new HashMap<String, String>();
	HashMap<String, String> KeyStore4 = new HashMap<String, String>();

	JDBCJava() throws SQLException {
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	}

	public String query(String key) {

		int q1 = key.indexOf("q1");
		int q2 = key.indexOf("q2");
		int q3 = key.indexOf("q3");

		Statement stmt = null;
		StringBuilder sb = new StringBuilder();
		
		if (q2 != -1) {
//			if (KeyStore2.containsKey(key)) {
//				return KeyStore2.get(key);
//			}
			try {
				key = key.substring(3);
				// STEP 2: Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver");
				stmt = conn.createStatement();
				int mid = key.indexOf(",");
				String userid = key.substring(0, mid);
				String timestamp = key.substring(mid + 1);
				String sql;
				sql = "SELECT post from test2 where userid=" + userid
						+ " and newDate=\'" + timestamp + "\';";
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					sb.append(rs.getString("post"));
				}
				// STEP 6: Clean-up environment
				rs.close();
				stmt.close();
			} catch (Exception e) {
				// Handle errors for Class.forName
				e.printStackTrace();
			}
			// finally {
			// // finally block used to close resources
			// try {
			// if (stmt != null)
			// stmt.close();
			// } catch (SQLException se2) {
			// }
			// }// end try
			// System.out.println("Goodbye!");
			KeyStore2.put("q2," + key, sb.toString());
			return sb.toString();
		} else if (q3 != -1) {
			try {
				key = key.substring(3);
				Class.forName("com.mysql.jdbc.Driver");
				// STEP 4: Execute a query
				stmt = conn.createStatement();
				String[] keys = key.split(",");

				String userid = keys[0];
				String start = keys[1].substring(0, 4)+keys[1].substring(5, 7)+keys[1].substring(7);
				String end = keys[2].substring(0, 4)+keys[2].substring(5, 7)+keys[2].substring(7);
				int num = Integer.valueOf(keys[3]);
				
				String sql;
				sql = "SELECT * from test where userid between " +userid+start
						+ " and " + userid+start +";";
				System.out.println("&&*:  "+sql);
				
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
						sb.append(rs.getString("userid")+","+rs.getString("post"));
				}
				
				rs.close();
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return sb.toString();
		} else {
			// q4,num,hashtag
//			if (KeyStore4.containsKey(key)) {
//				return KeyStore4.get(key);
//			}
			StringBuilder result = new StringBuilder();
			try {
				key = key.substring(3);
				Class.forName("com.mysql.jdbc.Driver");				 
				stmt = conn.createStatement();
				String[] keys = key.split(",");
				String hashTag = keys[1];				
				String n = keys[0];

				String sql;
				sql = "SELECT * from test5 where hashTag=\'" + hashTag + "\' ";

				ResultSet rs = stmt.executeQuery(sql);
				
				while (rs.next()) {
					String response = rs.getString("value");
					response = response.replace("$shit$", "\t");
					String[] posts = response.split("\t");
					
					
					int size = Integer.parseInt(n);
					int len = posts.length;
//					System.out.println("size: " + String.valueOf(size));
//					System.out.println("len: " + String.valueOf(len));	
					for(int i = 0; i < size && i < len; i++){
						String temp = posts[i];
//						temp.replace("$fuck$", "\n");
						int left = 0;
						while(left != -1){
							left = temp.indexOf("$fuck$");
							if(left != -1){
								temp = temp.substring(0,left) + "\n" + temp.substring(left+6);
							}
						}
						result.append(temp);
						result.append(";");
					}
				}
				rs.close();
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
//			KeyStore4.put("q4," + key, sb.toString());
			return result.toString();
		
		}

	}
}