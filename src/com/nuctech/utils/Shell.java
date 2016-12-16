package com.nuctech.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class Shell {
	private static final Logger logger = LoggerFactory
			.getLogger(Shell.class);
	private Connection conn;
	private static Constants constants = Constants.getInstance();
	private String ip=constants.getSPARK_IP();
	private String usr=constants.getSPARK_USR();
	private String psword=constants.getSPARK_PASSWD();
	private String charset = Charset.defaultCharset().toString();
	

	private static final int TIME_OUT = 1000 * 5 * 60;


	public Shell(String ip,String usr,String psword){
		this.ip=ip;
		this.usr=usr;
		this.psword=psword;
	}
	
	public Shell() {
	}

	private boolean login() throws IOException {
		conn = new Connection(ip);
		conn.connect();
		return conn.authenticateWithPassword(usr, psword);
	}

	public int exec(String cmds) throws Exception {
		InputStream stdOut = null;
		InputStream stdErr = null;
		String outStr = "";
		String outErr = "";
		int ret = -1;
		try {
			if (login()) {
				Session session = conn.openSession();
				session.execCommand(cmds);

				stdOut = new StreamGobbler(session.getStdout());
				outStr = processStream(stdOut, charset);

				stdErr = new StreamGobbler(session.getStderr());
				outErr = processStream(stdErr, charset);

				session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);

				logger.info("outStr=" + outStr);
				logger.info("outErr=" + outErr);

				ret = session.getExitStatus();
			} else {
				throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
			IOUtils.closeQuietly(stdOut);
			IOUtils.closeQuietly(stdErr);
		}
		return ret;
	}
	
	
	private String processStream(InputStream in, String charset)
			throws Exception {
		String result = IOUtils.toString(in);
		return result;
	}

	public static void main(String args[]) throws Exception {
		Shell exe = new Shell("192.168.123.47","root","Nuctech_123");
		String cmd="/var/flume/datacreate.sh stop";
//		String cmd = "/root/streamingProcess.sh restart -f 192.168.25.223 -p 11111 -d /home/spark/recive -z master.node -t result";
//		String cmd1 = "/root/streamingProcess.sh stop";
//		String cmd2="/root/copyModel.sh 192.168.25.65:123456,192.168.25.66:123456 /tmp /usr/local";
//		String updateCmd = "/root/batchProcess.sh start"+
//				" -t " + constants.getIMAGE_TABLENAME()+"20151211" +
//				" -z " + constants.getZK_QUORUM() +
//				" -d " + constants.getIMAGE_DOWNLOAD_PATH()+
//				" -h " + constants.getHDFS_DEFAULT_FS()+
//				" -v " + constants.getVERI_MODE_PATH()+
//				" -m " + constants.getMAPPING_MODE_PATH()+
//				" -p " + constants.getHDFS_MODE_PATH();
//		String test = "/root/batchTest.sh start" +
//		" -t " + constants.getIMAGE_TABLENAME()+"20151211" +
//		" -z " + constants.getZK_QUORUM() +
//		" -d " + constants.getIMAGE_DOWNLOAD_PATH()+
//		" -h " + constants.getHDFS_DEFAULT_FS()+
//		" -v " + constants.getVERI_MODE_PATH()+
//		" -m " + constants.getMAPPING_MODE_PATH()+
//		" -p " + constants.getHDFS_MODE_PATH();
		//System.out.println(test);
		System.out
				.println(exe.exec(cmd));
	}
}