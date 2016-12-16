package com.nuctech.utils;



public class FormatConvertUtil {
	
	/**
	 * byte[] 转 Int
	 * @param bytes
	 * @return
	 */
	public static int byteToInt(byte[] bytes) {
	    int iOutcome = 0;
	    byte bLoop;
	    for (int i = 0; i < bytes.length; i++) {
	        bLoop = bytes[i];
	        iOutcome += (bLoop & 0xFF) << (8 * i);
	    }
	    return iOutcome;
	}
	
	/**
	 * Int 转 byte[]
	 * @param iSource 要转换的int
	 * @param iArrayLen 生成的byte[]的长度
	 * @return
	 */
	public static byte[] intToByte(int iSource,int iArrayLen){
		{
		    byte[] bLocalArr = new byte[iArrayLen];
		    for (int i = 0; i < iArrayLen; i++) {
		        bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		    }
		    return bLocalArr;
		}
	}
	
	/**
	 *  byte[] 转 String
	 * @param bytes
	 * @return
	 */
	public static String byteToString(byte[] bytes){
		return new String(bytes);
	}
	
	/**
	 * String 转 byte[] 
	 * @param sSource 待转的string
	 * @param iArrayLen 生成的byte[]的长度
	 * @return
	 * @throws EcmSocketException
	 */
	public static byte[] stringToByte(String sSource,int iArrayLen) throws Exception{
		byte[] bytes = new byte[iArrayLen];
		byte[] bytess = sSource.getBytes();
		StringBuffer sb = new StringBuffer();
		sb.append(sSource);
		if(bytes.length<bytess.length){
			throw new Exception("字符串太长："+sSource);
		}
		else{
			for(int i = 0;i<bytes.length-bytess.length;i++){
				sb.append(" ");
			}
		}
		return (sb.toString()).getBytes();
	}

	
}
