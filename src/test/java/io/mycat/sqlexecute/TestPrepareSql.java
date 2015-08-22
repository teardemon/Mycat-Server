package io.mycat.sqlexecute;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class TestPrepareSql {

	static {
		try {
			// 加载MySql的驱动类
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("找不到驱动程序类 ，加载驱动失败！");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		testServerPrepareSelectNormal();
//		testServerPrepareSelectWithBingingParam();
		testServerPrepareInsertWithBingParam();
	}
	
	public static void testServerPrepareInsertWithBingParam() {
		String url = "jdbc:mysql://localhost:8066/TESTDB?useServerPrepStmts=true"; // 使用服务端预处理
//		String url = "jdbc:mysql://localhost:8066/TESTDB";
		String user = "test";
		String password = "test";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			String sql = "insert into company(id,name) values(?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			int startId = 100;
			int batchSize = 10;
			int count = 0;
			while(count < batchSize) {
				pstmt.setInt(1, startId);
				pstmt.setString(2, "测试公司" + startId);
				startId++;
				count++;
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.setAutoCommit(true);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void testServerPrepareSelectWithBingingParam() {
		String url = "jdbc:mysql://localhost:8066/TESTDB?useServerPrepStmts=true"; // 使用服务端预处理
//		String url = "jdbc:mysql://localhost:8066/TESTDB";
		String user = "test";
		String password = "test";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			String sql = "select * from company where id > ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
			for(int i = 1; i <= columns; i++) { // 输出列名
				System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			while(rs.next()) {
				for(int i = 1; i <= columns; i++) { // 输出行
					System.out.print(rs.getObject(i) + "\t");
				}
				System.out.println();
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void testServerPrepareSelectNormal() {
		String url = "jdbc:mysql://localhost:8066/TESTDB?useServerPrepStmts=true"; // 使用服务端预处理
//		String url = "jdbc:mysql://localhost:8066/TESTDB";
		String user = "test";
		String password = "test";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			String sql = "select * from company";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
			for(int i = 1; i <= columns; i++) { // 输出列名
				System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			while(rs.next()) {
				for(int i = 1; i <= columns; i++) { // 输出行
					System.out.print(rs.getObject(i) + "\t");
				}
				System.out.println();
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
