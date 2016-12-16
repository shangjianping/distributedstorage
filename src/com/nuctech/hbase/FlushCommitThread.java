package com.nuctech.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.HTableInterface;
/**
 * 
 * @类功能说明：
 * @作者：shangjianping
 * @创建时间：2015-5-12
 * @版本：V1.0
 */
public class FlushCommitThread extends Thread {
	HTableInterface table;

	public FlushCommitThread(HTableInterface table) {
		this.table = table;
	}

	@Override
	public void run() {
		while (true) {
			try {
				sleep(1000); // 1 second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (table) {
				try {
					table.flushCommits();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
