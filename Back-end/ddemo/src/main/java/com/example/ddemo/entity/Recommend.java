package com.example.ddemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("recommend")
public class Recommend {
    @TableId(type = IdType.INPUT)
    @TableField("user_id")
    private int userId;
    private Integer recommendedSong1;
    private Integer recommendedSong2;
    private Integer recommendedSong3;
    private Integer recommendedSong4;
    private Integer recommendedSong5;
    private Integer recommendedSong6;
    private Integer recommendedSong7;
    private Integer recommendedSong8;
    private Integer recommendedSong9;
    private Integer recommendedSong10;
}
