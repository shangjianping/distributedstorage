package com.nuctech.solr;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.utils.Constants;

public class test{

	private static final Logger LOG = LoggerFactory

	.getLogger(test.class);
	private static Constants constants = Constants.getInstance();

	private static final SolrServer solrServer = new ConcurrentUpdateSolrServer(
			constants.getSOLR_HTTP_URL(), constants.getSOLR_QUEUE_SIZE(),
			constants.getSOLR_THREAD_COUNT());

	
	public void inputSolr(Put put) {

		try {

			solrServer.add(getInputDoc(put));

		} catch (Exception ex) {

			LOG.error(ex.getMessage());

		}

	}

	@SuppressWarnings("deprecation")
	public static SolrInputDocument getInputDoc(Put put) throws ParseException {

		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("id", Bytes.toString(put.getRow()));

		for (Cell c : put.getFamilyCellMap().get(Constants.CF_META)) {

			String key = Bytes.toString(c.getQualifier());

			String value = Bytes.toString(c.getValue());
			System.out.println(value);
			if (value.isEmpty()) {

				continue;

			}

			doc.addField(key + "_s", value);

		}

		return doc;

	}
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		test a = new test();
		Put put = new Put("test2".getBytes());
		put.add("meta".getBytes(), "a".getBytes(), "你好啊".getBytes("utf-8"));
		a.inputSolr(put);
	}
}
