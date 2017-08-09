package org.echo.seckill.service.impl;

import org.echo.seckill.dao.SuccessKilledDao;
import org.echo.seckill.domain.SuccessKilled;
import org.echo.seckill.exception.SeckillException;
import org.echo.seckill.service.SuccessKilledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 8/6/2017.
 */
@Service("successKilledService")
public class SuccessKilledServiceImpl implements SuccessKilledService {

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Override
    public void save(long seckillId, long userPhone) {
        int saveCount = successKilledDao.saveSuccessKilled(seckillId, userPhone);
        if (saveCount <= 0) {
            throw new SeckillException("保存秒杀明细失败！");
        }
    }

    @Override
    public SuccessKilled get(long seckillId, long userPhone) {
        return successKilledDao.getById(seckillId, userPhone);
    }
}
