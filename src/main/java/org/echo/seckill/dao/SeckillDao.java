package org.echo.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.echo.seckill.domain.Seckill;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 8/3/2017.
 */
@Repository("seckillDao")
public interface SeckillDao {

    Seckill getById(long seckillId);

    List<Seckill> getAll(@Param("offset") int offset, @Param("limit") int limit);

    int reduceinventory(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);



}
