/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
package com.ncs.ftp;

import com.jcraft.jsch.*;

import java.util.Hashtable;
import java.util.Properties;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class SftpVHA {
	public static void main(String[] arg) {
		
		//dev
//		File fPublic = new File("/u01/clarify/.ssh/id_rsa.pub");
//		File fPrivate = new File("/u01/clarify/.ssh/id_rsa");
		
		//prd
		File fPublic = new File("/clarify/clfy12.5/.ssh/id_rsa.pub");
		File fPrivate = new File("/clarify/clfy12.5/.ssh/id_rsa");
		
		
	    try{
	      
	      String host=arg[0];
	      String user=arg[1];
	      String privateKeyPassword=arg[2];
	      String ftp_local_path=arg[3];
	      String ftp_dest=arg[4];
	      String ftp_file_src=arg[5];
	      String ftp_file_dest=arg[5];
	      
	      byte[] prvkey = readKeysFromFile(fPrivate); 		// Private key must be byte array
	      byte[] pubkey = readKeysFromFile(fPublic);  		// Public key must be byte array
	      byte[] passphrase = privateKeyPassword.getBytes();// PassPgrase must be byte array
	      

	       JSch jsch=new JSch();
	       jsch.addIdentity(user, // String userName
					prvkey, // byte[] privateKey
					pubkey, // byte[] publicKey
					passphrase // byte[] passPhrase
			);

			Session session = jsch.getSession(user, host, 22);
			System.out.println("Session created.");

			// Hashtable config = new Hashtable();

			if (passphrase != null) {
				session.setPassword(passphrase); // set pass
			}
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config); // Session properties
			
			session.connect();
			System.out.println("Session connected.");
			System.out.println("Connected to SFTP server");

			Channel channel = session.openChannel("sftp");
			channel.connect();
			System.out.println("Channel Opened");
			ChannelSftp c = (ChannelSftp) channel;
	      
		  try{
		  	c.lcd(ftp_local_path);
		  	String strList="";
			//strList=c.lpwd();
			//System.out.println(strList);
		  	System.out.println("Remote File listing");
		  	c.cd(ftp_dest);
			c.put(ftp_file_src,ftp_file_dest);
		  	strList+=c.pwd();
		  	System.out.println(strList);
		  	java.util.Vector vv=c.ls(ftp_dest);
		  	if(vv!=null){
		  		for(int ii=0; ii<vv.size(); ii++){
		  			System.out.println(vv.elementAt(ii).toString());
		  			}
		  	}
		  	c.exit();
		  } catch(SftpException e){
		    System.out.println(e.getMessage());
		  }           
	      session.disconnect();
	    }
	    catch(Exception e){
	      System.out.println(e);
	    }
	    System.exit(0);
	  }

	private static byte[] readKeysFromFile(File file) {
		byte[] pub = null;
		
		try {
			pub = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return pub;
	}

	/*  
	  private static String help =
	"      Available commands:\n"+
	"      * means unimplemented command.\n"+
	"cd path                       Change remote directory to 'path'\n"+
	"lcd path                      Change local directory to 'path'\n"+
	"chgrp grp path                Change group of file 'path' to 'grp'\n"+
	"chmod mode path               Change permissions of file 'path' to 'mode'\n"+
	"chown own path                Change owner of file 'path' to 'own'\n"+
	"help                          Display this help text\n"+
	"get remote-path [local-path]  Download file\n"+
	"get-resume remote-path [local-path]  Resume to download file.\n"+
	"get-append remote-path [local-path]  Append remote file to local file\n"+
	"*lls [ls-options [path]]      Display local directory listing\n"+
	"ln oldpath newpath            Symlink remote file\n"+
	"*lmkdir path                  Create local directory\n"+
	"lpwd                          Print local working directory\n"+
	"ls [path]                     Display remote directory listing\n"+
	"*lumask umask                 Set local umask to 'umask'\n"+
	"mkdir path                    Create remote directory\n"+
	"put local-path [remote-path]  Upload file\n"+
	"put-resume local-path [remote-path]  Resume to upload file\n"+
	"put-append local-path [remote-path]  Append local file to remote file.\n"+
	"pwd                           Display remote working directory\n"+
	"stat path                     Display info about path\n"+
	"exit                          Quit sftp\n"+
	"quit                          Quit sftp\n"+
	"rename oldpath newpath        Rename remote file\n"+
	"rmdir path                    Remove remote directory\n"+
	"rm path                       Delete remote file\n"+
	"symlink oldpath newpath       Symlink remote file\n"+
	"rekey                         Key re-exchanging\n"+
	"compression level             Packet compression will be enabled\n"+
	"version                       Show SFTP version\n"+
	"?                             Synonym for help";

	*/
	}