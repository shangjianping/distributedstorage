package com.nuctech.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.Args;
import com.nuctech.model.ICigDetect;
import com.nuctech.model.ILiquorDetect;
import com.nuctech.utils.JaxbUtil;

public class WiClueInvoke {
	private static final Logger logger = LoggerFactory
			.getLogger(WiClueInvoke.class);

	static ICigDetect wiCigDetect(Args arg) {
		ICigDetect result = null;
		if (arg != null) {
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("wiCigDetect inputXml==:" + inputXml);
			String resultXml = wiClueJni.wiCigDetect(inputXml);
			logger.info("wiCigDetect result==:" + resultXml);
			JaxbUtil jaxR = new JaxbUtil(ICigDetect.class);
			result = jaxR.fromXml(resultXml);
		}
		return result;
	}

	static ILiquorDetect wiLiquorDetect(Args arg) {
		ILiquorDetect result = null;
		if (arg != null) {
			JaxbUtil jaxI = new JaxbUtil(Args.class);
			String inputXml = jaxI.toXml(arg);
			logger.info("wiLiquorDetect inputXml==:" + inputXml);
			String resultXml = wiClueJni.wiLiquorDetect(inputXml);
			logger.info("wiLiquorDetect result==:" + resultXml);
			JaxbUtil jaxR = new JaxbUtil(ILiquorDetect.class);
			result = jaxR.fromXml(resultXml);
		}
		return result;
	}

}
