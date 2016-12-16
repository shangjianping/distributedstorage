package com.nuctech.startup;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FileUtils;

import com.nuctech.hbase.HBaseImpl;
import com.nuctech.model.FileInfo;

public class UploadImage {
	public static void main(String[] args) throws Exception {
		File[] list;
		FileInfo info;
		System.out.println("服务启动。。。。。。。。。。。。。。。");
		while (true) {
			list = new File(args[0] + "/unzip").listFiles();
			for (File file : list) {
				// 一旦有堵塞, 则表示服务器与客户端获得了连接

				// 处理这次连接
				info = new FileInfo();
				info.setId(file.getName());
				ZipFile zipFile = new ZipFile(args[0] + "/zip/"
						+ file.getName() + ".zip");

				String folderToAdd = file.getAbsolutePath();

				ZipParameters parameters = new ZipParameters();
				parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
				parameters
						.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

				zipFile.addFolder(folderToAdd, parameters);
				info.setScanImageFolderPath(args[0] + "/zip/" + file.getName()
						+ ".zip");
				HBaseImpl aa = new HBaseImpl("zip");
				aa.connect();
				aa.insert(info);
				if (new File(args[0] + "/bak/" + file.getName()).exists()) {
					FileUtils.deleteQuietly(file);
				} else {
					FileUtils.moveDirectoryToDirectory(file, new File(args[0]
							+ "/bak/"), true);
				}
				if (new File(args[0] + "/bak/" + file.getName()+".zip").exists()) {
					FileUtils.deleteQuietly(new File(args[0] + "/zip/" + file.getName()
							+ ".zip"));
				} else {
					FileUtils.moveFileToDirectory(new File(args[0] + "/zip/" + file.getName()
							+ ".zip"), new File(args[0]
							+ "/bak/"), true);
				}
			}
		}
	}
}
