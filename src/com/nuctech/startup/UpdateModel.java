package com.nuctech.startup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.hbase.HtableAdmin;
import com.nuctech.solr.SolrIndex;
import com.nuctech.utils.Constants;
import com.nuctech.utils.Shell;

public class UpdateModel implements Runnable {
	private static final Logger logger = LoggerFactory
			.getLogger(UpdateModel.class);
	private static Constants constants = Constants.getInstance();
	public static void main(String[] args) throws Exception{
		UpdateModel down = new UpdateModel();
		down.process();
	}
	
	public void process() throws Exception{
		Shell exe = new Shell();
		String dateStr = dateFormat(-1);
		String updateCmd = "/root/batchProcess.sh start"+
							" -t " + constants.getIMAGE_TABLENAME()+dateStr +
							" -z " + constants.getZK_QUORUM() +
							" -d " + constants.getIMAGE_DOWNLOAD_PATH()+
							" -h " + constants.getHDFS_DEFAULT_FS()+
							" -v " + constants.getVERI_MODE_PATH()+
							" -m " + constants.getMAPPING_MODE_PATH()+
							" -p " + constants.getHDFS_MODE_PATH();
		String copyModel="/root/copyModel.sh "+
							constants.getCLUSTER_HOSTINFO() +
							constants.getHDFS_MODE_PATH()+
							constants.getVERI_MODE_PATH();
		String restartStreaming="/root/streamingProcess.sh restart"+
							" -f " + constants.getFLUME_HOST() +
							" -p " + constants.getFLUME_PORT() +
							" -d " + constants.getRECIVE_PATH() +
							" -z " + constants.getZK_QUORUM() +
							" -t " + constants.getRESULT_TABLENAME();
		//批量更新操作
		int result = exe.exec(updateCmd);
		//模型库下发
		if(result==0){
			result = exe.exec(copyModel);
			if(result == 0){
				//重启实时任务
				result = exe.exec(restartStreaming);
			}
		}
		if(result==0){
			logger.info("model update successed!");
		}else{
			logger.info("model update failed!");
		}
	}
	
	public void createImageTable(){
		String dateStr = dateFormat(1);
		String tableName = constants.getIMAGE_TABLENAME() + dateStr;
		HtableAdmin admin = new HtableAdmin();
		admin.createTables(tableName);
	}
	
	public String dateFormat(int add){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, add);
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
		return dateStr;
	}

	@Override
	public void run() {
		try{
			process(); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
