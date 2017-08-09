/**
 * Created by Administrator on 8/6/2017.
 */

var seckill = {

    /**
     * 封装秒杀相关ajax的url
     */
    URL : {
        now : function () {
            return '/seckill/time/now';
        },
        exposer : function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execute : function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }

    },

    validatePhone : function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },

    /**
     * 处理秒杀逻辑
     */
    handleSeckill : function (seckillId, node) {
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    // 开启秒杀
                    // 获取秒杀地址
                    var md5 = exposer['md5'];
                    var executeUrl = seckill.URL.execute(seckillId, md5);
                    console.log('kill url is ' + executeUrl);
                    // 绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        $(this).addClass('disabled');
                        $.post(executeUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResutl = result['data'];
                                var state = killResutl['state'];
                                var stateInfo = killResutl['stateInfo'];
                                // 显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                } else {
                    // 未开启秒杀
                    var now = result['now'];
                    var start = result['start'];
                    var end = result['end'];
                    seckill.countdown(seckillId, now, start, end);
                }
            } else {
                console.log('result: ' + result);
            }
        })
    },

    countdown : function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        // 秒杀结束
        if (nowTime > endTime) {
            seckillBox.html('秒杀已经结束');
        } else if (nowTime < startTime) {
            // 秒杀未开始，启用倒计时
            // +1000 防止倒计时偏移
            var killTime = new Date(startTime + 1000)
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                // 倒计时结束后回调，获取秒杀地址，控制逻辑执行秒杀
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },

    /**
     * 详情页秒杀相关逻辑
     */
    detail : {
        // 详情页初始化
        init: function (params) {
            // 手机验证和登录, 交互计时
            // 在cookie中查找手机号
            var killerPhone = $.cookie('killerPhone');
            console.log("id is " + seckillId + ", start " + startTime + ", end "　+endTime);
            //验证手机号
            if (!seckill.validatePhone(killerPhone)) {
                // 绑定phone，控制输出
                var killerPhoneModal = $('#killerPhoneModal');
                killerPhoneModal.modal({
                    show: true, // 显示弹出层
                    backdrop: 'static', // 禁止位置关闭
                    keyboard: false // 关闭键盘事件
                });

                $('#killerPhoneBtn').click(function () {
                    var inputPhone = $('#killerPhoneKey').val();
                    console.log("input phone number " + inputPhone);
                    if (seckill.validatePhone(inputPhone)) {
                        // 写入cookie
                        $.cookie('killerPhone', inputPhone, {expires: 7, path: '/seckill'});
                        // 刷新页面
                        window.location.reload();
                    } else {
                        $('#killerPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);

                    }
                });
            }
            // 已登录
            //计时
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                }
            })
        }
    }
}