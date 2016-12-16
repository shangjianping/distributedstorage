package com.nuctech.spark;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.ClientProtos;
import org.apache.hadoop.hbase.util.Base64;


public class HbaseRead implements Serializable {

    public Log log = LogFactory.getLog(HbaseRead.class);

    /**
     * 将scan编码，该方法copy自 org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil
     *
     * @param scan
     * @return
     * @throws IOException
     */
    static String convertScanToString(Scan scan) throws IOException {
    	ClientProtos.Scan proto = ProtobufUtil.toScan(scan);    
        return Base64.encodeBytes(proto.toByteArray());
    }
/**
    public void start() {
        //初始化sparkContext，这里必须在jars参数里面放上Hbase的jar，
        // 否则会报unread block data异常
        JavaSparkContext sc = new JavaSparkContext("spark://nowledgedata-n3:7077", "hbaseTest",
                "/home/hadoop/software/spark-0.8.1",
                new String[]{"target/ndspark.jar", "target\\dependency\\hbase-0.94.6.jar"});

        //使用HBaseConfiguration.create()生成Configuration
        // 必须在项目classpath下放上hadoop以及hbase的配置文件。
        Configuration conf = HBaseConfiguration.create();
        //设置查询条件，这里值返回用户的等级
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes("195861-1035177490"));
        scan.setStopRow(Bytes.toBytes("195861-1072173147"));
        scan.addFamily(Bytes.toBytes("info"));
        scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("levelCode"));

        try {
            //需要读取的hbase表名
            String tableName = "usertable";
            conf.set(TableInputFormat.INPUT_TABLE, tableName);
            conf.set(TableInputFormat.SCAN, convertScanToString(scan));

            //获得hbase查询结果Result
            JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD = sc.newAPIHadoopRDD(conf,
                    TableInputFormat.class, ImmutableBytesWritable.class,
                    Result.class);

           

            
            
            //打印出最终结果
           
        } catch (Exception e) {
            log.warn(e);
        }

    }

 
     * spark如果计算没写在main里面,实现的类必须继承Serializable接口，<br>
     * </>否则会报 Task not serializable: java.io.NotSerializableException 异常
     */
    public static void main(String[] args) throws InterruptedException {

        //new HbaseRead().start();

        System.exit(0);
    }
}