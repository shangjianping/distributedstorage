package com.nuctech.algorithm;

import java.io.File;

import com.nuctech.model.Args;
import com.nuctech.model.ICigDetect;
import com.nuctech.model.ILiquorDetect;
import com.nuctech.model.ISearchPic;
import com.nuctech.model.ISuggest;
import com.nuctech.model.IWasteDetect;
import com.nuctech.model.Image;
import com.nuctech.model.ImageInfo;
import com.nuctech.model.IwiMappingAddModel;
import com.nuctech.model.IwiMappingBatchDel;
import com.nuctech.model.IwiMappingDelModel;
import com.nuctech.model.IwiMappingGetImageNames;
import com.nuctech.utils.ParseXML;

public class testAlgro {
	public static void main(String[] args) throws Exception {
		System.out.println(System.getProperty("java.library.path"));
		// String resultXml =
		// "<iSugDestroy>		<result>0</result>		</iSugDestroy>";
		Args arg = ParseXML.getArgs(args[1]);
		arg.setCustominfo(null);
		arg.setDelDataInfo(null);
		arg.setDeviceinfo(null);
		arg.setWasteModePath(null);
		arg.getImageInfo().setCatalog(null);
		arg.getImageInfo().setGoodsnum(null);
		// Args arg = new Args();
		// Deviceinfo aa = new Deviceinfo();
		// aa.setDeviceno(args[0]);
		// aa.setHscode(args[1]);
		// arg.setDeviceinfo(aa);
		// arg1.getDeviceinfo().setDeviceno("DEFAULT");
		// Algorithm.setHscodeThreshold(arg);
		// Algorithm.getThresholdTotal(arg);
		if (args[0].equals("veri")) {
			ISuggest aa = Algorithm.verifylabelAnalyze(arg);
			System.out.println(aa.getPath());
		} else if (args[0].equals("search")) {
			ISearchPic aa = Algorithm.search(arg);
			System.out.println(aa.getPicNum());
		} else if (args[0].equals("updateAdd")) {
			IwiMappingAddModel bb = Algorithm.updateSearchModelAdd(arg);
			System.out.println(bb.getResult());
		} else if (args[0].equals("updateDel")) {
			Args a = new Args();
			ImageInfo imageInfo = new ImageInfo();
			Image image = new Image();
			imageInfo.setImage(image);
			a.setImageInfo(imageInfo);
			for (int i = 2; i < args.length; i++) {
				if (args[i].equals("id")) {
					a.getImageInfo()
							.setImageid(arg.getImageInfo().getImageid());
				}
				if (args[i].equals("folder")) {
					a.getImageInfo().setImagefolder(
							arg.getImageInfo().getImagefolder());
				}
				if (args[i].equals("pathtype")) {
					a.getImageInfo().setPathtype(
							arg.getImageInfo().getPathtype());
				}
				if (args[i].equals("goodnum")) {
					a.getImageInfo().setGoodsnum(
							arg.getImageInfo().getGoodsnum());
				}
				if (args[i].equals("path")) {
					a.getImageInfo().getImage()
							.setPath(a.getImageInfo().getImage().getPath());
				}
				if (args[i].equals("type")) {
					a.getImageInfo().getImage()
							.setType(a.getImageInfo().getImage().getType());
				}
				if (args[i].equals("test")) {
					File file = new File("/tmp/"+arg.getImageInfo().getImageid()+"high.img");
					if(file.exists()){
						file.createNewFile();
					}
					a.getImageInfo()
							.setImageid(arg.getImageInfo().getImageid());
					a.getImageInfo().setImagefolder("/tmp/");
					a.getImageInfo().setPathtype("LOCAL");
					a.getImageInfo().setGoodsnum(
							arg.getImageInfo().getGoodsnum());
					a.getImageInfo().getImage()
							.setPath(arg.getImageInfo().getImageid()+"high.img");
					a.getImageInfo().getImage()
							.setType("HIGH");
				}
			}
			IwiMappingDelModel cc = Algorithm.updateSearchModelDel(a);
			System.out.println(cc.getResult());
		} else if (args[0].equals("getName")) {
			IwiMappingGetImageNames dd = Algorithm.getImageNames();
			System.out.println(dd.getImageNum());
		} else if (args[0].equals("batch")) {
			IwiMappingBatchDel ee = Algorithm.batchDeleteData(arg);
			System.out.println(ee.getCount());
		} else if (args[0].equals("cigdetect")) {
			ICigDetect ee = Algorithm.wiCigDetect(arg);
			System.out.println(ee.getAreaNum());
		} else if (args[0].equals("liqdetect")) {
			ILiquorDetect ee = Algorithm.wiLiquorDetect(arg);
			System.out.println(ee.getAreaNum());
		}else if (args[0].equals("waste")) {
			IWasteDetect ee = Algorithm.wiWasteDetect(arg);
			System.out.println(ee.getResult());
		}
		
		

	}
}
