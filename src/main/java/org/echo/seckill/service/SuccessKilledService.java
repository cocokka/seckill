package org.echo.seckill.service;

import org.echo.seckill.domain.SuccessKilled;

/**
 * Created by Administrator on 8/6/2017.
 */
public interface SuccessKilledService {

    void save(long seckillId, long userPhone);
    SuccessKilled get(long seckillId, long userPhone);
}
