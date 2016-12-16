package com.nuctech.utils;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLFilter{
	private static final Logger logger = LoggerFactory
			.getLogger(XMLFilter.class);
	public String deleteEmptyNode(String input) throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new ByteArrayInputStream(input
				.getBytes()));

		while (true) {
			@SuppressWarnings("unchecked")
			List<Element> list = document.selectNodes("//*[not(node())]");
			if (list == null || list.size() <= 0) {
				break;
			}
			for (Element e : list) {
				logger.info("empty node:"+e.getName());
				e.getParent().remove(e);
			}
		}
		return document.asXML();

	}

	public static void main(String[] args) throws DocumentException {
		String xmlOrigin = "<data><a> </a><b>b1</b><awb><awbpre>123</awbpre><awbno></awbno></awb><spls><spl/></spls></data>";
		new XMLFilter().deleteEmptyNode(xmlOrigin);
	}

}
