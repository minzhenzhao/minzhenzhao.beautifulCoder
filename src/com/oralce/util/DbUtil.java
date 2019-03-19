package com.oralce.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.management.loading.PrivateClassLoader;

/**
 * 
 * @author llq 数据库连util
 */
public class DbUtil {

	private String dbUrl = "jdbc:mysql://127.0.0.1:3306/db_student_manager?useUnicode=true&characterEncoding=utf8";
	private String dbUser = "root";
	private String dbPassword = "root";
	private String jdbcName = "com.mysql.jdbc.Driver";
	private Connection connection = null;

	public Connection getConnection() {
		try {
			Class.forName(jdbcName);
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			System.out.println("数据库链接成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("数据库链接失败！");
			e.printStackTrace();
		}
		return connection;
	}

	public void closeCon() {
		if (connection != null)
			try {
				connection.close();
				System.out.println("数据库链接已关闭！");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DbUtil dbUtil = new DbUtil();
		dbUtil.getConnection();
	}

}
