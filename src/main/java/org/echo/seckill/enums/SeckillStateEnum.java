package org.echo.seckill.enums;

/**
 * Created by Administrator on 8/6/2017.
 */
public enum SeckillStateEnum {
    SUCCESS(1, "秒杀成功"),
    CLOSE_DOWN(0, "秒杀结束"),
    INVALIDATION(-1, "秒杀无效"),
    REPEAT(-2, "秒杀重复"),
    ERROE(-3, "秒杀异常"),
    REWRITE(-4, "数据篡改");

    private int state;
    private String info;

    SeckillStateEnum(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }

    public String getInfo() {
        return info;
    }

    public static SeckillStateEnum stateOf(int index) {
        for (SeckillStateEnum stateEnum : values()) {
            if (stateEnum.getState() == index) {
                return stateEnum;
            }
        }
        return null;
    }
}
