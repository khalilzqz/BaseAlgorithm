package RandomForest;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import com.rs.rsplat.util.Utilities;

public class MapReduceRF {
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

		Configuration conf = new Configuration();

		String outPath = "/user/zqz/NBAProj/testout/";
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, "NBAMapReduce");
		job.setJarByClass(MapReduceRF.class);
		job.setMapOutputKeyClass(OutputKey.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setReducerClass(NBAReducer.class);
		job.setNumReduceTasks(1);
		job.setGroupingComparatorClass(OutputKeyGroupingComparator.class);
		job.setInputFormatClass(TextInputFormat.class);
		FileSystem fs = FileSystem.get(conf);

		String[] cachePath = { "/user/zqz/NBAProj/NBAid.txt", };

		for (int i = 0; i < 1; i++) {
			job.addCacheFile(new Path(cachePath[i]).toUri());
		}
		String inpath = "/user/zqz/NBAProj/test/";
		if (fs.exists(new Path(inpath))) {
			MultipleInputs.addInputPath(job, new Path(inpath), TextInputFormat.class, NBAMapper.class);
		}

		fs.delete(new Path(outPath), true);
		FileOutputFormat.setOutputPath(job, new Path(outPath));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	public static class NBAMapper extends Mapper<LongWritable, Text, OutputKey, Text> {
		HashSet<String> hostList = new HashSet<>();
		HashMap<String, String> reList = new HashMap<>();
		List<String> pathList = new ArrayList<String>();
		String TestName = "";
		String TestId = "";
		int count = 0;

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {

			pathList.add("NBAid");

			// 获取分布式缓存
			if (context.getCacheFiles() != null && context.getCacheFiles().length > 0) {
				for (String path : pathList) {
					File Info = new File("./" + path + ".txt");
					BufferedReader reader = null;
					try {
						reader = new BufferedReader(new FileReader(Info));
						String line = null;
						while ((line = reader.readLine()) != null) {
							String[] temp = line.trim().split("\\s+");
							if (temp.length == 2) {
								TestName = temp[0];
								TestId = temp[1];
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (reader != null) {
							try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		/**
		 * 进行分段，
		 */
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			OutputKey key1 = new OutputKey();
			Text value1 = new Text();
			if (count == 9) {
				key1.setKey("1");
				key1.setOrder(0l);
				value1.set("test|" + value.toString().trim());
				context.write(key1, value1);
				count = 0;
			} else {
				key1.setKey("1");
				key1.setOrder(0l);
				value1.set("train|" + value.toString().trim());
				context.write(key1, value1);
				count++;
			}
		}
	}

	public static class NBAReducer extends Reducer<OutputKey, Text, Text, Text> {

		private MultipleOutputs<Text, Text> mos;
		Text key3 = new Text();
		Text value3 = new Text();
		ArrayList<ArrayList<String>> Test = new ArrayList<>();
		ArrayList<ArrayList<String>> Train = new ArrayList<>();
		DescribeTreesCategMR DT = new DescribeTreesCategMR();

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			mos = new MultipleOutputs<Text, Text>(context);
		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			/*
			 * For class-labels
			 */
			HashMap<String, Integer> Classes = new HashMap<String, Integer>();
			ArrayList<String> outputs = new ArrayList<>();
			for (ArrayList<String> dp : Train) {
				String clas = dp.get(dp.size() - 1);
				if (Classes.containsKey(clas))
					Classes.put(clas, Classes.get(clas) + 1);
				else
					Classes.put(clas, 1);
			}

			// M是特征数，Ms是选择特征数，C是分类数量
			int numTrees = 15;
			int M = Train.get(0).size() - 1;
			int Ms = (int) Math.round(Math.log(M) / Math.log(2) + 1);
			int C = Classes.size();
			RandomForestCateg RFC = new RandomForestCateg(numTrees, M, Ms, C, Train, Test);
			outputs = RFC.StartMR();
			key3.set("report");
			value3.set(Test.size() + "|" + Train.size());
			mos.write(key3, value3, "report");
			for (String output : outputs) {
				key3.set("report");
				value3.set(output);
				mos.write(key3, value3, "report");
			}
			mos.close();
		}

		@Override
		public void reduce(OutputKey key, Iterable<Text> value, Context context)
				throws IOException, InterruptedException {
			String strKey = key.getKey();
			StringBuilder sb1 = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();

			for (Text val : value) {
				String str = val.toString();
				if (str.startsWith("test")) {
					String ReduceOut = str.substring(str.indexOf("|") + 1);
					Test.add(DT.CreateInputCateg(ReduceOut));
					key3.set("testSet");
					value3.set(ReduceOut);
					mos.write(key3, value3, "testSet");
				} else if (str.startsWith("train")) {
					String ReduceOut = str.substring(str.indexOf("|") + 1);
					Train.add(DT.CreateInputCateg(ReduceOut));
					key3.set("trainSet");
					value3.set(ReduceOut);
					mos.write(key3, value3, "trainSet");
				}
			}
		}
	}

	/**
	 * 二次排序用的
	 * 
	 * @author ron
	 * 
	 */
	private static class OutputKey implements WritableComparable<OutputKey> {

		private String key = "";
		private Long order = 0l;

		public OutputKey() {
		}

		@SuppressWarnings("unused")
		public OutputKey(String key) {
			this.key = key;
			this.order = 0l;
		}

		@SuppressWarnings("unused")
		public OutputKey(String key, Long order) {
			this.key = key;
			this.order = order;
		}

		public String getKey() {
			return this.key;
		}

		public long getOrder() {
			return this.order;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public void setOrder(Long order) {
			this.order = order;
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			this.key = in.readUTF();
			this.order = in.readLong();
		}

		@Override
		public void write(DataOutput out) throws IOException {
			out.writeUTF(key);
			out.writeLong(this.order);
		}

		@Override
		public int compareTo(OutputKey other) {
			int compare = this.key.compareTo(other.key);
			if (compare != 0) {
				return compare;
			} else if (this.order != other.order) {
				return order < other.order ? -1 : 1;
			} else {
				return 0;
			}
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}

		static { // register this comparator
			WritableComparator.define(OutputKey.class, new OutputKeyComparator());
		}
	}

	// key的比较函数
	private static class OutputKeyComparator extends WritableComparator {
		protected OutputKeyComparator() {
			super(OutputKey.class, true);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public int compare(WritableComparable w1, WritableComparable w2) {

			OutputKey p1 = (OutputKey) w1;
			OutputKey p2 = (OutputKey) w2;

			int cmp = p1.getKey().compareTo(p2.getKey());
			if (cmp != 0) {
				return cmp;
			}
			return p1.getOrder() == p2.getOrder() ? 0 : (p1.getOrder() < p2.getOrder() ? -1 : 1);
		}
	}

	// reduce阶段的分组函数
	@SuppressWarnings("unused")
	private static class OutputKeyGroupingComparator extends WritableComparator {

		protected OutputKeyGroupingComparator() {
			super(OutputKey.class, true);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public int compare(WritableComparable o1, WritableComparable o2) {

			OutputKey p1 = (OutputKey) o1;
			OutputKey p2 = (OutputKey) o2;

			return p1.getKey().compareTo(p2.getKey());
		}
	}
}
