package com.ncs.ftp;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SFTPChannel implements SFTPConstants {

//	private static final Logger LOG = Logger.getLogger(SFTPChannel.class.getName());

	public static void main(String[] arg) throws Exception {
		File fPublic = new File(
				"C:\\ncs_cheng\\01. ims\\02. CRs\\521 - Torrent Requirement for Clarify information - Kell\\01. code\\dev\\id_rsa.pub");
		File fPrivate = new File(
				"C:\\ncs_cheng\\01. ims\\02. CRs\\521 - Torrent Requirement for Clarify information - Kell\\01. code\\dev\\id_rsa");

		Session session = null;
		Channel channel = null;

		String ftpHost = SFTPConstants.SFTP_REQ_HOST;
		String port = SFTPConstants.SFTP_REQ_PORT;
		String ftpUserName = SFTPConstants.SFTP_REQ_USERNAME;
		String ftpPassword = SFTPConstants.SFTP_REQ_PASSWORD;

		// int ftpPort = SFTPConstants.SFTP_DEFAULT_PORT;
		// if (port != null && !port.equals("")) {
		// ftpPort = Integer.valueOf(port);
		// }

		byte[] prvkey = readKeysFromFile(fPrivate); // Private key must be byte
													// array
		byte[] pubkey = readKeysFromFile(fPublic); // Public key must be byte
													// array
		byte[] passphrase = ftpPassword.getBytes();// PassPgrase must be byte
		
		System.out.println("------------------------");
		System.out.println("prvkeyc -- " + prvkey);
		System.out.println("pubkey -- " + pubkey);
		System.out.println("passphrase -- " + passphrase);

		// array
		JSch jsch = new JSch(); // 创建JSch对象
		jsch.addIdentity(ftpUserName, // String userName
				prvkey, // byte[] privateKey
				pubkey, // byte[] publicKey
				passphrase // byte[] passPhrase
		);

		session = jsch.getSession(ftpUserName, ftpHost, 22); // 根据用户名，主机ip，端口获取一个Session对象
		System.out.println("Session created.");
		if (ftpPassword != null) {
			session.setPassword(ftpPassword); // 设置密码
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		// session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		System.out.println("Session connected.");

		System.out.println("Opening Channel.");
		channel = session.openChannel("sftp"); // 打开SFTP通道
		channel.connect(); // 建立SFTP通道的连接
		System.out.println("Connected successfully to ftpHost = " + ftpHost
				+ ",as ftpUserName = " + ftpUserName + ", returning: "
				+ channel);

		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}

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

}
