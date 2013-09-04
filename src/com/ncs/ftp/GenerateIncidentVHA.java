package com.ncs.ftp;

import java.io.*;
import java.sql.*;

public class GenerateIncidentVHA
{

    static Connection conn;

    public GenerateIncidentVHA()
    {
    }

    private static void getConnection(String username, String password, String databaseURL)
        throws SQLException
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(databaseURL, username, password);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new SQLException("Could not connect to database");
        }
    }

    private static void generateIncident(String username, String password, String dbURL, String outputFile)
        throws IOException, SQLException, ClassNotFoundException
    {
        getConnection(username, password, dbURL);
        String strSQL = "select * from VW_VHA_DCS ";
        BufferedWriter brw = new BufferedWriter(new FileWriter(new File(outputFile)));
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(strSQL);
        for(int counter = 0; rs.next(); counter++)
        {
            String s = rs.getString(1);
            brw.write(s);
            brw.newLine();
        }
//        System.out.println("------------successfull email ---------");
//   		Process proc = Runtime.getRuntime().exec("/u01/clarify/ftpbatchVHA/bin/sendEmail.sh");
        brw.close();
        conn.commit();
        conn.close();
    }

    public static void main(String args[]) throws IOException
    {
        try
        {
            generateIncident(args[0], args[1], args[2], args[3]);
        }
        catch(Exception e)
        {
        	//dev
//        	System.out.println("------------System failed ---------");
//	   		Process proc = Runtime.getRuntime().exec("/u01/clarify/ftpbatchVHA/bin/sendEmail.sh");
	   		//uat
//	   		System.out.println("------------System failed ---------");
//	   		Process proc = Runtime.getRuntime().exec("/u01/uat/ftpbatchVHA/bin/sendEmail.sh");
//            e.printStackTrace();
//            
            //prd
	   		System.out.println("------------System failed ---------");
	   		Process proc = Runtime.getRuntime().exec("/clarify/clfy12.5/12.5/solaris_server/sftpBatchVHA/bin/sendEmail.sh");
            e.printStackTrace();
        }
    }
}
