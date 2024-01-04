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
@TableName("user")
public class NewUser {
    /*
    * 在你的 MarsUser 类中，user_id 属性可能无法正确映射到数据库的 user_id 列，因为 Java 类通常遵循驼峰命名法（userId），
    * 而数据库列名通常是下划线分隔的（user_id）。
    * */
    @TableId(type = IdType.AUTO)
    private int id;
    private String username;
    private String password;
    private String birthday;
    @TableField(exist = false)
    private String token;
}
