package com.ncs.ftp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GenerateSubIncident {

	static Connection conn;

	private static void getConnection(String username, String password, String databaseURL)
			throws SQLException {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(databaseURL, username, password);
		} catch (Exception e) {
			 e.printStackTrace();
			throw new SQLException("Could not connect to database");
		}
	}
	/**
	 * 
	 * Generate report data
	 * 
	 * @param outputFile
	 * @param username
	 * @param password
	 * @param dbURL
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private static void generateIncident(String username, String password, String dbURL, String outputFile)
			throws IOException, SQLException, ClassNotFoundException {

		getConnection(username, password,dbURL);

		String strSQL =	"select * from VW_EXP_SUBCASE " ;
		
		BufferedWriter brw = new BufferedWriter(new FileWriter(new File(
				outputFile)));
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(strSQL);
		
		int counter = 0;
		while (rs.next()) {
						
			Clob c = rs.getClob("SUBCASE");
			int i;
			
			BufferedReader brr = new BufferedReader(c.getCharacterStream())  ;
			
		
		    
		    while((i=brr.read())!=-1){
		    	StringWriter sw = new StringWriter();
		    	sw.write(i);
		    	
		    	String str =sw.toString();
		    	brw.write(str);
			} 
			
			brw.newLine();
			counter++;
		}
		
		brw.close();   
		conn.commit();
		conn.close();
	}
	
	public static void main(String[] args) {
		try {
	 	  	generateIncident(args[0],args[1],args[2],args[3]);
			
	   	}catch( Exception e ) {
			 e.printStackTrace();
	   	}

	}

}
