SELECT * FROM DUAL;
CREATE DATABASE seckill IF NOT EXISTS;
USE SECKILL;

DROP TABLE IF EXISTS seckill;
CREATE TABLE seckill (
  seckill_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  NAME VARCHAR(120) NOT NULL COMMENT '商品名称',
  number INT NOT NULL COMMENT '商品数量',
  start_time TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
  end_time TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

INSERT INTO
  seckill (NAME, number, start_time, end_time)
VALUES
  ('1000秒杀iPhone7', 100, '2017-08-01 00:00:00', '2017-08-02 00:00:00'),
  ('800秒杀ipad2', 200, '2017-08-01 00:00:00', '2017-08-02 00:00:00'),
  ('50秒杀小米5', 300, '2017-08-01 00:00:00', '2017-08-02 00:00:00'),
  ('200秒杀红米note', 400, '2017-08-01 00:00:00', '2017-08-02 00:00:00');

DROP TABLE IF EXISTS success_killed;
CREATE TABLE success_killed(
  seckill_id BIGINT NOT NULL COMMENT '商品库存id',
  user_phone BIGINT NOT NULL COMMENT '用户手机号',
  state TINYINT NOT NULL DEFAULT -1 COMMENT '状态: -1表示无效',
  create_time TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY (seckill_id, user_phone),
  KEY idx_create_time (create_time)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';

DESCRIBE seckill;
