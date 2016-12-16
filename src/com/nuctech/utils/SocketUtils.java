package com.nuctech.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketUtils {
	public static final int PORT = 9999;// 监听的端口号

	public static void main(String[] args) {
		System.out.println("服务器启动...\n");
		SocketUtils server = new SocketUtils();
		try {
			server.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() throws IOException {
		ServerSocket serverSocket = null;
		serverSocket = new ServerSocket(PORT);
		
		while (true) {
		
				// 一旦有堵塞, 则表示服务器与客户端获得了连接
			Socket client = serverSocket.accept();
				// 处理这次连接
				send(client);

			
		}
	}

	public void send(Socket client) throws IOException {
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		System.out.print("请输入:\t");
		// 发送键盘输入的一行
		String s = new BufferedReader(new InputStreamReader(System.in))
				.readLine();
		out.writeUTF(s);

		out.close();
	}

}
