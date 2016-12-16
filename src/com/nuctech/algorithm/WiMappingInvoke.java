package com.nuctech.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.Args;
import com.nuctech.model.ISearchPic;
import com.nuctech.model.IwiMappingAddModel;
import com.nuctech.model.IwiMappingBatchDel;
import com.nuctech.model.IwiMappingDelModel;
import com.nuctech.model.IwiMappingGetImageNames;
import com.nuctech.model.IwiMappingIni;
import com.nuctech.utils.JaxbUtil;

public class WiMappingInvoke {
	private static final Logger logger = LoggerFactory
			.getLogger(WiMappingInvoke.class);

	static String iniResult = "";

	synchronized static void init() {
		if (iniResult == null || iniResult.equals("")) {
			JaxbUtil jax = new JaxbUtil(IwiMappingIni.class);
			String resultXml = wiMappingJni.init();
			logger.info("init result==:" + resultXml);
			IwiMappingIni mappingIni = jax.fromXml(resultXml);
			if (mappingIni == null || mappingIni.getResult() == null
					|| !mappingIni.getResult().equals("0")) {
				iniResult = "-1";
			} else {
				iniResult = "0";
			}
		}
	}

	static ISearchPic search(Args arg) {
		ISearchPic result = null;
		if (arg != null) {
			init();
			if (iniResult.equals("-1")) {
				return null;
			}
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("search inputXml==:" + inputXml);
			String resultXml = wiMappingJni.search(inputXml);
			logger.info("search result==:" + resultXml);
			JaxbUtil jaxR = new JaxbUtil(ISearchPic.class);
			result = jaxR.fromXml(resultXml);
		}
		return result;
	}

	static IwiMappingAddModel updateSearchModelAdd(Args arg) {
		IwiMappingAddModel result = null;
		if (arg != null) {
			init();
			if (iniResult.equals("-1")) {
				return null;
			}
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("updateSearchModelAdd inputXml==:" + inputXml);
			String resultXml = wiMappingJni.updateSearchModelAdd(inputXml);
			logger.info("updateSearchModelAdd result==:" + resultXml);
			JaxbUtil jaxR = new JaxbUtil(IwiMappingAddModel.class);
			result = jaxR.fromXml(resultXml);
		}
		return result;
	}

	static IwiMappingDelModel updateSearchModelDel(Args arg) {
		IwiMappingDelModel result = null;
		if (arg != null) {
			init();
			if (iniResult.equals("-1")) {
				return null;
			}
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("updateSearchModelDel inputXml==:" + inputXml);
			String resultXml = wiMappingJni.updateSearchModelDel(inputXml);
			logger.info("updateSearchModelDel result==:" + resultXml);
			JaxbUtil jaxR = new JaxbUtil(IwiMappingDelModel.class);
			result = jaxR.fromXml(resultXml);
		}
		return result;
	}

	static IwiMappingGetImageNames getImageNames() {
		IwiMappingGetImageNames result = null;
		init();
		if (iniResult.equals("-1")) {
			return null;
		}
		String resultXml = wiMappingJni.getImageNames();
		logger.info("getImageNames result==:" + resultXml);
		JaxbUtil jaxR = new JaxbUtil(IwiMappingGetImageNames.class);
		result = jaxR.fromXml(resultXml);

		return result;
	}
	
	static IwiMappingBatchDel batchDeleteData(Args arg) {
		IwiMappingBatchDel result = null;
		if (arg != null) {
			init();
			if (iniResult.equals("-1")) {
				return null;
			}
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("batchDeleteData inputXml==:" + inputXml);
			String resultXml = wiMappingJni.batchDeleteData(inputXml);
			logger.info("batchDeleteData result==:" + resultXml);
			JaxbUtil jaxR = new JaxbUtil(IwiMappingBatchDel.class);
			result = jaxR.fromXml(resultXml);
		}
		return result;
	}

}
