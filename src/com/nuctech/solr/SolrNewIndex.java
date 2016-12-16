package com.nuctech.solr;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.model.PageQuery;
import com.nuctech.utils.Constants;

/**
 * 
 * @类功能说明：solr索引操作
 * @作者：shangjianping
 * @创建时间：2015-5-13
 * @版本：V1.0
 */
public class SolrNewIndex {
	private static final Logger logger = LoggerFactory
			.getLogger(SolrNewIndex.class);
	private static Constants constants = Constants.getInstance();
	private static String solrIndexSuffix = "_s";

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-13
	 * @功能描述： 条件查询文件id
	 * @param meta
	 * @return
	 */
	public SolrDocumentList query(Object obj , PageQuery pageQuery,String tableName) {
		return query(obj,null,pageQuery,tableName);
	}
	
	public SolrDocumentList query(Object obj ,String filterQuery, PageQuery pageQuery,String tableName) {
		SolrDocumentList docs = null;
		String solrHttp="";
		try {
			if(tableName.equals("result")){
				solrHttp=constants.getSOLR_HTTP_URL()+constants.getCORE_INSPECT();
			}else if(tableName.equals("standard")){
				solrHttp=constants.getSOLR_HTTP_URL()+constants.getCORE_STANDARD();
			}else{
				return null;
			}
			SolrServer server = new HttpSolrServer(solrHttp);
			String queryStr = "";
			if (obj == null) {
				queryStr = "*:*";
			} else {
				Field[] fields = obj.getClass().getDeclaredFields();
				for (Field field : fields) {
					Object property;
					field.setAccessible(true);
					property = field.get(obj);
					// System.out.print(field.getName() + ":");
					// System.out.println(property);
					if (property != null&&!property.toString().trim().equals("")) {
						if (queryStr.equals("")) {
							queryStr = field.getName() + solrIndexSuffix + ":"
									+ property.toString().replaceAll(" ", "\\\\ ")+"*";
						} else {
							queryStr = queryStr + " AND " + field.getName()
									+ solrIndexSuffix + ":" + property.toString().replaceAll(" ", "\\\\ ")+"*";
						}
					}
				}
			}
			if(queryStr.equals("")){
				queryStr = "*:*";
			}
			
//			if(queryStr.contains(" ")){
//				queryStr = queryStr.replaceAll(" ", "\\\\ ");
//				logger.info(queryStr);
//			}
			logger.info(queryStr);
			SolrQuery query = new SolrQuery(queryStr);
			if (pageQuery != null) {
				query.setStart((pageQuery.getPage() - 1)
						* pageQuery.getPageSize()); // 数据起始行，分页用
				query.setRows(pageQuery.getPageSize()); // 返回记录数，分页用
			}
			query.setSort("uploadTimestamp_s", SolrQuery.ORDER.desc);
			if(filterQuery!=null&&!filterQuery.equals("")){
				query.addFilterQuery(filterQuery);
			}
			query.setParam("fl", "id");
			QueryResponse response;
			response = server.query(query);
			docs = response.getResults();
			logger.info(new SimpleDateFormat("hh/mm/ss:SSS")
					.format(new Date()));
			logger.info("文档个数：" + docs.getNumFound()); // 数据总条数也可轻易获取
			logger.info("查询时间：" + response.getQTime());
			logger.info("查询个数：" + docs.size());
			System.out.println("文档个数：" + docs.getNumFound());
			System.out.println("查询个数：" + docs.size());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("条件查询失败");
		}
		return docs;
	}

	public void deleteIndex(String id,String tableName) {
		try {
			String solrHttp="";
			if(tableName.equals("result")){
				solrHttp=constants.getSOLR_HTTP_URL()+constants.getCORE_INSPECT();
			}else if(tableName.equals("standard")){
				solrHttp=constants.getSOLR_HTTP_URL()+constants.getCORE_STANDARD();
			}else{
				return ;
			}
			SolrServer server = new HttpSolrServer(solrHttp);
			server.deleteById(id);
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除索引失败");
		}
	}

	public void deleteIndex(List<String> ids,String tableName) {
		try {
			String solrHttp="";
			if(tableName.equals("result")){
				solrHttp=constants.getSOLR_HTTP_URL()+constants.getCORE_INSPECT();
			}else if(tableName.equals("standard")){
				solrHttp=constants.getSOLR_HTTP_URL()+constants.getCORE_STANDARD();
			}else{
				return ;
			}
			SolrServer server = new HttpSolrServer(solrHttp);
			server.deleteById(ids);
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除索引失败");
		}
	}

	public void deleteAllIndex(String tableName) {
		try {
			String solrHttp="";
			if(tableName.equals("result")){
				solrHttp=constants.getSOLR_HTTP_URL()+constants.getCORE_INSPECT();
			}else if(tableName.equals("standard")){
				solrHttp=constants.getSOLR_HTTP_URL()+constants.getCORE_STANDARD();
			}else{
				return ;
			}
			SolrServer server = new HttpSolrServer(solrHttp);
			server.deleteByQuery("*:*");
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除失败");
		}
	}
	
	public static void main(String[] arg){
		SolrNewIndex index = new SolrNewIndex();
		PageQuery pageQuery= new PageQuery();
		pageQuery.setPage(1);
		pageQuery.setPageSize(30);
		SolrDocumentList docs = index.query(null,null, pageQuery,"result");
		for (SolrDocument doc : docs) {
			System.out.println(doc.getFieldValue("id"));
		}
	}

}
