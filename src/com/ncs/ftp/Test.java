package com.ncs.ftp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
	 	  	
			File f = new File("C:\\t.txt");
			
			
			BufferedReader brr = new BufferedReader(new FileReader(new File(
					"C:\\t.txt")));
			String s;
			s = brr.readLine();
			System.out.println(s);
			
			brr.close();
			
			BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(
			"C:\\w.txt")));
			
			bwr.write(s);
			
			bwr.close();
			
		
			 

		 //   s=s.replaceAll("\\u0022","\\\\\"");
		 //   s=s.replaceAll("\\","\\\\");
/* 	    
			s=s.replaceAll("\\u005c", "\\\\\\\\");
			s=s.replaceAll("\\u000a", "\\\\n"); 
			s=s.replaceAll("\\u0008", "\\\\b");
			s=s.replaceAll("\\u0009", "\\\\t");
			s=s.replaceAll("\\u000c", "\\\\f");
			s=s.replaceAll("\\u000d", "\\\\r");
			s=s.replaceAll("\\u0022", "\\\\\"");
			s=s.replaceAll("\\u0027", "\\\\'"); */
			
			
			
			
			// brw.write(s);
			
			// System.out.println(s);
		    
			//System.out.println(index);
			
			// brw.close();
			
			
	   	}catch( Exception e ) {
			 e.printStackTrace();
	   	}
	}

}
