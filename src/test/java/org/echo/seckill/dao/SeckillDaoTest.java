package org.echo.seckill.dao;

import org.echo.seckill.domain.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Administrator on 8/5/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void getById() throws Exception {
        long seckillId = 1000L;
        Seckill seckill = seckillDao.getById(seckillId);
        assertEquals(seckillId, seckill.getSeckillId());
        Date startTime = seckill.getStartTime();
        System.out.println(startTime);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(startTime));
    }

    @Test
    public void getAll() throws Exception {
        int offset = 0;
        int limit = 2;
        List<Seckill> list = seckillDao.getAll(offset, limit);
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void reduceinventory() throws Exception {
        long seckillId = 1000;
        int number = seckillDao.getById(seckillId).getNumber();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = "2017-08-01 00:00:00";
        Date date = dateFormat.parse(dateString);
        int count = seckillDao.reduceinventory(seckillId, date);
        assertEquals(1, count);
        Seckill seckill = seckillDao.getById(seckillId);
        assertEquals(number-1, seckill.getNumber());
    }




}