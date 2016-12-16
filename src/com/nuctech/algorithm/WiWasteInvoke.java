package com.nuctech.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.Args;
import com.nuctech.model.IWasteDestory;
import com.nuctech.model.IWasteDetect;
import com.nuctech.model.IWasteIni;
import com.nuctech.utils.Constants;
import com.nuctech.utils.JaxbUtil;

public class WiWasteInvoke {
	private static final Logger logger = LoggerFactory
			.getLogger(WiWasteInvoke.class);
	private static Constants constants = Constants.getInstance();
	 static String iniResult ="";
	  synchronized  static  void  wiWasteInit() {
		if(iniResult==null||iniResult.equals("")){
			Args arg = new Args();
			arg.setWasteModePath(constants.getWASTE_MODE_PATH());
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("wiWasteDetect inputXml==:"+inputXml);
			String resultXml = wiWasteClueJni.wiWasteInit(inputXml);
			JaxbUtil jax = new JaxbUtil(IWasteIni.class);
			logger.info("wiWasteInit result==:"+resultXml);
			IWasteIni iWasteIni = jax.fromXml(resultXml);
			if(iWasteIni==null ||iWasteIni.getResult()==null|| !iWasteIni.getResult().equals("0")){
				iniResult="-1";
			}else{
				iniResult="0";
			}
		}
	}
	
	
	 static IWasteDetect wiWasteDetect(Args arg){
		 IWasteDetect result=null;
		if (arg != null) {
			wiWasteInit();
			if(iniResult.equals("-1")){
				return null;
			}
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("wiWasteDetect inputXml==:"+inputXml);
			String resultXml = wiWasteClueJni.wiWasteDetect(inputXml);
			logger.info("wiWasteDetect result==:"+resultXml);
			JaxbUtil jaxR = new JaxbUtil(IWasteDetect.class);
			result = jaxR.fromXml(resultXml);
		}
		return result;
	}
	 
	 static String wiWasteDestory() {
			String resultXml = wiWasteClueJni.wiWasteDestory();
			logger.info("wiWasteDestory result==:" + resultXml);
			JaxbUtil jaxR = new JaxbUtil(IWasteDestory.class);
			IWasteDestory result = jaxR.fromXml(resultXml);
			if(result.getResult().equals("0")){
				iniResult="";
			}
			return result.getResult();
		}

}
