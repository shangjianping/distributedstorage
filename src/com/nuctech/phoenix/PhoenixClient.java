package com.nuctech.phoenix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.nuctech.utils.Constants;

public class PhoenixClient {
	private static Constants constants = Constants.getInstance();
	public static String DRIVER = constants.getPHOENIX_DRIVER();
	public static String url = constants.getPHOENIX_URL();
	private Connection conn = null;
	private Statement stmt = null;
	ResultSet rs = null;

	public void connection() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (conn == null) {
			try {
				conn = DriverManager.getConnection(url);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ResultSet executeQuery(String sql) {

		try {
			//System.out.println("connect...........");
			connection();
			//System.out.println("connect alreay!");
			if (conn == null) {
				return null;
			}
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			//System.out.println("execute alreay!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public void close() {
		try {
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		PhoenixClient c = new PhoenixClient();
		String sql = "select * from \"device\"";
		ResultSet rs = c.executeQuery(sql);
		ResultSetMetaData rsm = rs.getMetaData();
		for (int i = 0; i < rsm.getColumnCount(); i++) {
			System.out.print(rsm.getColumnName(i + 1) + "  ");
		}
		System.out.println();
		System.out
				.println("-----------------------------------------------------------------");
		while (rs.next()) {
			for (int i = 0; i < rsm.getColumnCount(); i++) {
				System.out.print(rs.getObject(i + 1) + "  ");
			}
			System.out.println();
		}
		c.close();
	}

}
