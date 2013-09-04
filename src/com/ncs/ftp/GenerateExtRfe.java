package com.ncs.ftp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GenerateExtRfe {
	
	static Connection conn;

	private  static void getConnection(String username, String password, String databaseURL)
			throws SQLException {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection(databaseURL, username, password);
		} catch (Exception e) {
			 e.printStackTrace();
			throw new SQLException("Could not connect to database");
		}
	}
	
	public  static void generateExtRfe( String username, String password, String dbURL, String outputFile)
	throws IOException, SQLException, ClassNotFoundException{
		
		getConnection(username, password,dbURL);
		

		String strSQL =	"select * from VW_EXP_EXT_REF " ;
		
		BufferedWriter brw = new BufferedWriter(new FileWriter(new File(
				outputFile)));
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(strSQL);
		
		
		int counter = 0;
		while (rs.next()) {
						
			String s = rs.getString(1);
			
			brw.write(s);
			brw.newLine();
			counter++;
		}
		
		brw.close();   
		conn.commit();
		conn.close();
	}
	
	public static void main(String[] args)  {
		
		try {
			generateExtRfe(args[0],args[1],args[2],args[3]);
			
	   	}catch( Exception e ) {
			 e.printStackTrace();
	   	}
	

	}
		
	
	
}
