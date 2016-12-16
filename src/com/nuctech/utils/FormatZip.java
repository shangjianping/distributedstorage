package com.nuctech.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FormatZip {
	private static final Logger logger = LoggerFactory
			.getLogger(FormatZip.class);
	static int num=0;

	public static void format(File dir,String outputPath) {
		String xmlName = "";
		List<String> imgNames = new ArrayList<String>();
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				format(file,outputPath);
			}
			String fileName = file.getName();
			if (fileName.endsWith(".xml")) {
				xmlName = fileName;
			}
			if (fileName.endsWith(".img")) {
				imgNames.add(fileName);
			}
		}
		if (!xmlName.equals("")) {
			String id = xmlName.substring(0, xmlName.indexOf("."));
			String jpgName = id + ".jpg";
			if (!new File(dir.getAbsoluteFile() + File.separator + jpgName)
					.exists()) {
				jpgName = id + "_icon.jpg";
			}
			String imgName = imgNameRule(id, imgNames);
			if (!new File(dir.getAbsoluteFile() + File.separator + jpgName)
					.exists() || imgName.equals("")) {
				logger.error(dir.getAbsoluteFile() + File.separator + jpgName
						+ " or " + imgName + " not exists!");
			} else {
				try {
					ZipFile zipFile = new ZipFile(outputPath+File.separator+id+".zip");
					ArrayList<File> filesToAdd = new ArrayList<File>();
					filesToAdd.add(new File(dir.getAbsoluteFile()
							+ File.separator + xmlName));
					filesToAdd.add(new File(dir.getAbsoluteFile()
							+ File.separator + jpgName));
					filesToAdd.add(new File(dir.getAbsoluteFile()
							+ File.separator + imgName));
					ZipParameters parameters = new ZipParameters();
					parameters
							.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
					parameters
							.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
					parameters.setRootFolderInZip(id+"/"); 
					zipFile.addFiles(filesToAdd, parameters);
					num++;
					logger.info(dir.getAbsoluteFile()+ File.separator+id);
				} catch (ZipException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String imgNameRule(String id, List<String> names) {
		String imgName = "";
		int flag = 0;
		for (String name : names) {
			if (!name.contains(id)) {
				continue;
			}
			if (name.toUpperCase().contains("HIGH")) {
				imgName = name;
				flag = 3;
				break;
			} else if (name.toUpperCase().contains("GRAY")) {
				if (flag < 3) {
					imgName = name;
					flag = 2;
				}
			} else if (name.toUpperCase().contains("LOW")) {
				if (flag < 2) {
					imgName = name;
					flag = 1;
				}
			} else {
				if (flag < 1) {
					imgName = name;
				}
			}
		}
		return imgName;
	}

	public static void main(String[] args) {
		FormatZip.format(new File("D:\\test\\ImageLibrary"), "D:\\test\\zip");
		logger.info("total num :"+num);
	}

}
