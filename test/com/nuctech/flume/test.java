package com.nuctech.flume;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;

import com.google.common.base.Preconditions;
import com.nuctech.model.Args;

public class test {
	 private String table="sad";
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
	String baseName="aasda.gfsd.sd";
//		String id = baseName.substring(0, baseName.contains(".")?baseName.indexOf("."):baseName.length());
//	//System.out.println(baseName.contains("."));
//		System.out.println(id);
//		test test = new test();
//		for(int i=0;i<3;i++){
//			Args table = test.getTable();
//			System.out.println(table);
//		}
	//long a = 1460082419502;
	Date date= new Date(Long.parseLong("1460082419502"));
	System.out.print(30%6);
	}
	private Args getTable() throws IOException{
		   Args table;
		  table=new Args();
		  table=new Args();
		  return table;
	}
	
	public void test(){
		Preconditions.checkArgument(table == null, "Please call stop " +
		        "before calling start on an old instance.");
	}

}
