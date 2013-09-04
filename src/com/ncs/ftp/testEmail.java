package com.ncs.ftp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class testEmail {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// TODO Auto-generated method stub
		try {
			//DW
//			Process proc = Runtime.getRuntime().exec("/clarify/clfy12.5/ftpbatch/h.sh");
			
			//DEV
			Process proc = Runtime.getRuntime().exec("/u01/clarify/ftpbatchVHA/bin/sendEmail.sh");
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
