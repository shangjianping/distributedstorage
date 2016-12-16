package com.nuctech.flume;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.sink.hbase.HbaseEventSerializer;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;

import com.google.common.base.Charsets;

/**
 * A simple serializer that returns puts from an event, by writing the event
 * body into it. The headers are discarded. It also updates a row in hbase which
 * acts as an event counter.
 * 
 * Takes optional parameters:
 * <p>
 * <tt>rowPrefix:</tt> The prefix to be used. Default: <i>default</i>
 * <p>
 * <tt>incrementRow</tt> The row to increment. Default: <i>incRow</i>
 * <p>
 * <tt>suffix:</tt> <i>uuid/random/timestamp.</i>Default: <i>uuid</i>
 * <p>
 * 
 * Mandatory parameters:
 * <p>
 * <tt>cf:</tt>Column family.
 * <p>
 * Components that have no defaults and will not be used if null:
 * <tt>payloadColumn:</tt> Which column to put payload in. If it is null, event
 * data will not be written.
 * <p>
 * <tt>incColumn:</tt> Which column to increment. Null means no column is
 * incremented.
 */
public class CustomSerializer implements HbaseEventSerializer {
	private byte[] cf;
	private byte[] plCol;
	private byte[] payload;
	private Map<String,String> map;

	public CustomSerializer() {

	}

	@Override
	public void configure(Context context) {

		String payloadColumn = context.getString("payloadColumn", "zip");
		
		if (payloadColumn != null && !payloadColumn.isEmpty()) {
			plCol = payloadColumn.getBytes(Charsets.UTF_8);
		}
	}

	@Override
	public void configure(ComponentConfiguration conf) {
	}

	@Override
	public void initialize(Event event, byte[] cf) {
		this.payload = event.getBody();
		this.map = event.getHeaders();
		this.cf = cf;
	}

	@Override
  public List<Row> getActions() throws FlumeException {
    List<Row> actions = new LinkedList<Row>();
    System.out.println("================map:"+map.toString());
    if(plCol != null){
      if(map.containsKey("basename")){
    	  String baseName = map.get("basename");
    	  String id = baseName.substring(0, baseName.contains(".")?baseName.indexOf("."):baseName.length());
	      byte[] rowKey =id.getBytes();
	      try {
	        Put put = new Put(rowKey);
	        put.add(cf, plCol, payload);
	        actions.add(put);
	      } catch (Exception e){
	        throw new FlumeException("Could not get row key!", e);
	      }
	
	    }
    }
    return actions;
  }

	

	@Override
	public void close() {
	}

	public enum KeyType {
		UUID, RANDOM, TS, TSNANO;
	}

	@Override
	public List<Increment> getIncrements() {
		return null;
	}

}