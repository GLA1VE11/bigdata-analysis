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
@TableName("mars_tianchi_user")
public class MarsUser {
    @TableId(type = IdType.AUTO)
    @TableField("user_id")
    private int userId;
    private String username;
    private String password;
    @TableField(exist = false)
    private String token;
}
