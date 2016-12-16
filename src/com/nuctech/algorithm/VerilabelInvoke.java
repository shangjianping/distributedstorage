package com.nuctech.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.Args;
import com.nuctech.model.Dev;
import com.nuctech.model.ISugDelModelSample;
import com.nuctech.model.ISugDestroy;
import com.nuctech.model.ISugGetDevNames;
import com.nuctech.model.ISugGetHscodeInfo;
import com.nuctech.model.ISugGetHscodesList;
import com.nuctech.model.ISugGetHscodesNum;
import com.nuctech.model.ISugGetSim;
import com.nuctech.model.ISugIni;
import com.nuctech.model.ISugSetThreshold;
import com.nuctech.model.ISugUpdate;
import com.nuctech.model.ISuggest;
import com.nuctech.utils.JaxbUtil;

public class VerilabelInvoke {
	private static final Logger logger = LoggerFactory
			.getLogger(VerilabelInvoke.class);
	 static String globalModel = "";

	 synchronized static void init() {
		if (globalModel == null || globalModel.equals("")||globalModel.equals("-1")) {
			JaxbUtil jax = new JaxbUtil(ISugIni.class);
			String resultXml = verilabel.init();
			logger.info("init result==:" + resultXml);
			ISugIni result = jax.fromXml(resultXml);
			if (Integer.parseInt(result.getResult()) == 0) {
				globalModel = result.getGlobalModel();
			} else {
				globalModel = "-1";
			}
		}
	}

	 static String destroy(String model) {
		Args arg = new Args();
		arg.setGlobalModel(model);
		JaxbUtil jaxI = new JaxbUtil(Args.class);
		String inputXml = jaxI.toXml(arg);
		logger.info("destroy input ==:" + inputXml);
		String resultXml = verilabel.destroy(inputXml);
		logger.info("destroy result==:" + resultXml);
		JaxbUtil jaxR = new JaxbUtil(ISugDestroy.class);
		ISugDestroy result = jaxR.fromXml(resultXml);
		if(result.getResult().equals("0")){
			globalModel="";
		}
		return result.getResult();
	}

	 static ISuggest verifylabelAnalyze(Args arg) {
		ISuggest result = null;
		if (arg != null) {
			init();
			if (globalModel.equals("-1")) {
				return null;
			}
			arg.setGlobalModel(globalModel);
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("Analyze inputXml==:" + inputXml);
			String resultXml = verilabel.verifylabelAnalyze(inputXml);
			logger.info("Analyze result==:" + resultXml);
			JaxbUtil jaxR = new JaxbUtil(ISuggest.class);
			result = jaxR.fromXml(resultXml);
		}
		return result;
	}

	 static String updateModel(Args arg) {
		String rt = "-9";
		if (arg != null) {
			init();
			if (globalModel.equals("-1")) {
				return "-8";
			}
			arg.setGlobalModel(globalModel);
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("updateModel inputXml==:" + inputXml);
			String resultXml = verilabel.updateModel(inputXml);
			logger.info("updateModel result==:" + resultXml);
			JaxbUtil jaxR = new JaxbUtil(ISugUpdate.class);
			ISugUpdate result = jaxR.fromXml(resultXml);
			if (result.getResult() == null) {
				rt = "";
			} else {
				rt = result.getResult();
			}
		}
		return rt;
	}

	 static ISugGetSim getThresholdTotal(Args arg) {
		ISugGetSim result = null;
		if (arg != null) {
			init();
			if (globalModel.equals("-1")) {
				return null;
			}
			arg.setGlobalModel(globalModel);
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("getThresholdTotal inputXml==:" + inputXml);
			String resultXml = verilabel.getThresholdTotal(inputXml);
			logger.info("getThresholdTotal result==:" + resultXml);
			JaxbUtil jaxR = new JaxbUtil(ISugGetSim.class);
			result = jaxR.fromXml(resultXml);
			destroy(globalModel);
		}
		return result;
	}

	 static ISugGetDevNames getAvailableDevice() {
		init();
		if (globalModel.equals("-1")) {
			return null;
		}
		String resultXml = verilabel.getAvailableDevice();
		logger.info("getAvailableDevice result==:" + resultXml);
		JaxbUtil jaxR = new JaxbUtil(ISugGetDevNames.class);
		ISugGetDevNames result = jaxR.fromXml(resultXml);
		destroy(globalModel);
		return result;
	}
	
	 static ISugGetHscodesNum getNumberHscodes(Args arg) {
		ISugGetHscodesNum result = null;
		if (arg != null) {
			init();
			if (globalModel.equals("-1")) {
				return null;
			}
			boolean flag = false;
			String devNo = arg.getDeviceinfo().getDeviceno();
			//判断设备号是否存在
			String resultDev = verilabel.getAvailableDevice();
			JaxbUtil jaxDev = new JaxbUtil(ISugGetDevNames.class);
			ISugGetDevNames devs = jaxDev.fromXml(resultDev);
			for(Dev dev :devs.getDev()){
				if(devNo.equals(dev.getDevName())){
					flag=true;
					break;
				}
			}
			if(flag){
				arg.setGlobalModel(globalModel);
				JaxbUtil jaxI = new JaxbUtil(Args.class);
				String inputXml = jaxI.toXml(arg);
				logger.info("getNumberHscodes inputXml==:" + inputXml);
				String resultXml = verilabel.getNumberHscodes(inputXml);
				logger.info("getNumberHscodes result==:" + resultXml);
				JaxbUtil jaxR = new JaxbUtil(ISugGetHscodesNum.class);
				result = jaxR.fromXml(resultXml);
			}else{
				logger.info(devNo+":devName not contains in Model");
			}
			destroy(globalModel);
		}
		return result;
	}
	
	 static ISugGetHscodesList getHscodeList(Args arg) {
		ISugGetHscodesList result = null;
		if (arg != null) {
			init();
			if (globalModel.equals("-1")) {
				return null;
			}
			boolean flag = false;
			String devNo = arg.getDeviceinfo().getDeviceno();
			//判断设备号是否存在
			String resultDev = verilabel.getAvailableDevice();
			JaxbUtil jaxDev = new JaxbUtil(ISugGetDevNames.class);
			ISugGetDevNames devs = jaxDev.fromXml(resultDev);
			for(Dev dev :devs.getDev()){
				if(devNo.equals(dev.getDevName())){
					flag=true;
					break;
				}
			}
			if(flag){
				arg.setGlobalModel(globalModel);
				JaxbUtil jaxI = new JaxbUtil(Args.class);
				String inputXml = jaxI.toXml(arg);
				logger.info("getHscodeList inputXml==:" + inputXml);
				;
				String resultXml = verilabel.getHscodeList(inputXml);
				logger.info("getHscodeList result==:" + resultXml);
				JaxbUtil jaxR = new JaxbUtil(ISugGetHscodesList.class);
				result = jaxR.fromXml(resultXml);
			}else{
				logger.info(devNo+":devName not contains in Model");
			}
			destroy(globalModel);
		}
		return result;
	}
	
	 static ISugGetHscodeInfo getHscodeInfo(Args arg) {
		ISugGetHscodeInfo result = null;
		if (arg != null) {
			init();
			if (globalModel.equals("-1")) {
				return null;
			}
			arg.setGlobalModel(globalModel);
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("getHscodeInfo inputXml==:" + inputXml);
			String resultXml = verilabel.getHscodeInfo(inputXml);
			logger.info("getHscodeInfo result==:" + resultXml);
			JaxbUtil jaxR = new JaxbUtil(ISugGetHscodeInfo.class);
			result = jaxR.fromXml(resultXml);
			destroy(globalModel);
		}
		return result;
	}
	 
	 static ISugSetThreshold setHscodeThreshold(Args arg) {
		 ISugSetThreshold result = null;
			if (arg != null) {
				init();
				if (globalModel.equals("-1")) {
					return null;
				}
				arg.setGlobalModel(globalModel);
				JaxbUtil jaxI = new JaxbUtil(Args.class);
				String inputXml = jaxI.toXml(arg);
				logger.info("setHscodeThreshold inputXml==:" + inputXml);
				String resultXml = verilabel.setHscodeThreshold(inputXml);
				logger.info("setHscodeThreshold result==:" + resultXml);
				JaxbUtil jaxR = new JaxbUtil(ISugSetThreshold.class);
				result = jaxR.fromXml(resultXml);
				destroy(globalModel);
			}
			return result;
		}
	 
	 static ISugDelModelSample setDelModelSample(Args arg) {
		 ISugDelModelSample result = null;
			if (arg != null) {
				init();
				if (globalModel.equals("-1")) {
					return null;
				}
				arg.setGlobalModel(globalModel);
				JaxbUtil jaxI = new JaxbUtil(Args.class);
				String inputXml = jaxI.toXml(arg);
				logger.info("setDelModelSample inputXml==:" + inputXml);
				String resultXml = verilabel.setDelModelSample(inputXml);
				logger.info("setDelModelSample result==:" + resultXml);
				JaxbUtil jaxR = new JaxbUtil(ISugDelModelSample.class);
				result = jaxR.fromXml(resultXml);
				destroy(globalModel);
			}
			return result;
		}

}
