package com.nuctech.spark;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkConf;
import org.apache.spark.deploy.yarn.Client;
import org.apache.spark.deploy.yarn.ClientArguments;

public class SparkJobSubmit {

	public static void main(String[] args) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss"); 
		String filename = dateFormat.format(new Date());
		String tmp=Thread.currentThread().getContextClassLoader().getResource("").getPath();
		tmp =tmp.substring(0, tmp.length()-8);
		System.out.println(tmp);
		String jars ="";
		for(File file:new File("D:/extend").listFiles()){
			jars +=",/tmp/extend/"+file.getName();
			
		}
		jars = jars.substring(1);
		System.out.println(jars);
		String[] arg0=new String[]{
				"--name","test java submit job to yarn",
				"--class","com.nuctech.flume.FlumeEventProcess",
				"--executor-memory","1G",
				"--jar","/D:/sparkJob/out/artifacts/sparkJob/sparkJob.jar",//
				
				"--arg","192.168.25.223",
				"--arg","11111",
				"--arg","/home/spark/recive",
				"--arg","master.node",
				"--arg","result",
				"--addJars",jars
		};
		
//		SparkSubmit.main(arg0);
		Configuration conf = new Configuration();
		String os = System.getProperty("os.name");
		boolean cross_platform =false;
		if(os.contains("Windows")){
			cross_platform = true;
		}
		conf.setBoolean("mapreduce.app-submission.cross-platform", cross_platform);// 配置使用跨平台提交任务
		conf.set("fs.defaultFS", "hdfs://master.node:8020");// 指定namenode
		conf.set("mapreduce.framework.name","yarn"); // 指定使用yarn框架
		conf.set("yarn.resourcemanager.address","master.node:8050"); // 指定resourcemanager
		conf.set("yarn.resourcemanager.scheduler.address", "master.node:8030");// 指定资源分配器
		conf.set("mapreduce.jobhistory.address","master.node:10020");
		
		 System.setProperty("SPARK_YARN_MODE", "true");

		 SparkConf sparkConf = new SparkConf();
		 ClientArguments cArgs = new ClientArguments(arg0,sparkConf);
		
		new Client(cArgs,conf,sparkConf).run();
	}

}
