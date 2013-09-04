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

public class TestSubcase {

	Connection conn;

	private void getConnection(String username, String password,
			String databaseURL) throws SQLException {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(databaseURL, username, password);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("Could not connect to database");
		}
	}

	private  void generateIncident(String username, String password,
			String dbURL, String outputFile) throws IOException, SQLException,
			ClassNotFoundException {

		getConnection(username, password, dbURL);

		String strSQL = "select * from VW_EXP_SUBCASE ";

		BufferedWriter brw = new BufferedWriter(new FileWriter(new File(
				outputFile)));
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(strSQL);

		int counter = 0;
		while (rs.next()) {

			Clob c = rs.getClob("SUBCASE");
			int i;

			BufferedReader brr = new BufferedReader(c.getCharacterStream());

			while ((i = brr.read()) != -1) {
				StringWriter sw = new StringWriter();
				sw.write(i);

				String str = sw.toString();
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
		TestSubcase ts = new TestSubcase();
		
		try {
			ts.generateIncident("sa", "newenv", "jdbc:oracle:thin:@10.28.112.179:1522:clarprdw", "C:\\VW_EXP_SUBCASE_DW_OLD.txt");
			System.out.print("--DW job completed--");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
