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

public class StandardImageIndex {

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
	public SolrDocumentList query(Object obj , PageQuery pageQuery) {
		return query(obj,null,pageQuery);
	}
	
	public SolrDocumentList query(Object obj ,String filterQuery, PageQuery pageQuery) {
		SolrDocumentList docs = null;
		try {
			SolrServer server = new HttpSolrServer(constants.getSOLR_HTTP_URL());
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
					if (property != null) {
						if (queryStr.equals("")) {
							queryStr = field.getName() + solrIndexSuffix + ":"
									+ property;
						} else {
							queryStr = queryStr + " AND " + field.getName()
									+ solrIndexSuffix + ":" + property;
						}
					}
				}
			}
			if(queryStr.equals("")){
				queryStr = "*:*";
			}
			logger.info(queryStr);
			SolrQuery query = new SolrQuery(queryStr);
			if (pageQuery != null) {
				query.setStart((pageQuery.getPage() - 1)
						* pageQuery.getPageSize()); // 数据起始行，分页用
				query.setRows(pageQuery.getPageSize()); // 返回记录数，分页用
			}
			query.setSort("_version_", SolrQuery.ORDER.desc);
			if(filterQuery!=null&&!filterQuery.equals("")){
				query.addFilterQuery(filterQuery);
			}
			QueryResponse response;
			response = server.query(query);
			docs = response.getResults();
			logger.info(new SimpleDateFormat("hh/mm/ss:SSS")
					.format(new Date()));
			logger.info("文档个数：" + docs.getNumFound()); // 数据总条数也可轻易获取
			logger.info("查询时间：" + response.getQTime());
			logger.info("查询个数：" + docs.size());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("条件查询失败");
		}
		return docs;
	}

	public void deleteIndex(String id) {
		try {
			SolrServer server = new HttpSolrServer(constants.getSOLR_HTTP_URL());
			server.deleteById(id);
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除索引失败");
		}
	}

	public void deleteIndex(List<String> ids) {
		try {
			SolrServer server = new HttpSolrServer(constants.getSOLR_HTTP_URL());
			server.deleteById(ids);
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除索引失败");
		}
	}

	public void deleteAllIndex() {
		try {
			SolrServer server = new HttpSolrServer(constants.getSOLR_HTTP_URL());
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
