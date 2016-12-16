package com.nuctech.utils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class RemoteShellTool {

	private Connection conn;
	private String ipAddr=constants.getSPARK_IP();
	private String userName=constants.getSPARK_USR();
	private String password=constants.getSPARK_PASSWD();
	private String charset = Charset.defaultCharset().toString();
	private static Constants constants = Constants.getInstance();


	public boolean login() throws IOException {
		conn = new Connection(ipAddr);
		conn.connect(); // 连接
		return conn.authenticateWithPassword(userName, password); // 认证
	}

	public String exec(String cmds) {
		InputStream in = null;
		String result = "";
		try {
			if (this.login()) {
				Session session = conn.openSession(); // 打开一个会话
				session.execCommand(cmds);
				
				in = session.getStdout();
				result = this.processStdout(in, this.charset);
				session.close();
				conn.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	public String processStdout(InputStream in, String charset) {
	
		byte[] buf = new byte[1024];
		StringBuffer sb = new StringBuffer();
		try {
			while (in.read(buf) != -1) {
				sb.append(new String(buf, charset));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
System.out.println(new Date());
		RemoteShellTool tool = new RemoteShellTool();
		String test2 = "nohup /usr/hdp/2.3.2.0-2950/spark/bin/spark-submit --verbose --class  com.nuctech.batchProcess.updataModel --master local  --executor-memory 512m --executor-cores 1 --jars /usr/hdp/2.3.2.0-2950/spark/lib/extend/distributedStorage.jar,/usr/hdp/2.3.2.0-2950/spark/lib/extend/dom4j-1.6.1.jar,/usr/hdp/2.3.2.0-2950/spark/lib/extend/guava-12.0.1.jar,/usr/hdp/2.3.2.0-2950/spark/lib/extend/hbase-client-0.98.4.2.2.4.2-2-hadoop2.jar,/usr/hdp/2.3.2.0-2950/spark/lib/extend/hbase-common-0.98.4.2.2.4.2-2-hadoop2.jar,/usr/hdp/2.3.2.0-2950/spark/lib/extend/hbase-protocol-0.98.4.2.2.4.2-2-hadoop2.jar,/usr/hdp/2.3.2.0-2950/spark/lib/extend/hbase-server-0.98.4.2.2.4.2-2-hadoop2.jar,/usr/hdp/2.3.2.0-2950/spark/lib/extend/htrace-core-2.04.jar,/usr/hdp/2.3.2.0-2950/spark/lib/extend/jaxen-1.1-beta-6.jar,/usr/hdp/2.3.2.0-2950/spark/lib/extend/spark-streaming-flume_2.11-1.2.1.jar,/usr/hdp/2.3.2.0-2950/spark/lib/extend/zip4j_1.3.2.jar /root/sparkJob.jar image20151211 master.node /home/spark/download hdfs://master.node:8020 /usr/local /usr/local/jdk1.7.0_45/bin /tmp  2>&1";
		String test1="/root/batchTest.sh image20151211 master.node /home/spark/download hdfs://master.node:8020 /usr/local /usr/local/jdk1.7.0_45/bin /tmp";
		String result = tool.exec(test2);
		System.out.print(result);
		System.out.println(new Date());

	}

}

