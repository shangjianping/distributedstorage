package com.nuctech.hbase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.AlgorithmResult;
import com.nuctech.model.ICigDetect;
import com.nuctech.model.InspectInfo;
import com.nuctech.model.Meta;
import com.nuctech.model.PageList;
import com.nuctech.model.PageQuery;
import com.nuctech.solr.SolrNewIndex;
import com.nuctech.storage.Storage;
import com.nuctech.utils.ConnectionCxn;
import com.nuctech.utils.Constants;
import com.nuctech.utils.ImageUtils;

/**
 * 
 * @类功能说明：hbase存储实现
 * @作者：shangjianping
 * @创建时间：2015-5-11
 * @版本：V1.0
 */
public class StorageHBaseNewImpl extends Storage<InspectInfo> {
	private static final Logger logger = LoggerFactory
			.getLogger(StorageHBaseNewImpl.class);
	private static Constants constants = Constants.getInstance();
	private static Configuration config = ConnectionCxn.getHbaseConf();
	private Connection connection;
	private String tableName;

	public StorageHBaseNewImpl(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 连接hbase
	 */
	@Override
	public void connect() {
		try {
			if (connection == null) {
				connection = ConnectionFactory.createConnection(config);
				;
			}
		} catch (java.io.IOException e) {
			e.printStackTrace();
			logger.error("connect hbase fail");
		}
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 关闭hbase连接
	 * @param table
	 */
	@Override
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (java.io.IOException e) {
			e.printStackTrace();
			logger.error("disconnect hbase fail");
		}
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 获取htable实例
	 * @param tableName
	 * @return
	 */
	private Table getTable() {
		Table table = null;
		try {
			table = connection.getTable(TableName.valueOf(tableName));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("get hbaseTable fail");
			try {
				connection.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				logger.error("获取" + tableName + "失败");
			}
		}
		return table;
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 增加单个存储对象
	 * @param inspectInfo
	 * @return
	 */
	@Override
	public int insert(InspectInfo info) {
		if (info == null || info.getId() == null || info.getId().equals("")) {
			logger.info("RowKey is null,insert fail!");
			return 0;
		}
		Table table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			Put p = new Put(Bytes.toBytes(info.getId()));
			if(info.getMeta()!=null){
				addPut(info.getMeta(), p, "meta", "");
			}
			if(info.getAlgorithmResult()!=null){
				addPut(info.getAlgorithmResult(), p, "algorithmResult", "");
			}
			table.put(p);
			logger.info("文件信息写入HBase成功！");
			rt = 1;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文件信息写入HBase失败！");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return rt;
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 批量增加存储对象
	 * @param list
	 */
	@Override
	public int insert(List<InspectInfo> list) {
		if (list.size() == 0) {
			logger.info("Task list is none");
			return 0;
		}
		List<List<InspectInfo>> taskList = splitList(list);

		Table[] table = new Table[taskList.size()];
		Thread[] threads = new Thread[taskList.size()];
		for (int i = 0; i < taskList.size(); i++) {
			table[i] = getTable();
			if (table[i] == null) {
				logger.error("获取htable实例失败");
				return 0;
			}
			threads[i] = new InsertNewThread(table[i], taskList.get(i));
			threads[i].start();
			// threads[i] = new flushCommitThread(table[i]);
		}

		for (int i = 0; i < taskList.size(); i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 1;

	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 读取单个存储对象
	 * @param table
	 * @param id
	 * @return
	 */
	@Override
	public InspectInfo read(String id) {
		Table table = getTable();
		if (table == null) {
			logger.error("获取htable实例失败");
			return null;
		}
		InspectInfo resultInfo = null;
		try {
			Get get = new Get(id.getBytes());
			boolean flag = table.exists(get);
			if(flag==true){
				Result rs;
				rs = table.get(get);
				resultInfo = beanSet(rs);
				System.out.println(new String(rs.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读取单个存储对象失败");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return resultInfo;
	}
	
	public InspectInfo read(String id,byte[] family) {
		//Object obj=null;
		Table table = getTable();
		if (table == null) {
			logger.error("获取htable实例失败");
			return null;
		}
		InspectInfo resultInfo = null;
		try {
			Get get = new Get(id.getBytes());
			get.addFamily(family);
			boolean flag = table.exists(get);
			if(flag==true){
				Result rs;
				rs = table.get(get);
				resultInfo = beanSet(rs);
				//obj = PropertyUtils.getProperty(resultInfo,family);
				System.out.println(new String(rs.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读取单个存储对象失败");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return resultInfo;
	}

	@Override
	public int isExists(String id) {
		Table table = getTable();
		if (table == null) {
			logger.error("获取htable实例失败");
			return -1;
		}
		int flag=0;
		try {
			Get get = new Get(id.getBytes());
			boolean isExists = table.exists(get);
			if(isExists==false){
				flag=1;
				logger.info("流水号未找到" + id);
				return flag;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询单个存储对象id失败");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		logger.info("流水号找到" + id);
		return flag;
	}

	@Override
	public List<InspectInfo> read(Object obj) {
		return read(obj, null);
	}

	public void newInstanceByName(Object obj, String fieldPath, String field)
			throws Exception {
		String packageName = obj.getClass().getPackage().getName();
		Constructor<?> con = Class.forName(parseClassName(packageName, field))
				.getConstructor();
		BeanUtils.setProperty(obj, fieldPath, con.newInstance());

	}

	public void newListInstanceByName(Object obj, String fieldPath,
			String field, int number) throws Exception {
		List<Object> list = new ArrayList<Object>();
		String packageName = obj.getClass().getPackage().getName();
		for (int i = 0; i < number; i++) {
			Object o = ConstructorUtils.invokeConstructor(
					Class.forName(parseClassName(packageName, field)), null);
			list.add(o);
		}
		BeanUtils.setProperty(obj, fieldPath, list);

	}

	public String parseClassName(String packageName, String name) {
		if (null == name || "".equals(name)) {
			return null;
		}
		String setName = packageName + "." + name.substring(0, 1).toUpperCase()
				+ name.substring(1);
		return setName;
	}

	private InspectInfo beanSet(Result rs) {
		InspectInfo info = new InspectInfo();
		String family;
		String qualifier;
		String value;
		try {
			info.setId(Bytes.toString(rs.getRow()));
			for (Cell cell : rs.listCells()) {
				family = Bytes.toString(cell.getFamilyArray(),
						cell.getFamilyOffset(), cell.getFamilyLength());
				qualifier = Bytes.toString(cell.getQualifierArray(),
						cell.getQualifierOffset(), cell.getQualifierLength());
				String property = BeanUtils.getProperty(info, family);
				if (property == null) {
					newInstanceByName(info, family, family);
				}
				if(qualifier.equals("icon")||qualifier.equals("jpg")||qualifier.equals("sug")||qualifier.equals("waste")){
					BeanUtils.setProperty(info, family + "." + qualifier,CellUtil.cloneValue(cell));
					continue;
				}
				value = Bytes.toString(cell.getValueArray(),
						cell.getValueOffset(), cell.getValueLength());
//				System.out.println("column=" + family + ":" + qualifier
//						+ "  value=" + value);
				

				String[] strs = qualifier.split("\\.");
				String beanPath = "";
				for (int i = 0; i < strs.length - 1; i++) {
					if (strs[i].endsWith("]")) {
						String field = strs[i].substring(0,
								strs[i].indexOf("["));
						property = BeanUtils.getProperty(info, family
								+ beanPath + "." + field);
						if (property == null) {
							String numStr = Bytes.toString(rs.getValue(
									Bytes.toBytes(family),
									Bytes.toBytes(beanPath.substring(1) + "."
											+ field + "Num")));
							int num;
							if (null == numStr || numStr.equals("")) {
								num = 5;
							} else {
								num = Integer.parseInt(numStr);
							}

							newListInstanceByName(info, family + beanPath + "."
									+ field, field, num);
						}
						beanPath += "." + strs[i];
					} else {
						beanPath += "." + strs[i];
						property = BeanUtils.getProperty(info, family
								+ beanPath);
						if (property == null) {
							newInstanceByName(info, family + beanPath, strs[i]);
						}
					}
				}
				BeanUtils.setProperty(info, family + "." + qualifier, value);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return info;
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 批量读取存储对象
	 * @param inspectInfo
	 * @return
	 */
	@Override
	public PageList<InspectInfo> read(Object obj , PageQuery pageQuery) {
		return read(obj ,null ,pageQuery);
	}

	@Override
	public PageList<InspectInfo> read(Object obj ,String filterquery, PageQuery pageQuery) {
		Table table = getTable();
		if (table == null) {
			logger.error("获取htable实例失败");
			return null;
		}
		Get get = null;
		PageList<InspectInfo> page = new PageList<InspectInfo>();
		page.setPage(pageQuery.getPage());
		page.setPageSize(pageQuery.getPageSize());
		try {
			List<Get> list = new ArrayList<Get>();
			SolrNewIndex index = new SolrNewIndex();
			SolrDocumentList docs = index.query(obj,filterquery, pageQuery,tableName);
			page.setTotalItems((int) docs.getNumFound());
			for (SolrDocument doc : docs) {
				get = new Get(Bytes.toBytes((String) doc.getFieldValue("id")));
				get.addFamily(Constants.CF_META);
				list.add(get);
				for (Entry<String, Object> entry : doc.entrySet()) {
					System.out.print(entry.getKey() + ":");
					System.out.println(entry.getValue());
				}
			}
			Result[] res = table.get(list);
			for (Result rs : res) {
				InspectInfo infoTemp = beanSet(rs);
				if(infoTemp==null){
					System.out.println(new String(rs.getRow()));
					continue;
				}
				//System.out.println(new String(rs.toString()));
				page.add(infoTemp);
				
			}

		} catch (IOException e) {
			e.printStackTrace();
			logger.error("批量读取失败");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return page;
	}

	@Override
	public int update(InspectInfo info) {
		return insert(info);
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 删除单个存储对象
	 * @param id
	 */
	@Override
	public int delete(String id) {
		Table table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			Delete delete = new Delete(id.getBytes());
			table.delete(delete);
			SolrNewIndex index = new SolrNewIndex();
			index.deleteIndex(id,tableName);
			logger.info(id + ":删除行成功!");
			rt = 1;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(id + ":删除行失败!");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return rt;

	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 批量删除存储对象
	 * @param inspectInfo
	 */
	@Override
	public int delete(Object obj) {
		Table table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			List<Delete> list = new ArrayList<Delete>();
			List<String> ids = new ArrayList<String>();
			SolrNewIndex index = new SolrNewIndex();
			SolrDocumentList docs = index.query(obj, null,tableName);
			Delete delete;
			for (SolrDocument doc : docs) {
				delete = new Delete(Bytes.toBytes((String) doc
						.getFieldValue("id")));
				list.add(delete);
				ids.add((String) doc.getFieldValue("id"));
			}
			table.delete(list);
			index.deleteIndex(ids,tableName);
			logger.info("批量删除行成功!");
			rt = 1;
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("批量删除行失败!");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return rt;

	}

	public int deleteAll() {
		Table table = getTable();
		int rt = 0;
		if (table == null) {
			logger.error("获取htable实例失败");
			return 0;
		}
		try {
			List<Delete> list = new ArrayList<Delete>();
			SolrNewIndex index = new SolrNewIndex();
			SolrDocumentList docs = index.query(null, null,tableName);
			Delete delete;
			for (SolrDocument doc : docs) {
				delete = new Delete(Bytes.toBytes((String) doc
						.getFieldValue("id")));
				list.add(delete);
			}
			table.delete(list);
			index.deleteAllIndex(tableName);
			logger.info("批量删除行成功!");
			rt = 1;
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("批量删除行失败!");
		}
		try {
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("关闭hbase表失败");
		}
		return rt;
	}

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 切分存储任务
	 * @param list
	 * @return
	 */
	public ArrayList<Map<Object, Map<String, Object>>> splitTask(
			List<InspectInfo> list) {
		ArrayList<Map<Object, Map<String, Object>>> processList = new ArrayList<Map<Object, Map<String, Object>>>();
		int i = 0;
		int avg = list.size() / constants.getHBASE_READ_PROCESS();
		int addIndex = list.size() - avg * constants.getHBASE_READ_PROCESS();
		Map<Object, Map<String, Object>> rowKeyMap = new HashMap<Object, Map<String, Object>>();
		processList.add(rowKeyMap);
		for (InspectInfo info : list) {
			if ((processList.size() <= addIndex && i == avg + 1)
					|| (processList.size() > addIndex && i == avg)) {
				rowKeyMap = new HashMap<Object, Map<String, Object>>();
				processList.add(rowKeyMap);
				i = 0;
			}
			Object rowKey = null;
			Map<String, Object> cfMap = new HashMap<String, Object>();
			try {
				Field[] fields = info.getClass().getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					Object property = field.get(info);
					// System.out.print(field.getName() + ":");
					// System.out.println(property);
					if (property != null) {
						if (field.getName().equals("id")) {
							rowKey = property;
						} else {
							cfMap.put(field.getName(), property);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (rowKey != null) {
				rowKeyMap.put(rowKey, cfMap);
			} else {
				logger.info("inspectInfo id is null");
			}
			i++;
		}
		return processList;
	}

	public List<List<InspectInfo>> splitList(List<InspectInfo> list) {
		ArrayList<List<InspectInfo>> processList = new ArrayList<List<InspectInfo>>();
		List<InspectInfo> subList = new ArrayList<InspectInfo>();
		int i = 0;
		System.out.println(constants.getHBASE_READ_PROCESS());
		int avg = list.size() / constants.getHBASE_READ_PROCESS();
		int addIndex = list.size() - avg * constants.getHBASE_READ_PROCESS();
		processList.add(subList);
		for (InspectInfo resultInfo : list) {
			if ((processList.size() <= addIndex && i == avg + 1)
					|| (processList.size() > addIndex && i == avg)) {
				subList = new ArrayList<InspectInfo>();
				processList.add(subList);
				i = 0;
			}
			subList.add(resultInfo);
			i++;
		}
		return processList;
	}

	public void addPut(Object obj, Put put, String family, String prefix)
			throws Exception {
		if (obj != null) {
			for (Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object property = field.get(obj);

				if (property != null) {
					if (field.getType().equals(String.class)) {
						System.out.println(family + " : " + prefix
								+ field.getName() + "=" + property.toString());
						if (field.getName().equals("path")
								&& new File(property.toString()).exists()) {
							put.addColumn(Bytes.toBytes(family), Bytes
									.toBytes(prefix + field.getName()),
									FileUtils.readFileToByteArray(new File(
											property.toString())));
						} else {
							put.addColumn(Bytes.toBytes(family),
									Bytes.toBytes(prefix + field.getName()),
									Bytes.toBytes(property.toString()));
						}
					} else if (field.getType().equals(List.class)) {
						int i = 0;
						for (Object member : (List<?>) property) {
							addPut(member,
									put,
									family,
									prefix + field.getName() + "["
											+ String.valueOf(i) + "].");
							i += 1;
						}
					} else if (field.getType().equals(byte[].class)) {
							put.addColumn(Bytes.toBytes(family),
										Bytes.toBytes(prefix + field.getName()),
										(byte[])property);
					} else {
							addPut(property, put, family, prefix + field.getName()
								+ ".");
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {


		InspectInfo a = new InspectInfo();
		String name="86574FG01201107250063";
		String path="D:/test";
		String iconPath=path + "/"+name+"/"+name+"_icon.jpg";
		String jpgPath=path + "/"+name+"/"+name+".jpg";
	    byte[] byteIcon=null;
			    if(!new File(iconPath).exists()){
			      if(new File(jpgPath).exists()){
			        ImageUtils.resize(iconPath,new File(jpgPath));
			      }
			    }
			    if(new File(iconPath).exists()){
			      byteIcon=FileUtils.readFileToByteArray(new File(iconPath));
			    }
		
		
		a.setId("0001");
		AlgorithmResult b= new AlgorithmResult();
		ICigDetect iCigDetect =new ICigDetect();
		iCigDetect.setAreaNum("4");
		iCigDetect.setResult("1");
		
		b.setICigDetect(iCigDetect);
		b.setJpg(byteIcon);
		Meta meta = new Meta();
		meta.setRiskLevel("red");
		meta.setArticleNo("sda");
		meta.setIcon(byteIcon);
		a.setAlgorithmResult(b);
		a.setMeta(meta);
		System.out.println(new java.text.SimpleDateFormat(
				"yyyyMMdd:HH:mm:ss:SSS").format(new Date()));
		StorageHBaseNewImpl storageHBaseImpl = new StorageHBaseNewImpl("result");
		// storageHBaseImpl.isExists("86574MB02201402180051");
		storageHBaseImpl.connect();
		PageQuery query = new PageQuery();
		query.setPage(1);
		query.setPageSize(30);
		InspectInfo ss = storageHBaseImpl.read("86574MB01201306202053");
		FileUtils.writeByteArrayToFile(new File("D:/test/86574MB01201303202052_icon.jpg"), ss.getMeta().getIcon());
		System.out.println(ss);
		//storageHBaseImpl.insert(a);
		// storageHBaseImpl.insert(list);
		//Meta meta = new Meta();
		//meta.setRiskLevel("red");
//		 PageList<InspectInfo> resultInfo = (PageList<InspectInfo>)
//		 storageHBaseImpl
//		 .read(null, query);
//		 for(InspectInfo ab:resultInfo){
//			 System.out.println(ab.getId());
//			 //FileUtils.writeByteArrayToFile(new File("D:/86574MB01201401080026/"+ab.getId()+"_icon.jpg"), ab.getAlgorithmResult().getIcon());
//		 }
		 //storageHBaseImpl.insert("0003",byteIcon);
		 //FileUtils.writeByteArrayToFile(new File("D:/86574MB01201401080026/aaa"+"_icon.jpg"), byteIcon);
		// storageHBaseImpl.insert(a);
//		 InspectInfo info =
//		 (InspectInfo)storageHBaseImpl.read("86574MB02201311010005");
		//storageHBaseImpl.delete(meta);
		storageHBaseImpl.close();
//		 List<InspectInfo> list1 = new ArrayList<InspectInfo>();
//		 for(int i=0;i<10;i++){
//		 InspectInfo c = (InspectInfo)BeanUtils.cloneBean(info);
//		 c.setId(info.getId().substring(0,info.getId().length()-1)+i);
//		 list1.add(c);
//		 }
//		 Storage<InspectInfo> test = new StorageHBaseNewImpl("result");
//		 test.connect();
//		 test.insert(list1);
//		 test.close();
		// storageHBaseImpl.update(inspectInfo2);
		// storageHBaseImpl.delete("testaaa");
		// storageHBaseImpl.deleteAll();
		System.out.println(new java.text.SimpleDateFormat(
				"yyyyMMdd:HH:mm:ss:SSS").format(new Date()));
		System.out.println("ok");
	}

}
