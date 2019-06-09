package com.glite.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {

	private static final String DRIVERCLASS;
	private static final String URL;
	private static final String USERNAME;
	private static final String PASSWORD;

	static {
		
		Properties properties = new Properties();
		InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DRIVERCLASS = properties.getProperty("dataSource.driver");
		URL = properties.getProperty("dataSource.url");
		USERNAME = properties.getProperty("dataSource.username");
		PASSWORD = properties.getProperty("dataSource.password");
		
	}

	public static void loadDriver() {
		try {
			Class.forName(DRIVERCLASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		Connection conn = null;
		try {
			// 加载驱动
			loadDriver();
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void release(Statement stmt, Connection conn) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt = null;
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}

	public static void release(ResultSet rs, Statement stmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt = null;
		}

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}
}