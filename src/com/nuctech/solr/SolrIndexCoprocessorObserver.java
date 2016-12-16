package com.nuctech.solr;

import java.io.UnsupportedEncodingException;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.utils.Constants;

public class SolrIndexCoprocessorObserver extends BaseRegionObserver {

	private static final Logger LOG = LoggerFactory

	.getLogger(SolrIndexCoprocessorObserver.class);
	private static Constants constants = Constants.getInstance();

	private static final ConcurrentUpdateSolrServer solrServer = new ConcurrentUpdateSolrServer(
			constants.getSOLR_HTTP_URL()+constants.getCORE_STANDARD(), constants.getSOLR_QUEUE_SIZE(),
			constants.getSOLR_THREAD_COUNT());

	/**
	 * 
	 * 建立solr索引
	 * 
	 * 
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Override
	public void postPut(ObserverContext<RegionCoprocessorEnvironment> e,
			Put put, WALEdit edit, Durability a) {
		inputSolr(put);
	}

	public void inputSolr(Put put) {

		try {

			solrServer.add(getInputDoc(put));

		} catch (Exception ex) {

			LOG.error(ex.getMessage());

		}

	}

	@SuppressWarnings("deprecation")
	public static SolrInputDocument getInputDoc(Put put) {

		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("id", Bytes.toString(put.getRow()));

		for (Cell c : put.getFamilyCellMap().get(Constants.CF_META)) {

			String key = Bytes.toString(c.getQualifier());

			String value = Bytes.toString(c.getValue());

			if (value.isEmpty()) {

				continue;

			}

			doc.addField(key + "_s", value);

		}

		return doc;

	}
}
