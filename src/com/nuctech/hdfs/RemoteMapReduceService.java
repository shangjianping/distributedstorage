package com.nuctech.hdfs;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RemoteMapReduceService {
	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}
	}

	public static class IntSumReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static String startJob() throws Exception {
		Job job = Job.getInstance();
		job.setJobName("wordcount");
		/***************************
		 * ...... 在这里，和普通的MapReduce一样，设置各种需要的东西 ......
		 ***************************/
		//job.setJarByClass(RemoteMapReduceService.class);
		job.setMapperClass(TokenizerMapper.class); // 为job设置Mapper类
		job.setCombinerClass(IntSumReducer.class); // 为job设置Combiner类
		job.setReducerClass(IntSumReducer.class); // 为job设置Reducer类
		job.setOutputKeyClass(Text.class); // 为job的输出数据设置Key类
		job.setOutputValueClass(IntWritable.class); // 为job输出设置value类
		FileInputFormat.addInputPath(job, new Path("hdfs://ambari.node:8020/tmp/aaa")); // 为job设置输入路径
		FileOutputFormat.setOutputPath(job, new Path("/tmp/count"));// 为job设置输出路径

		// 下面为了远程提交添加设置：
		Configuration conf = job.getConfiguration();
		conf.set("mapreduce.framework.name", "yarn");
		conf.set("hbase.zookeeper.quorum", "ambari.node:2181");
		conf.set("fs.default.name", "hdfs://ambari.node:8020");
		conf.set("yarn.resourcemanager.resource-tracker.address", "ambari.node:8025");
		conf.set("yarn.resourcemanager.address", "ambari.node:8050");
		conf.set("yarn.resourcemanager.scheduler.address", "ambari.node:8030");
		conf.set("yarn.resourcemanager.admin.address", "ambari.node:8141");
		conf.set("yarn.application.classpath", "$HADOOP_CONF_DIR,"
				+ "$HADOOP_COMMON_HOME/*,$HADOOP_COMMON_HOME/lib/*,"
				+ "$HADOOP_HDFS_HOME/*,$HADOOP_HDFS_HOME/lib/*,"
				+ "$HADOOP_MAPRED_HOME/*,$HADOOP_MAPRED_HOME/lib/*,"
				+ "$YARN_HOME/*,$YARN_HOME/lib/*,"
				+ "$HBASE_HOME/*,$HBASE_HOME/lib/*,$HBASE_HOME/conf/*");
		conf.set("mapreduce.jobhistory.address", "ambari.node:10020");
		conf.set("mapreduce.jobhistory.webapp.address", "ambari.node:19888");
		conf.set("mapred.child.java.opts", "-Xmx1024m");

		job.submit();
		// 提交以后，可以拿到JobID。根据这个JobID可以打开网页查看执行进度。
		return job.getJobID().toString();
	}
	
	
	public static void main(String[] args) throws Exception{
		RemoteMapReduceService.startJob();
	}

	
}
