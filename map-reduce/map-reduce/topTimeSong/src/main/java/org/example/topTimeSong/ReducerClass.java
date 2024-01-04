package org.example.topTimeSong;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.example.topTimeSong.MySQLOperate;

public class ReducerClass extends Reducer<Text, IntWritable, Text, IntWritable> {
    static float max = 0;
    static int sum = 0;
    static String finalKey = "";
    Map<String, Integer> map = new HashMap<String, Integer>();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        map.put(key.toString(), sum);
    }

    @Override
    protected void cleanup(Reducer<Text, IntWritable, Text, IntWritable>.Context context)
            throws IOException, InterruptedException {

        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        for (int i = 0; i < 10; i++){
            context.write(new Text(list.get(i).getKey()), new IntWritable(list.get(i).getValue()));
            new MySQLOperate().insert(list.get(i).getKey(), list.get(i).getValue());
        }
    }

}
