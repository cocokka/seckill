package org.echo.seckill.dao;

import org.echo.seckill.domain.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 8/6/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Test
    public void saveSuccessKilled() throws Exception {
        long seckillId = 1000L;
        long userPhone = 18510038026L;
        int count = successKilledDao.saveSuccessKilled(seckillId, userPhone);
        assertEquals(1, count);
    }

    @Test
    public void getById() throws Exception {
        long seckillId = 1000L;
        long userPhone = 18510038026L;
        SuccessKilled successKilled = successKilledDao.getById(seckillId, userPhone);
        assertNotNull(successKilled);
        assertEquals(seckillId, successKilled.getSeckillId());
        assertEquals(userPhone, successKilled.getUserPhone());
        assertEquals(seckillId, successKilled.getSeckill().getSeckillId());
    }

}