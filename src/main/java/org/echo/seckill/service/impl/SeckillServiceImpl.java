package org.echo.seckill.service.impl;

import com.google.common.base.Strings;
import org.echo.seckill.dao.SeckillDao;
import org.echo.seckill.dao.SuccessKilledDao;
import org.echo.seckill.domain.Seckill;
import org.echo.seckill.domain.SuccessKilled;
import org.echo.seckill.dto.Exposer;
import org.echo.seckill.dto.SeckillExecution;
import org.echo.seckill.enums.SeckillStateEnum;
import org.echo.seckill.exception.RepeatKillException;
import org.echo.seckill.exception.SeckillClosedException;
import org.echo.seckill.exception.SeckillException;
import org.echo.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 8/6/2017.
 */
@Service("seckillService")
public class SeckillServiceImpl implements SeckillService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String slatValue = "sfs%(><jfowj@3249u^*^((IKLHF^%%";

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Override
    public List<Seckill> getSeckillList(int offset, int limit) {
        return seckillDao.getAll(offset, limit);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.getById(seckillId);
    }

    @Override
    public Exposer exposeSeckillUrl(long seckillId) {

        Seckill seckill = seckillDao.getById(seckillId);
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date now = new Date();
        if (now.before(startTime) || now.after(endTime)) {
            return new Exposer(false, seckillId, now.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMd5(long seckillId) {
        String base = seckillId + slatValue;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, SeckillClosedException, RepeatKillException {

        if (Strings.isNullOrEmpty(md5)) {
            throw new SeckillException("seckill data rewrite.");
        }

        try {

            // 执行秒杀行为，减少库存，添加秒杀明细
            Date now = new Date();
            int updateCount = seckillDao.reduceinventory(seckillId, now);
            if (updateCount <= 0) {
                // 没有更新到记录，秒杀结束
                throw new SeckillClosedException("seckill is closed.");
            } else {
                //记录购买行为
                int saveCount = successKilledDao.saveSuccessKilled(seckillId, userPhone);
                if (saveCount < 0) {
                    throw new SeckillException("system inner error.");
                } else if (saveCount == 0) {
                    throw new RepeatKillException("seckill is repeated.");
                } else {
                    SuccessKilled successKilled = successKilledDao.getById(seckillId, userPhone);
                    SeckillStateEnum success = SeckillStateEnum.SUCCESS;
                    return new SeckillExecution(seckillId, success.getState(), success.getInfo(), successKilled);
                }
            }
        } catch (SeckillClosedException | RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error. " + e.getMessage());
        }
    }
}
