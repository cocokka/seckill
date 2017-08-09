package org.echo.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.echo.seckill.domain.SuccessKilled;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 8/3/2017.
 */
@Repository("successKilledDao")
public interface SuccessKilledDao {

    int saveSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    SuccessKilled getById(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

}
