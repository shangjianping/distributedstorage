package com.nuctech.algorithm;

import com.nuctech.model.Args;
import com.nuctech.model.ICigDetect;
import com.nuctech.model.ILiquorDetect;
import com.nuctech.model.ISearchPic;
import com.nuctech.model.ISugDelModelSample;
import com.nuctech.model.ISugGetDevNames;
import com.nuctech.model.ISugGetHscodeInfo;
import com.nuctech.model.ISugGetHscodesList;
import com.nuctech.model.ISugGetHscodesNum;
import com.nuctech.model.ISugGetSim;
import com.nuctech.model.ISugSetThreshold;
import com.nuctech.model.ISuggest;
import com.nuctech.model.IWasteDetect;
import com.nuctech.model.IwiMappingAddModel;
import com.nuctech.model.IwiMappingBatchDel;
import com.nuctech.model.IwiMappingDelModel;
import com.nuctech.model.IwiMappingGetImageNames;

public class Algorithm {

	// 报关单比对
	public static String destroy(String model) {
		return VerilabelInvoke.destroy(model);
	}

	public static ISuggest verifylabelAnalyze(Args arg) {
		return VerilabelInvoke.verifylabelAnalyze(arg);
	}

	public static String updateModel(Args arg) {
		return VerilabelInvoke.updateModel(arg);
	}

	public static ISugGetSim getThresholdTotal(Args arg) {
		return VerilabelInvoke.getThresholdTotal(arg);
	}

	public static ISugGetDevNames getAvailableDevice() {
		return VerilabelInvoke.getAvailableDevice();
	}

	public static ISugGetHscodesNum getNumberHscodes(Args arg) {
		return VerilabelInvoke.getNumberHscodes(arg);
	}

	public static ISugGetHscodesList getHscodeList(Args arg) {
		return VerilabelInvoke.getHscodeList(arg);
	}

	public static ISugGetHscodeInfo getHscodeInfo(Args arg) {
		return VerilabelInvoke.getHscodeInfo(arg);
	}

	public static ISugSetThreshold setHscodeThreshold(Args arg) {
		return VerilabelInvoke.setHscodeThreshold(arg);
	}

	public static ISugDelModelSample setDelModelSample(Args arg) {
		return VerilabelInvoke.setDelModelSample(arg);
	}

	// 以图找图
	public static ISearchPic search(Args arg) {
		return WiMappingInvoke.search(arg);
	}

	public static IwiMappingAddModel updateSearchModelAdd(Args arg) {
		return WiMappingInvoke.updateSearchModelAdd(arg);
	}

	public static IwiMappingDelModel updateSearchModelDel(Args arg) {
		return WiMappingInvoke.updateSearchModelDel(arg);
	}

	public static IwiMappingGetImageNames getImageNames() {
		return WiMappingInvoke.getImageNames();
	}

	public static IwiMappingBatchDel batchDeleteData(Args arg) {
		return WiMappingInvoke.batchDeleteData(arg);
	}

	// 烟酒识别
	public static ICigDetect wiCigDetect(Args arg) {
		return WiClueInvoke.wiCigDetect(arg);
	}

	public static ILiquorDetect wiLiquorDetect(Args arg) {
		return WiClueInvoke.wiLiquorDetect(arg);
	}

	// 三废识别
	public static IWasteDetect wiWasteDetect(Args arg) {
		return WiWasteInvoke.wiWasteDetect(arg);
	}

	// 三废销毁
	public static String wiWasteDestory() {
		return WiWasteInvoke.wiWasteDestory();
	}

	public static void main(String[] args) {
		// String mode = Algorithm.init();
		// System.out.println("init status:"+mode);
		// if(!mode.equals("-1")){
		// ISuggest is=Algorithm.verifylabelAnalyze(args[0], mode);
		// System.out.println("path"+is.getPath());
		// }
		// String result = Algorithm.destroy(mode);
		// System.out.println("desoty status:"+result);
	}
}
