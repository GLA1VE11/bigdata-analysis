package com.example.ddemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ddemo.entity.Recommend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RecommendMapper extends BaseMapper<Recommend> {

    @Select("SELECT * FROM recommend WHERE user_id = #{userId}")
    Recommend findRecommendationByUserId(Integer userId);
}
