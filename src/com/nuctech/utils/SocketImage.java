package com.nuctech.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class SocketImage {

	public void sendData(String path, Socket socket) {
		System.out.println("send data:" + path);
		OutputStream out = null;
		FileInputStream in = null;
		BufferedOutputStream bf = null;
		try {
			out = socket.getOutputStream();
			File file = new File(path);
			System.out.println("send data start:" + path+"  name:"+file.getName()+"  length:"+file.length());
			in = new FileInputStream(file);
			bf = new BufferedOutputStream(out);
			byte[] bt = new byte[(int) file.length()];
			in.read(bt);
			byte[] name=FormatConvertUtil.stringToByte(file.getName(), 21);
			byte[] length=FormatConvertUtil.intToByte((int)file.length(), 4);
			bf.write(name);
			bf.write(length);
			bf.write(bt);
			System.out.println("send data end:" + path+"  name:"+file.getName()+"  length:"+file.length());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	public void init(int port, String path) {
		ServerSocket serverSocket = null;
		String[] list;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("服务启动。。。。。。。。。。。。。。。");
			while (true) {
				list = new File(path).list();
				for (String name : list) {
					// 一旦有堵塞, 则表示服务器与客户端获得了连接
					System.out.println("客户端开始连接。。。。。。。。。。。。。。。");
					Socket client = serverSocket.accept();
					System.out.println("客户端已连接。。。。。。。。。。。。。。。");
					// 处理这次连接
					sendData(path + name, client);
					if(new File("E:/bak/"+name).exists()){
						FileUtils.deleteQuietly(new File(path + name));
					}else{
					FileUtils.moveFileToDirectory(new File(path + name),
							new File("E:/bak"), true);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("服务器异常: " + e.getMessage());
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		args = new String[4];
		args[0] = "11111";
		args[1] = "E:/test1/";
		args[2] = "1";
		args[3] = "1000";
		if (args.length < 4) {
			System.err
					.println("Usage:server3 <port> <file or dir> <send-times> <sleep-time(ms)>");
			System.exit(1);
		}



		SocketImage s = new SocketImage();

		
		s.init(Integer.parseInt(args[0]),args[1]);

	}

	public Map<Integer, String> getMap(String dir, Map<Integer, String> fileMap) {
		File file = new File(dir);
		if (file.isFile()) {
			if (file.getName().endsWith(".jpg")
					|| file.getName().endsWith(".bmp")
					| file.getName().endsWith(".JPG")
					|| file.getName().endsWith(".BMP")) {
				if (file.length() < 1024 * 1024 * 2) {
					fileMap.put(fileMap.size(), file.getAbsolutePath());
				}
			} else {
			}
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int j = 0; j < files.length; j++) {
				getMap(files[j].getAbsolutePath(), fileMap);
			}
		}
		return fileMap;
	}

	public Map<Integer, String> getFileMap(String dir) {
		Map<Integer, String> fileMap = new HashMap<Integer, String>();
		return getMap(dir, fileMap);
	}

	public int getNum(int offset, int max) {
		int i = offset + (int) (Math.random() * max);
		if (i > max) {
			return i - offset;
		} else {
			return i;
		}
	}
}
