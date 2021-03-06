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

public class GenerateHistory {

	/**
	 * @param args
	 */
	static Connection conn;
	static String num = "";
	static String type = "initialType";
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
	private static void generateHistory(String username, String password, String dbURL, String outputFile)
			throws IOException, SQLException, ClassNotFoundException {

		getConnection(username, password,dbURL);

		String strSQL =	"select * from VW_EXP_History  " ;
		
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(strSQL);
		BufferedWriter brw = new BufferedWriter(new FileWriter(new File(
				outputFile)));
		int counter = 0;
		
		while (rs.next()) {
			num = rs.getString("objid")	;
			type = rs.getString("obj_type");
			Clob c = rs.getClob("HISTORY");
			
			int i;
			
			BufferedReader brr = new BufferedReader(c.getCharacterStream())  ;
			
		
		    
		    while((i=brr.read())!=-1){
		    	StringWriter sw = new StringWriter();
		    	sw.write(i);
		    	
		    	String str =sw.toString();
		    	brw.write(str);
			} 
			
			/*Testing
			String tmp;
			while((tmp=brr.readLine())!=null){
				brw.write(tmp);
				
			}
			Testing
			*/
			brw.newLine();
			counter++;
		}
		
		
		brw.close();
		 
		conn.commit();
		conn.close();
	}
	
	public static void main(String[] args) throws IOException {
		try {
	 	  	generateHistory(args[0],args[1],args[2],args[3]);
		
	   	}catch( Exception e ) {
	   		String s = "";
	   		char c = type.charAt(0);
	   		switch (c) {
	   		case 'P': 
	   			s= "TABLE_phone_log";
	   			break;
	   		case 'N': 
	   			s= "TABLE_NOTES_log";
	   			break;
	   		case 'E': 
	   			s= "TABLE_EMAIL_log";
	   			break;
	   		default:
	   			s= "TABLE_GBST_ELM";
	   			break;
	   			
	   		}
	   		System.out.println("System failed at record objid = "+ num + " IN TBALE "  + s);
	   		Process proc = Runtime.getRuntime().exec("/clarify/clfy12.5/ftpbatch/bin/sendEmail.sh");
	   		e.printStackTrace();
	   	}
	}

}
