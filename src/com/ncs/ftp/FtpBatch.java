package com.ncs.ftp;

import org.apache.commons.net.ftp.*;
import java.io.*;

public class FtpBatch
{

  public static void getDataFile(String username, String password,String ipaddress,String folder,String filename,String destFolder)
  {
/*	  
   String ipaddress="10.28.112.185";
   String username="uat";
   String password="uat";
   String folder="Optus Directory Transfer/Loc2.txt";
   String destFolder="c:\\";
   String filename="Loc2.txt";
*/ 
   try
    {
      // Connect and logon to FTP Server
      FTPClient ftp = new FTPClient();
      ftp.connect( ipaddress );
      ftp.login( username, password );
      System.out.println("Connected to " +  ipaddress + ".");
      System.out.print(ftp.getReplyString());

      // List the files in the directory
      ftp.changeWorkingDirectory( folder );
      File file = new File( destFolder + File.separator + filename );
          FileOutputStream fos = new FileOutputStream( file ); 
          ftp.retrieveFile(filename, fos );
          fos.close();
       // Logout from the FTP Server and disconnect
      ftp.logout();
      ftp.disconnect();

    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
   }


  public static void putDataFile(String localfile, String destfile, String destfolder, String ipaddress, String username, String password)
  {
	  
  	System.out.println("localfile : " + localfile);
  	System.out.println("destfolder : " + destfolder);
  	System.out.println("ipaddress : " + ipaddress);
  	System.out.println("username : " + username);
  	System.out.println("password : " + password);
  	//System.exit(0);
   //String ipaddress="10.28.112.185";
   //String username="uat";
   //String password="uat";
   //String folder="Optus Directory Transfer/Loc2.txt";
   //String destFolder="c:\\";
   //String filename="don.txt";
   	
   try
    {
      // Connect and logon to FTP Server
      FTPClient ftp = new FTPClient();
      ftp.connect( ipaddress );
      ftp.login( username, password );
      System.out.println("Connected to " +  ipaddress + ".");
      System.out.print(ftp.getReplyString());

      // List the files in the directory
      ftp.changeWorkingDirectory( destfolder );
          //File file = new File( localFolder + File.separator + filename );
          File file = new File( localfile );
          FileInputStream fis = new FileInputStream( file ); 					
		  ftp.storeFile(destfile, fis );
          fis.close();
       // Logout from the FTP Server and disconnect
      ftp.logout();
      ftp.disconnect();

    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
   }

/*
    public static void main( String[] args ) {
 	  try {
		putDataFile(args);
		 }
   		catch( Exception e ) {
		 e.printStackTrace();
   	   }
        }
*/
  }



