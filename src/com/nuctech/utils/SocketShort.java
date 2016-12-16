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

public class SocketShort {

	public void sendData(String path, Socket socket) {
		System.out.println("send data:" + path);
		OutputStream out = null;
		FileInputStream in = null;
		BufferedOutputStream bf = null;
		try {
			out = socket.getOutputStream();
			File file = new File(path);
			System.out.println("send data start:" + path + "  name:"
					+ file.getName() + "  length:" + file.length());
			in = new FileInputStream(file);
			bf = new BufferedOutputStream(out);
			byte[] bt = new byte[(int) file.length()];
			in.read(bt);
			byte[] name = FormatConvertUtil.stringToByte(file.getName(), 21);
			byte[] length = FormatConvertUtil.intToByte((int) file.length(), 4);
			bf.write(name);
			bf.write(length);
			bf.write(bt);
			System.out.println("send data end:" + path + "  name:"
					+ file.getName() + "  length:" + file.length());
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
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("服务启动。。。。。。。。。。。。。。。");
			// 一旦有堵塞, 则表示服务器与客户端获得了连接
			System.out.println("客户端开始连接。。。。。。。。。。。。。。。");
			Socket client = serverSocket.accept();
			System.out.println("客户端已连接。。。。。。。。。。。。。。。");
			// 处理这次连接
			sendData(path, client);
			if (new File("E:/bak/" + new File(path).getName()).exists()) {
				FileUtils.deleteQuietly(new File(path));
			} else {
				FileUtils.moveFileToDirectory(new File(path),
						new File("E:/bak"), true);
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
		args[1] = "E:/test1/Image0001.JPG";
		args[2] = "1";
		args[3] = "1000";
		if (args.length < 4) {
			System.err
					.println("Usage:server3 <port> <file or dir> <send-times> <sleep-time(ms)>");
			System.exit(1);
		}

		SocketShort s = new SocketShort();

		s.init(Integer.parseInt(args[0]), args[1]);

	}

	
}
