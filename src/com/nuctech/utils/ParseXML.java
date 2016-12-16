package com.nuctech.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.Args;
import com.nuctech.model.Custominfo;
import com.nuctech.model.DelDataInfo;
import com.nuctech.model.Deviceinfo;
import com.nuctech.model.Entry;
import com.nuctech.model.EntryItems;
import com.nuctech.model.Image;
import com.nuctech.model.ImageInfo;
import com.nuctech.model.Image_List;
import com.nuctech.model.Meta;

public class ParseXML {
	private static final Logger logger = LoggerFactory
			.getLogger(ParseXML.class);
	private static Constants constants = Constants.getInstance();
	public static Args getArgs(String path) throws Exception {
		File files = new File(path);
		Args arg = null;
		String encoding = "UTF-8";
		List<String> imgs = new ArrayList<String>();
		for (File file : files.listFiles()) {
			if (file.getName().endsWith(".xml")) {
					try {
						arg = parse(file, encoding);
						if(arg==null){
							String lines = transform(file);
							arg = parse(lines, encoding);
						}
					} catch (DocumentException e) {
						try {
							encoding = "Unicode";
							arg = parse(file, encoding);
							if(arg==null){
								String lines = transform(file);
								arg = parse(lines, encoding);
							}
						} catch (Exception e1) {
							// logger.info("Read file [" + filePath + "] error.");
							e.printStackTrace();
							arg = null;
						}
					}
			}
			if (file.getName().endsWith(".img")) {
				imgs.add(file.getName());
			}
		}
		String name = imgNameRule(imgs);
		if (arg != null && !name.equals("")) {
			arg.getImageInfo().setImagefolder(path);
			arg.getImageInfo().setPathtype("LOCAL");
			String type;
			if (name.toUpperCase().contains("HIGH")) {
				type = "HIGH";
			} else if (name.toUpperCase().contains("GRAY")) {
				type = "GRAY";
			} else {
				type = "LOW";
			}
			Image image = new Image();
			image.setPath(name);
			image.setType(type);
			arg.getImageInfo().setImage(image);
			return arg;
		}else{
			return null;
		}
	}
	
	public static String transform(File file){
		try{
		TransformerFactory tFac = TransformerFactory.newInstance();
		//Source xslSource = new StreamSource(new File(constants.getXLS_FILEPATH()+"/default2history.xslt"));
		Source xslSource = new StreamSource(ParseXML.class.getClassLoader().getResourceAsStream("xsl/default2history.xslt"));
		Transformer t = tFac.newTransformer(xslSource);
		Source source = new StreamSource(file);
		Writer returnHtml = new StringWriter();
		Result result = new StreamResult(returnHtml);
		t.transform(source, result);
		logger.info("===transform:"+returnHtml.toString());
		return returnHtml.toString();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static Args parse(String lines, String encoding) throws Exception{
		InputStream input = new ByteArrayInputStream(lines.getBytes(encoding));
		return parse(input,encoding);
	}
	
	
	public static Args parse(File file, String encoding) throws Exception{
		return parse(new FileInputStream(file),encoding);
	}
	
	

	public static Args parse(InputStream input, String encoding)
			throws Exception {
		Args args = new Args();
		int num = 0;
		Custominfo custominfo = new Custominfo();
		ImageInfo imageInfo = new ImageInfo();
		Meta meta = new Meta();
		Deviceinfo dev = new Deviceinfo();
		encoding = encoding == "" ? "UTF-8" : encoding;
		SAXReader saxReader = new SAXReader();
		InputStreamReader reader = new InputStreamReader(input, encoding);
		org.dom4j.Document document = saxReader.read(reader);
		Element root = document.getRootElement();
		if (root == null) {
			return null;
		}
		Element root1 = root.element("IDR_IMAGE");
		if (root1 == null) {
			return null;
		}
		
		
		String picNo = root1.elementText("PICNO");
		if (picNo != null ) {
			imageInfo.setImageid(picNo);
			dev.setDeviceno(picNo.substring(0, 9));
			dev.setSamplename(picNo);
			String scanTime = picNo.substring(9, 17);
			meta.setScanTime(scanTime);
		}
		
		

		
		Element root2 = root1.element("IDR_CHECK_UNIT");
		if (root2 == null) {
			return null;
		}

		@SuppressWarnings("unchecked")
		List<Element> nodes = root2.elements("IDR_SIIG");
		if (nodes == null) {
			return null;
		}
		Element eEDI = null;
		Element inputInfo = null;
		for (Iterator<Element> it = nodes.iterator(); it.hasNext();) {
			Element elm = (Element) it.next();
			Element typeElement = elm.element("TYPE");
			if (typeElement == null) {
				continue;
			}
			if (typeElement.getText().equals("EDI")) {
				eEDI = elm;
			}
			if (typeElement.getText().equals("inputinfo")) {
				inputInfo = elm;
			}
		}
		if (inputInfo != null) {
			Element inputGeneral = inputInfo.element("IDR_SII_INPUTINFO_GENERAL");
			if(inputGeneral !=null){
				Element inputContainer = inputGeneral.element("IDR_SII_INPUTINFO_CONTAINER");
					if(inputContainer !=null){
					//集卡车牌号
					String vesselNo = inputContainer.elementText("name_vessel");
					if (vesselNo != null && !vesselNo.equals("")) {
						meta.setVesselNo(vesselNo);
					}
					//集装箱类型
					String articleNo = inputContainer.elementText("article_no");
					if (articleNo != null && !articleNo.equals("")) {
						meta.setArticleNo(articleNo);
					}
				}
			}
		}
		if (eEDI == null) {
			return null;
		}
		Element root3 = eEDI.element("IDR_SII_EDI_TOTAL");
		if (root3 == null) {
			return null;
		}
		//集装箱号
		String containerNo = root3.elementText("CONTA_ID");
		if (containerNo != null && !containerNo.equals("") ) {
			meta.setContainerNo(containerNo);
		}
		// 多个报关单
		@SuppressWarnings("unchecked")
		List<Element> root4 = root3.elements("IDR_SII_EDI_ENTRY");
		if (root4 == null) {
			return null;
		}
		List<Entry> entryList = new ArrayList<Entry>();
		for (Element element : root4) {
			//提运单号
			String ladingBillNo = element.elementText("LADING_BILL_NO");
			if (ladingBillNo != null && !ladingBillNo.equals("")) {
				meta.setLadingBillNo(ladingBillNo);
			}
			//报关单号
			String declarationNo = element.elementText("ENTRY_ID");
			if (declarationNo != null && !declarationNo.equals("")) {
				meta.setDeclarationNo(declarationNo);
			}
			
			Entry entry = new Entry();

			// 净重
			String netWeight = element.elementText("PACK_MARK");
			if (netWeight != null && !netWeight.equals("")) {
				entry.setNetWeight(netWeight);
			}

			// 毛重
			String grossWeight = element.elementText("GROSS_WT");
			if (grossWeight != null && !grossWeight.equals("")) {
				entry.setGrossWeight(grossWeight);
			}

			// 每单多个报关货物
			@SuppressWarnings("unchecked")
			List<Element> root5 = element.elements("IDR_SII_EDI_ENTRY_ITEM");
			List<EntryItems> itemList = new ArrayList<EntryItems>();

			if (root5 != null) {
				for (Element elem : root5) {
					EntryItems item = new EntryItems();
					// 货物品名
					String goodsName = elem.elementText("GOODS_D");
					if (goodsName != null) {
						item.setName(goodsName);
						meta.setCommodityName(goodsName);
					}
					// 货物类别
					String goodsCategory = elem.elementText("HS_C");
					if (goodsCategory != null) {
						item.setHscode(goodsCategory);
						dev.setHscode(goodsCategory);
						meta.setCommodityCategory(goodsCategory);
						int catalog = Integer.parseInt(goodsCategory.substring(0, 2));
						imageInfo.setCatalog(Catalog.getCatalogMap().get(String.valueOf(catalog)));
					}
					// 数量
					String packageNumber = elem.elementText("QTY_1");
					if (packageNumber != null) {
						item.setPackageNumber(String.valueOf(new Double(Double
								.parseDouble(packageNumber)).intValue()));
					}
					num++;
					itemList.add(item);
				}
			}
			
			@SuppressWarnings("unchecked")
			List<Element> manInfo = element.elements("IDR_SII_EDI_ENTRY_MANINFO");
			Element agentInfo=null;
			Element sellerInfo=null;
			for (Iterator<Element> it = manInfo.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();
				Element typeElement = elm.element("Type");
				if (typeElement == null) {
					continue;
				}
				if (typeElement.getText().equals("AGENT_INFO")) {
					agentInfo = elm;
				}
				if (typeElement.getText().equals("SELLER_INFO")) {
					sellerInfo = elm;
				}
			}
			if(agentInfo!=null){
				String declarationCompany = agentInfo.elementText("Name");
				if(declarationCompany != null){
					meta.setDeclarationCompany(declarationCompany);
				}
			}
			if(sellerInfo!=null){
				String operatingCompany = sellerInfo.elementText("Name");
				if(operatingCompany != null){
					meta.setOperatingCompany(operatingCompany);
				}
			}
			entry.setEntryItems(itemList);

			entryList.add(entry);
		}
		custominfo.setEntry(entryList);
		imageInfo.setGoodsnum(num + "");
		args.setDeviceinfo(dev);
		args.setCustominfo(custominfo);
		args.setImageInfo(imageInfo);
		args.setMeta(meta);
		return args;
	}
	
	public static String imgNameRule(List<String> names) {
		String imgName = "";
		int flag = 1;
		for (String name : names) {
			if (name.toUpperCase().contains("HIGH")) {
				imgName = name;
				flag = 3;
				break;
			} else if (name.toUpperCase().contains("GRAY")) {
				if (flag < 3) {
					imgName = name;
					flag = 2;
				}
			} else {
				if (flag < 2) {
					imgName = name;
				}
			}
		}
		return imgName;
	}
	


	public static void main(String[] args) throws Exception {
		Args arg = ParseXML.getArgs("D:/home/spark/recive/bak/86574MB01201306202017/");
//		arg.setCustominfo(null);
//		arg.setDelDataInfo(null);
//		arg.setDeviceinfo(null);
//		arg.setWasteModePath(null);
//		arg.getImageInfo().setCatalog(null);
//		arg.getImageInfo().setGoodsnum(null);
		JaxbUtil jaxI = new JaxbUtil(Args.class);
		String inputXml = jaxI.toXml(arg);
		logger.info("wiWasteDetect inputXml==:"+inputXml);
		//System.out.println(new XMLFilter().deleteEmptyNode(inputXml));
	}

}
