package org.echo.seckill.service;

import org.echo.seckill.dto.Exposer;
import org.echo.seckill.domain.Seckill;
import org.echo.seckill.dto.SeckillExecution;
import org.echo.seckill.exception.RepeatKillException;
import org.echo.seckill.exception.SeckillClosedException;
import org.echo.seckill.exception.SeckillException;

import java.util.List;

/**
 * Created by Administrator on 8/6/2017.
 */
public interface SeckillService {

    List<Seckill> getSeckillList(int offset, int limit);

    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀地址，否则输出系统时间
     * @param seckillId
     * @return
     */
    Exposer exposeSeckillUrl(long seckillId);

    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException,
            SeckillClosedException, RepeatKillException;
}
