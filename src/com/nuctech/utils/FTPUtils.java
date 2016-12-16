package com.nuctech.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(FTPUtils.class);
	private static Constants constants = Constants.getInstance();
	
	public static FTPClient getFTPClient(String hostname,int port,String username,String password){
		FTPClient client = null;
		try{
			client = new FTPClient();
			client.connect(hostname, port);
			client.login(username, password);
			if(!FTPReply.isPositiveCompletion(client.getReplyCode())){
				logger.info("未连接到FTP，用户名密码错误");
				client.disconnect();
			}else{
				logger.info("FTP连接成功");
			}
		} catch(SocketException e){
			e.printStackTrace();
			logger.error("FTP的IP地址可能错误，请正确配置");
		} catch(IOException e){
			e.printStackTrace();
			logger.error("FTP的IP地址可能错误，请正确配置");
		}
		return client;
	}
	
	public static boolean uplaod(String path){
		String hostname = constants.getFTP_IP();
		int port = constants.getFTP_PORT();
		String username = constants.getFTP_USER();
		String password = constants.getFTP_PASSWD();
		String remotePath = constants.getFTP_SHARE_PATH();
		return upload(hostname,port,username,password,remotePath,new File(path));
	}
	
	public static boolean uplaod(String hostname,int port,String username,String password,String remotePath,String path){
		return upload(hostname,port,username,password,remotePath,new File(path));
	}
	
	public static boolean upload(String hostname,int port,String username,String password,String remotePath,File file){
		try {
			InputStream input = new FileInputStream(file);
			return upload(hostname,port,username,password,remotePath,input,file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(file.getName()+"读取失败");
			return false;
		}
		
	}
	
	public static boolean upload(String hostname,int port,String username,String password,String remotePath,InputStream input,String fileName){
		boolean flag=false;
		FTPClient client = getFTPClient(hostname,port,username,password);
		try{
		client.setFileType(FTPClient.BINARY_FILE_TYPE);
		client.changeWorkingDirectory(remotePath);
		flag = client.storeFile(fileName+".temp", input);
		client.sendSiteCommand("chmod 777 "+fileName+".temp");
		client.rename(fileName+".temp", remotePath+"/spool/"+fileName);
		//client.sendSiteCommand("chmod 777 "+fileName);
		//System.out.println("mv "+remotePath+"/"+fileName+" "+remotePath+"/spool");
		//client.sendSiteCommand("mv "+remotePath+"/"+fileName+" "+remotePath+"/spool");
		input.close();
		client.logout();
		logger.info(fileName+"上传成功");
		}catch(IOException e){
			e.printStackTrace();
			logger.error(fileName+"上传失败");
		}finally{
			if(client.isConnected()){
				try {
					client.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	
	public static boolean download(String hostname,int port,String username,String password,String remotePath,String fileName,String localPath){
		boolean flag=false;
		FTPClient client = getFTPClient(hostname,port,username,password);
		try{
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			client.changeWorkingDirectory(remotePath);
			File localFile = new File(localPath+File.separator+fileName);
			OutputStream is = new FileOutputStream(localFile);
			flag = client.retrieveFile(fileName, is);
			client.logout();
			logger.info(fileName+"下载成功");
		} catch(Exception e){
			e.printStackTrace();
			logger.error(fileName+"下载失败");
		} finally{
			if(client.isConnected()){
				try {
					client.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FTPUtils.uplaod( "D:/bak/86574MB02201402250012.zip");
		//FTPUtils.download("192.168.25.223", 21, "uftp", "123456", "/var/flume/spool", "86574MB02201402250012.zip", "D:/");
	}

}
