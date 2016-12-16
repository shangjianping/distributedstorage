package com.nuctech.startup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;

import org.apache.commons.io.FileUtils;

import com.nuctech.hbase.StorageHBaseNewImpl;
import com.nuctech.hbase.StorageImageImpl;
import com.nuctech.model.Args;
import com.nuctech.model.ImgInfo;
import com.nuctech.model.InspectInfo;
import com.nuctech.storage.Storage;
import com.nuctech.utils.Constants;
import com.nuctech.utils.ParseXML;

public class InitStandardImage {
private static int batchNum=10;
private static Constants constants = Constants.getInstance();
	public void saveImage(String path) throws Exception {
		File pathFile = new File(path);
		int count =1;
		List<ImgInfo> listImg = new ArrayList<ImgInfo>();
		List<InspectInfo> list = new ArrayList<InspectInfo>();
		if (pathFile.isDirectory()) {
			for (File file : pathFile.listFiles()) {
				if (file.getName().endsWith("zip")) {
					String id = file.getName().substring(0,file.getName().indexOf("."));
					System.out.println(id);
					ImgInfo imgInfo = new ImgInfo();
					imgInfo.setId(id);
					imgInfo.setZips(FileUtils.readFileToByteArray(file));
					imgInfo.setState("3");
					listImg.add(imgInfo);
					
					InspectInfo inspectInfo = createMeta(path,id);
					list.add(inspectInfo);
					if(count%batchNum==0){
//						Storage<ImgInfo> storage = new StorageImageImpl(constants.getIMAGE_TABLENAME());
//						storage.connect();
//						storage.insert(listImg);
//						storage.close();
//						listImg.clear();
						
						Storage<InspectInfo> storageInfo = new StorageHBaseNewImpl(constants.getSTANDARD_TABLENAME());
						storageInfo.connect();
						storageInfo.insert(list);
						storageInfo.close();
						list.clear();
						count=0;
					}
					count++;
				}
			}
			if(listImg.size()>0){
//				Storage<ImgInfo> storage = new StorageImageImpl(constants.getIMAGE_TABLENAME());
//				storage.connect();
//				storage.insert(listImg);
//				storage.close();
				System.out.println(constants.getSTANDARD_TABLENAME());
				Storage<InspectInfo> storageInfo = new StorageHBaseNewImpl(constants.getSTANDARD_TABLENAME());
				storageInfo.connect();
				storageInfo.insert(list);
				storageInfo.close();
			}
		}
	}

	public InspectInfo createMeta(String path,String name) throws Exception {
		ZipFile zipFile = new ZipFile(path+File.separator+name+".zip");
	    zipFile.extractAll(path);
		Args arg = ParseXML.getArgs(path+File.separator+name);
		arg.getMeta().setState("0");
		InspectInfo info = new InspectInfo();
		info.setId(name);
		info.setMeta(arg.getMeta());
		FileUtils.deleteDirectory(new File(path+File.separator+name));
		return info;
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		InitStandardImage init = new InitStandardImage();
		init.saveImage("D:/bak");
	}

}
