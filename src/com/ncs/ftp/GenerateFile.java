package com.ncs.ftp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

public class GenerateFile {

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
	private static void generateData(String username, String password, String dbURL, String outputFile)
			throws IOException, SQLException, ClassNotFoundException {

		getConnection(username, password,dbURL);

		String strSQL =	"SELECT A.X_CASE_PRODUCT PRODUCT, A.X_CASE_PROBLEM_TYPE PROBLEM, " +
		"A.ID_NUMBER INCIDENT_NO, " +	
		"TO_CHAR(A.CREATION_TIME, 'DD/MM/YYYY HH24:MI') REQ_TIME, " +
		"DECODE(TO_CHAR(A.X_RESOLVED_DATE,'YYYY'),'1753',' ',TO_CHAR(A.X_RESOLVED_DATE,'DD/MM/YYYY HH24:MI')) RESOLVED_TIME, " +
		"DURATION(A.CREATION_TIME,A.X_RESOLVED_DATE,'H') MPR_DURATION, " +
		"DBMS_LOB.SUBSTR(F.X_MPR_DESCRIPTION,4000,1)  MPR_DESCR, " +
		"A.X_ROOT_CAUSE||':'||A.X_ACTUAL_ROOT_CAUSE CAUSE_CODE, " +
		"DBMS_LOB.SUBSTR(F.X_MP_BUSINESS_IMPACT,4000,1)  BUSINESS_IMPACT, " +
		"DBMS_LOB.SUBSTR(C.X_IR_IMMEDIATE_FIX,4000,1)  IR_IMM_FIX, " +
		"DBMS_LOB.SUBSTR(C.X_IR_ROOT_CAUSE,4000,1)  IR_CAUSE, " +
		"DBMS_LOB.SUBSTR(C.X_IR_PREVENTIVE_ACTIONS,4000,1)  PREVENTIVE_ACTIONS, " +
		"F.X_MP_FIXED_STATUS MPR_STAT, " +
		"A.X_SEVERITY SEVERITY, " +
		"A.X_PRIORITY PRIORITY, " +
		"A.X_ACTION_TAKEN||':'||A.X_ACTUAL_ACTION_TAKEN RESOLN_CODE, " +
		"ROUND((decode(to_char(A.X_RESOLVED_DATE,'YYYY'),'1753',SYSDATE, A.X_RESOLVED_DATE) - K.LAST_DESP_DATE),1) DAYS_OPEN, " +
		"A.X_CURRENT_QUEUE SKILL_GRP, " +
		"S.X_SLA_NAME SLA, " +
		"(B.LAST_NAME||', '||B.FIRST_NAME) CONT_NAME, " +
		"TO_CHAR(SYSDATE,'DD/MM/YYYY HH24:MI') TIMESTAMP, " +
		"P.NOTES LOB " +
		"FROM TABLE_CASE A, " +
		"TABLE_CONTACT B, " +
		"TABLE_X_INCIDENT_REPORT C, " +
		"TABLE_X_MAJOR_PROBLEM F, " +
		"TABLE_X_CASE_CALC_DET K, " +
		"TABLE_X_SLA_RULE S, " +
		"TABLE_PART_NUM P " +
		"WHERE A.CASE_REPORTER2CONTACT=B.OBJID " +
		"AND C.OBJID(+)=F.MP2IR " +
		"AND F.OBJID(+)=A.CASE2MP " +
		"AND K.ID_NUMBER=A.ID_NUMBER " +
		"AND A.X_SEVERITY IN ('1','2A','2B') " +
		"AND P.PART_NUMBER=A.X_CASE_PRODUCT " +
		"AND S.OBJID(+)=K.SLA_RULE " +
		"AND A.CREATION_TIME > SYSDATE - 9 " +
		"AND ROWNUM < 7000 ORDER BY A.ID_NUMBER ";
		     
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(strSQL);
		Vector reportData = new Vector();
		int counter = 0;
		while (rs.next()) {
						
			String[] all = {
					rs.getString(1), rs.getString(2), 
					rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getString(6),
					rs.getString(7), rs.getString(8),
					rs.getString(9), rs.getString(10),
					rs.getString(11), rs.getString(12),
					rs.getString(13), rs.getString(14),
					rs.getString(15), rs.getString(16),					
					rs.getString(17), rs.getString(18),
					rs.getString(19), rs.getString(20),
					rs.getString(21), rs.getString(22)
			};
			reportData.add(all);
			counter++;
		}
		// Writes the result into an output files which can be browsed by the user.
		BufferedWriter brw = new BufferedWriter(new FileWriter(new File(
				outputFile)));
		brw.write("\"Product\",\"Problem\",\"Incident No\",\"Req Time\",\"Reslv Time\",\"MPR Dur\",\"MPR Desc\",\"Cause Code\",\"Business Impact\",\"IR Imm Fix\",\"IR Cause\",\"Prev Action\",\"Fix Status\",\"Severity\",\"Priority\",\"Resln Code\",\"Days Open\",\"Skill Group\",\"SLA\",\"Contact\",\"Timestamp\",\"LOB\"");
		brw.newLine();
		String tempStr = "";
		for (int i = 0; i < reportData.size(); i++) {
			String[] next = (String[]) (reportData.elementAt(i));
			for (int j = 0; j < next.length; j++) {
					if (next[j] != null) {
						tempStr = next[j].replaceAll("\"","'");
					} else {
						tempStr = " ";
					}
					if( j == (next.length)-1){
						brw.write("\"" +  tempStr + "\"");
					}else{
						brw.write("\"" +  tempStr + "\"" + ",");
					}
			}
			brw.newLine();
		}
		brw.close();   
		conn.commit();
		conn.close();
	}

	   public static void main( String[] args ) {
	   	try {
	 	  	generateData(args[0],args[1],args[2],args[3]);
	   	}catch( Exception e ) {
			 e.printStackTrace();
	   	}
	   }
}