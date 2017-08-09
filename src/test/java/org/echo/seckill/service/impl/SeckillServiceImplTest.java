package org.echo.seckill.service.impl;

import org.echo.seckill.domain.Seckill;
import org.echo.seckill.domain.SuccessKilled;
import org.echo.seckill.dto.Exposer;
import org.echo.seckill.dto.SeckillExecution;
import org.echo.seckill.enums.SeckillStateEnum;
import org.echo.seckill.service.SeckillService;
import org.echo.seckill.service.SuccessKilledService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 8/6/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private SuccessKilledService successKilledService;

    @Test
    public void getSeckillList() throws Exception {

    }

    @Test
    public void getById() throws Exception {
        long seckillId = 1000L;
        Seckill seckill = seckillService.getById(seckillId);
        assertNotNull(seckill);
        assertEquals(seckillId, seckill.getSeckillId());
    }

    @Test
    public void exposeSeckillUrl() throws Exception {

        long seckillId = 1000L;
        Seckill origin = seckillService.getById(seckillId);
        Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
        assertNotNull(exposer);
        System.out.println(exposer.getMd5());
        assertEquals("c2d6bd3654bc0c7c1ded30fea54e2488", exposer.getMd5());
        long userPhone = 18811000011L;
        SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, exposer.getMd5());
        assertNotNull(seckillExecution);
        assertEquals(seckillId, seckillExecution.getSeckillId());
        assertEquals(SeckillStateEnum.SUCCESS.getState(), seckillExecution.getState());
        assertEquals(SeckillStateEnum.SUCCESS.getInfo(), seckillExecution.getStateInfo());
        SuccessKilled successKilled = seckillExecution.getSuccessKilled();
        assertNotNull(successKilled);
        Seckill now = seckillService.getById(seckillId);
        assertEquals((origin.getNumber() -1), now.getNumber());

    }

    @Test
    public void executeSeckill() throws Exception {

    }

}