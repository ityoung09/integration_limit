package com.libsept24.limit.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.libsept24.limit.entity.User;
import com.libsept24.limit.handler.annotations.LocalLimit;
import com.libsept24.limit.handler.annotations.RedisLimit;
import com.libsept24.limit.res.ResultResponse;
import com.libsept24.limit.res.StatusEnum;
import com.libsept24.limit.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class UserController {

    /**
     * 限流策略：令牌桶算法
     * 1s中生成2个令牌
     */
    private final RateLimiter limiter = RateLimiter.create(1.0);

    @Resource
    private IUserService userService;

    @PostMapping("/queryUserInfos")
    public ResultResponse<List<User>> queryUserInfos(){
        boolean b = limiter.tryAcquire(0, TimeUnit.MILLISECONDS);
        if (!b){
            log.warn("进入服务降级，时间{}", LocalDateTime.now());
            return ResultResponse.error(StatusEnum.SERVICE_ERROR, "当前排队人数较多，请稍后再试！");
        }
        List<User> users = userService.queryUserInfos();
        return ResultResponse.success(users);
    }

    @RedisLimit(key = "queryUserInfos2", permitsPerSecond = 11, timeout = 0, msg = "当前排队人数较多，请稍后再试！")
    @PostMapping("/queryUserInfos2")
    public ResultResponse<List<User>> queryUserInfos2(){
        List<User> users = userService.queryUserInfos();
        return ResultResponse.success(users);
    }

}
