package com.example.ddemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("mars_tianchi_songs")
public class Song {
    @TableId(type = IdType.AUTO)
    @TableField("song_id")
    private String songId;
    @TableField("artist_id")
    private String artistId;
    @TableField("song_init_plays")
    private int songInitPlays;
}
