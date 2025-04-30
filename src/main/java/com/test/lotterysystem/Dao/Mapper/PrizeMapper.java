package com.test.lotterysystem.Dao.Mapper;

import com.test.lotterysystem.Dao.DaoObject.PrizeDO;
import com.test.lotterysystem.Dao.DaoObject.priceDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PrizeMapper {

    @Insert("insert into prize (name,description,price,image_url) " +
            "values (#{prizeName},#{description},#{price},#{imageUrl})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void create(priceDO priceDO);

    @Select("select count(*) from prize")
    Integer getCount();


    @Select("select * from prize order by id desc limit #{offset} , #{pageSize}")
    List<PrizeDO> getPrizeList(Integer offset,Integer pageSize);

    @Select("<script>" +
            " select id from prize" +
            " where id in" +
            " <foreach item='item' collection='prizesIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            " </script>")
    List<Long> selectPrizeByIds(List<Long> prizesIds);

    @Select("<script>" +
            " select * from prize" +
            " where id in" +
            " <foreach item='item' collection='prizeIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            " </script>")
    List<PrizeDO> getPrizeByIds(List<Long> prizeIds);

    @Select("select * from prize where id = #{prizeId}")
    PrizeDO selectById(Long prizeId);
}
