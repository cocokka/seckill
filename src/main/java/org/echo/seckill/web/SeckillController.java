package org.echo.seckill.web;

import org.echo.seckill.domain.Seckill;
import org.echo.seckill.dto.Exposer;
import org.echo.seckill.dto.SeckillExecution;
import org.echo.seckill.dto.SeckillResult;
import org.echo.seckill.enums.SeckillStateEnum;
import org.echo.seckill.exception.RepeatKillException;
import org.echo.seckill.exception.SeckillClosedException;
import org.echo.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 8/6/2017.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        // jsp + model = modelAndView
        int offset = 0;
        int limit = 10;
        List<Seckill> list = seckillService.getSeckillList(offset, limit);
        model.addAttribute("seckills", list);
        return "list";
    }


    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }


    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult exposer(@PathVariable Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }


    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killerPhone", required = false) Long killerPhone) {
        SeckillResult<SeckillExecution> result;

        if (killerPhone == null) {
            return new SeckillResult<>(false, "未注册");
        }
        SeckillExecution seckillExecution = null;
        try {
            seckillExecution = seckillService.executeSeckill(seckillId, killerPhone, md5);
            return new SeckillResult<>(true, seckillExecution);
        } catch (RepeatKillException e) {
            SeckillStateEnum repeat = SeckillStateEnum.REPEAT;
            seckillExecution = new SeckillExecution(seckillId, repeat.getState(), repeat.getInfo());
            return new SeckillResult<>(true, seckillExecution);
        } catch (SeckillClosedException e) {
            SeckillStateEnum close = SeckillStateEnum.CLOSE_DOWN;
            seckillExecution = new SeckillExecution(seckillId, close.getState(), close.getInfo());
            return new SeckillResult<>(true, seckillExecution);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            SeckillStateEnum error = SeckillStateEnum.ERROE;
            seckillExecution = new SeckillExecution(seckillId, error.getState(), error.getInfo());
            return new SeckillResult<>(true, seckillExecution);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }

}
