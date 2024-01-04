package org.example.topTimeSong;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class MapperClass extends Mapper<LongWritable, Text, Text, IntWritable> {

    private Text song_name = new Text();
    private IntWritable type = new IntWritable();

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String line = value.toString();
        String str[] = line.split(",");

        if (str.length == 3) {
            song_name.set(str[1]);
            try {
                int temp = Integer.parseInt(str[2]);
                if (temp == 1){
                    type.set(temp);
                }
                else{
                    type.set(0);
                }
                context.write(song_name, type);
            }catch(Exception e){
                e.printStackTrace();
            }

            //views.set(temp);
        }

    }
}

