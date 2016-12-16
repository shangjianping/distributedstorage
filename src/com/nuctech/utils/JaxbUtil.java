package com.nuctech.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

/**
 * 使用Jaxb2.0实现XML<->Java Object的Binder.
 * 
 * 特别支持Root对象是List的情形.
 * 
 * @author
 */
public class JaxbUtil implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 多线程安全的Context.
	private JAXBContext jaxbContext;

	/**
	 * @param types
	 *            所有需要序列化的Root对象的类型.
	 */
	public JaxbUtil(Class<?>...types) {
		try {
			jaxbContext = JAXBContext.newInstance(types);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Java Object->Xml.
	 */
	public String toXml(Object root) {
		try {
			StringWriter writer = new StringWriter();
			createMarshaller().marshal(root, writer);
			String result =new XMLFilter().deleteEmptyNode(writer.toString());
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public  void buildXmlFile(Object root,String  filePath,String filename) {
		try {
			File directory=new File(filePath);
			if  (!directory .exists()  && !directory .isDirectory())      
				{       
				directory.mkdirs();    
				}
			File ispDatefile=new File(filePath+filename);
			FileOutputStream fileOutputStream=null;
			try {
				fileOutputStream=new FileOutputStream(ispDatefile);
				createMarshaller().marshal(root,fileOutputStream);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(fileOutputStream!=null){
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
    }
	/**
	 * Xml->Java Object.
	 */
	@SuppressWarnings("unchecked")
	public <T> T buildXmlFileToStr(String path) {
		try {
			
			 File file = new File(path);
			 if(!file.exists()){
					return null;
			}else{
				return (T) createUnmarshaller().unmarshal(file);	
			}
			
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
   
	/**
	 * Java Object->Xml, 特别支持对Root Element是Collection的情形.
	 */
	public String toXml(Collection<?> root, String rootName, String encoding) {
		try {
			CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;
			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(
					new QName(rootName), CollectionWrapper.class, wrapper);
			StringWriter writer = new StringWriter();
			createMarshaller().marshal(wrapperElement, writer);

			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Xml->Java Object.
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromXml(String xml) {
		try {
			StringReader reader = new StringReader(xml);
			return (T) createUnmarshaller().unmarshal(reader);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Xml->Java Object, 支持大小写敏感或不敏感.
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromXml(String xml, boolean caseSensitive) {
		try {
			String fromXml = xml;
			if (!caseSensitive)
				fromXml = xml.toLowerCase();
			StringReader reader = new StringReader(fromXml);
			return (T) createUnmarshaller().unmarshal(reader);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建Marshaller, 设定encoding(可为Null).
	 */
	public Marshaller createMarshaller() {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			return marshaller;
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建UnMarshaller.
	 */
	public Unmarshaller createUnmarshaller() {
		try {
			return jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 封装Root Element 是 Collection的情况.
	 */
	public static class CollectionWrapper {
		@XmlAnyElement
		protected Collection<?> collection;
	}
}

