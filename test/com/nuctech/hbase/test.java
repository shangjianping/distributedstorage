package com.nuctech.hbase;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.nuctech.utils.Constants;

public class test {
	String a;

	public test(String a) {
		// TODO Auto-generated constructor stub
		this.a = a;
	}

	void print() {
		System.out.println(a);
		System.out.println(a);
		System.out.println(a);
	}

	public static void main(String[] arg) throws IOException {
		Map<Integer,String> map = new TreeMap<Integer,String>();
				map.put(3,"香烟");
				map.put(5,"酒类");
				map.put(1,"废金属");
				map.put(4,"废纸");
				map.put(2,"废塑料");
		for(Map.Entry<Integer,String> entry: map.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}
	}
}
