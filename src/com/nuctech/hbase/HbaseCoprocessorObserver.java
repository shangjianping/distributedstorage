package com.nuctech.hbase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nuctech.utils.ConnectionCxn;
import com.nuctech.utils.Constants;

public class HbaseCoprocessorObserver extends BaseRegionObserver {

	private static final Logger LOG = LoggerFactory

	.getLogger(HbaseCoprocessorObserver.class);
	private static Constants constants = Constants.getInstance();

	private static final ConcurrentUpdateSolrServer solrServer = new ConcurrentUpdateSolrServer(
			constants.getSOLR_HTTP_URL()+constants.getCORE_INSPECT(), constants.getSOLR_QUEUE_SIZE(),
			constants.getSOLR_THREAD_COUNT());

	private static final Configuration conf = ConnectionCxn.getHbaseConf();
	private static Connection connection = init();
	private Table table;
	private static String tableName = constants.getIMAGE_TABLENAME();

	private static Connection init() {
		try {
			return ConnectionFactory.createConnection(conf);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

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
		synchroTable(put);

	}

	public void synchroTable(Put put) {
		try {
			Put input = new Put(put.getRow());
			for (Cell cell : put.get(Constants.CF_META, "state".getBytes())) {
				input.add(cell);
			}
			
			getTable(tableName);
			table.put(input);
			table.close();
			LOG.info("===insert table completed " + tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getTable(String tableName) {
		try {
			if (table == null) {
				if (connection == null) {
					connection = ConnectionFactory.createConnection(conf);
				}
				table = connection.getTable(TableName.valueOf(tableName));
				LOG.info("===get table connection " + tableName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("get table " + tableName + " fail!");
		}
	}
	/**
	private void createTabel(String tableName) throws IOException{
		if (connection == null) {
			connection = ConnectionFactory.createConnection(conf);
		}
		Admin admin = connection.getAdmin();
		if (!admin.tableExists(TableName.valueOf(tableName))) {
			HTableDescriptor table = new HTableDescriptor(
					TableName.valueOf(tableName));
			table.addFamily(new HColumnDescriptor(Bytes.toBytes("meta"))
					.setCompressionType(Algorithm.SNAPPY));
			table.addFamily(new HColumnDescriptor(Bytes.toBytes("data"))
					.setCompressionType(Algorithm.SNAPPY));

			admin.createTable(table);
			LOG.info("===Creating table " + tableName
					+ " success!");
		}
		admin.close();
	}
	**/

	public void inputSolr(Put put) {

		try {
			LOG.info("===solr insert");
			solrServer.add(getInputDoc(put));
				
		} catch (Exception ex) {

			LOG.error(ex.getMessage());

		}

	}

	public static SolrInputDocument getInputDoc(Put put) {

		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("id", Bytes.toString(put.getRow()));

		for (Cell c : put.getFamilyCellMap().get(Constants.CF_META)) {

			String key = Bytes.toString(c.getQualifierArray(),
					c.getQualifierOffset(), c.getQualifierLength());
			if (key.equals("icon")) {

				continue;

			}

			String value = Bytes.toString(c.getValueArray(),
					c.getValueOffset(), c.getValueLength());

			if (value.isEmpty()) {

				continue;

			}

			doc.addField(key + "_s", value);

		}

		return doc;

	}
}
