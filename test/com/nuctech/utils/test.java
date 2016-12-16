package com.nuctech.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.nuctech.model.Area;
import com.nuctech.model.AlgorithmResult;
import com.nuctech.model.Meta;
import com.nuctech.model.InspectInfo;

public class test {
	// 插入排序
	public void insert(int[] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = i - 1; j >= 0; j--) {
				if (array[j + 1] < array[j]) {
					int temp = array[j + 1];
					array[j + 1] = array[j];
					array[j] = temp;
				} else {
					break;
				}
				print(array);
			}
		}
		print(array);
	}

	public void bubbSort(int[] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length - i - 1; j++) {
				if (array[j] > array[j + 1]) {
					int temp = array[j + 1];
					array[j + 1] = array[j];
					array[j] = temp;
				}
				print(array);
			}
		}
		print(array);
	}

	public void chooseSort(int[] array) {
		for (int i = 0; i < array.length; i++) {
			int min = array[i];
			int num = i;
			for (int j = i + 1; j < array.length; j++) {
				if (min > array[j]) {
					min = array[j];
					num = j;
				}
			}
			array[num] = array[i];
			array[i] = min;

			print(array);
		}
	}

	public void quickSort(int[] array) {
		quick(array, 0, array.length - 1);
	}

	public void quick(int[] array, int start, int end) {
		if (start < end) {
			int flag = array[start];
			int left = start;
			int right = end;
			while (start < end) {
				while (start < end && flag < array[end]) {
					end--;
				}
				array[start] = array[end];
				print(array);
				while (start < end && flag >= array[start]) {
					start++;
				}
				array[end] = array[start];
				print(array);
			}

			array[start] = flag;
			System.out.print("--- ");
			print(array);
			quick(array, left, start - 1);
			quick(array, start + 1, right);
		}
	}

	public void print(int[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + " ");
		}
		System.out.println(".");
	}

	public static void beanutils(Result rs) throws Exception {
		InspectInfo resultInfo = new InspectInfo();
		String family;
		String qualifier;
		String value;
		for (Cell cell : rs.listCells()) {
			family = Bytes.toString(cell.getFamilyArray(),
					cell.getFamilyOffset(), cell.getFamilyLength());
			qualifier = Bytes.toString(cell.getQualifierArray(),
					cell.getQualifierOffset(), cell.getQualifierLength());
			value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
					cell.getValueLength());
			System.out.println("column=" + family + ":" + qualifier
					+ "  value=" + value);
			if (BeanUtils.getProperty(resultInfo, family) == null) {
				Constructor<?> con = Class.forName(family).getConstructor();
				BeanUtils.setProperty(resultInfo, "", con.newInstance());
			}
			BeanUtils.setProperty(resultInfo, family + "." + qualifier, value);
		}
	}

	public static String parsePackeName(String name) {
		if (null == name || "".equals(name)) {
			return null;
		}
		String setName = "com.nuctech.model."
				+ name.substring(0, 1).toUpperCase() + name.substring(1);
		return setName;
	}

	public static void newInstance(Object obj, String fieldPath, String field)
			throws Exception {
		Constructor<?> con = Class.forName(test.parsePackeName(field))
				.getConstructor();
		BeanUtils.setProperty(obj, fieldPath, con.newInstance());

	}

	public static void newListInstance(Object bean, String fieldPath,Object obj)
			throws Exception {
		List<Object> list = new ArrayList<Object>(Collections.nCopies(10, obj));
		BeanUtils.setProperty(bean, fieldPath, list);

	}
	
	private static String parseGetMethodName(String fieldName){
		if(null == fieldName||"".equals(fieldName)){
			return null;
		}
		String setName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return setName;
	}
	
	private static Object getList(Object obj,String tmp) throws Exception{
		String[] strs = tmp.split("\\.");
		for (int i = 0; i < strs.length; i++) {
			obj = MethodUtils.invokeExactMethod(obj, parseGetMethodName(strs[i]),null);
		}
		return obj;
	}

	public static void main(String[] args) throws Exception {
		// test aa = new test();
		// int[] array = {3,2,5,3,54,2,6,11,67,4,1};
		// aa.print(array);
		// //aa.insert(array);
		// //aa.bubbSort(array);
		// //aa.chooseSort(array);
		// aa.quickSort(array);
		String family = "algorithmResult";
		String qualifier = "ICigDetect.detectArea[1].rect.top";
		String value = "test";
		InspectInfo resultInfo = new InspectInfo();
		String property = BeanUtils.getProperty(resultInfo, "algorithmResult");
		if (property == null) {
			newInstance(resultInfo, family, family);
		}

		String[] strs = qualifier.split("\\.");
		String field;
		String tmp="";
		for (int i = 0; i < strs.length - 1; i++) {
			if (strs[i].endsWith("]")) {
				field = strs[i].substring(0, strs[i].indexOf("["));
				//String aa = strs[i].substring(strs[i].indexOf("["));
				property = BeanUtils.getProperty(resultInfo, family + tmp+"."+field);
				if (property == null) {
					Object o =ConstructorUtils.invokeConstructor(Class.forName(test.parsePackeName(field)),null);
					newListInstance(resultInfo, family + tmp+"."+field,o);
				}
				tmp+="."+strs[i];
			} else {
				tmp+="."+strs[i];
				property = BeanUtils.getProperty(resultInfo, family + tmp);
				if (property == null) {
					newInstance(resultInfo, family + tmp, strs[i]);
				}
			}
		}
		BeanUtils.setProperty(resultInfo, family + "." + qualifier, value);
		// System.out.println(resultInfo.getMeta().getScanTime());
		System.out.println(resultInfo.getAlgorithmResult().getICigDetect().getDetectArea().get(1).getRect().getTop());
		// List fileHeaderList = zipFile.getFileHeaders();
		//
		// for (int i = 0; i < fileHeaderList.size(); i++) {
		// FileHeader fileHeader = (FileHeader)fileHeaderList.get(i);
		// System.out.println("****File Details for: " +
		// fileHeader.getFileName() + "*****");
		// System.out.println("Name: " + fileHeader.getFileName());
		// System.out.println("Compressed Size: " +
		// fileHeader.getCompressedSize());
		// System.out.println("Uncompressed Size: " +
		// fileHeader.getUncompressedSize());
		// System.out.println("CRC: " + fileHeader.getCrc32());
		// System.out.println("************************************************************");
		// }

	}
}
