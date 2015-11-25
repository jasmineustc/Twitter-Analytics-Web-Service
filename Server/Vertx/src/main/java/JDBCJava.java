import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JDBCJava {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "123456";
	Connection conn = null;

	JDBCJava() throws SQLException {
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	}

	public String query(String key) {

		int q1 = key.indexOf("q1");
		int q2 = key.indexOf("q2");
		int q3 = key.indexOf("q3");
		int q4 = key.indexOf("q4");
		int q5 = key.indexOf("q5");
		int q6 = key.indexOf("q6");

		// do query
		if (q2 != -1) {
			return doQ2(key);
		} else if (q3 != -1) {
			return doQ3(key);
		} else if (q4 != -1) {
			return doQ4(key);
		} else if (q5 != -1) {
			return doQ5(key);
		} else if (q6 != -1) {
			return doQ6(key);
		}

		return "shouldn't goes here!";

	}

	private String doQ5(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	private String doQ6(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	private String doQ3(String key) {
		StringBuilder sb = new StringBuilder();
		Statement stmt = null;
		key = key.substring(3);
		String[] keys = key.split(",");
		String id = keys[0];
		String start = keys[1];
		String end = keys[2];
		int num = Integer.parseInt(keys[3]);

		try {
			Class.forName("com.mysql.jdbc.Driver");
			stmt = conn.createStatement();
			String sql = "SELECT * from test3 where userid=\'" + id + "\' ";

			ResultSet rs = stmt.executeQuery(sql);

			String result = "";
			while (rs.next()) {
				result += rs.getString("value");
			}
			String[] posts = result.split("#&#");
			int n = posts.length;
			// add positive
			for (int i=0; i<num; i++) {
				String post = posts[i];
				String[] ele = post.split(",");
				String time = ele[0];
				int score = Integer.parseInt(ele[1]);
				if (score > 0 && time.compareTo(start)>=0 && time.compareTo(end) <= 0) {
					sb.append(post);
				} else if (score < 0) {
					break;
				}
			}
			
			// add negative
			for (int i=0; i<num; i++) {
				String post = posts[n-i];
				String[] ele = post.split(",");
				String time = ele[0];
				int score = Integer.parseInt(ele[1]);
				if (score < 0 && time.compareTo(start)>=0 && time.compareTo(end) <= 0) {
					sb.append(post);
				} else if (score > 0) {
					break;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	private String doQ4(String key) {
		StringBuilder sb = new StringBuilder();
		Statement stmt = null;
		key = key.substring(3);
		String[] keys = key.split(",");
		String hashTag = keys[1];
		String n = keys[0];
		try {
			Class.forName("com.mysql.jdbc.Driver");
			stmt = conn.createStatement();
			String sql = "SELECT * from test4 where hashTag=\'" + hashTag
					+ "\' ";

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String response = rs.getString("value");
				response = response.replace("$shit$", "\t");
				String[] posts = response.split("\t");

				int size = Integer.parseInt(n);
				int len = posts.length;

				for (int i = 0; i < size && i < len; i++) {
					String temp = posts[i];

					int left = 0;
					while (left != -1) {
						left = temp.indexOf("$fuck$");
						if (left != -1) {
							temp = temp.substring(0, left) + "\n"
									+ temp.substring(left + 6);
						}
					}
					sb.append(temp);
					sb.append(";");
				}
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String doQ2(String key) {
		// prepare to query
		StringBuilder sb = new StringBuilder();
		Statement stmt = null;
		key = key.substring(3);
		int mid = key.indexOf(",");
		String userid = key.substring(0, mid);
		String timestamp = key.substring(mid + 1);
		try {
			// build sql
			Class.forName("com.mysql.jdbc.Driver");
			stmt = conn.createStatement();

			String sql = "SELECT post from test2 where userid=" + userid
					+ " and newDate=\'" + timestamp + "\';";
			// query
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				sb.append(rs.getString("post"));
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}