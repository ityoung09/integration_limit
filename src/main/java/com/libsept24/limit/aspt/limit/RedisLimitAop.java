package com.libsept24.limit.aspt.limit;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.libsept24.limit.handler.annotations.LocalLimit;
import com.libsept24.limit.handler.annotations.RedisLimit;
import com.libsept24.limit.res.ResultResponse;
import com.libsept24.limit.res.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class RedisLimitAop {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 不同的接口，不同的流量控制
     * map的key为 Limiter.key
     */
    private final Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    @Around("@annotation(com.libsept24.limit.handler.annotations.RedisLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //拿limit的注解
        RedisLimit limit = method.getAnnotation(RedisLimit.class);
        if (limit != null) {
            //key作用：不同的接口，不同的流量控制
            String key = limit.key();
            RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
            // rateInterval RateIntervalUnit 中生成limit.permitsPerSecond()个
            rateLimiter.trySetRate(RateType.OVERALL, limit.permitsPerSecond(), 1, RateIntervalUnit.SECONDS);
            boolean acquire = rateLimiter.tryAcquire(limit.timeout(), limit.timeunit());
            // 拿不到命令，直接返回异常提示
            if (!acquire) {
                log.debug("令牌桶={}，获取令牌失败", key);
                return ResultResponse.error(StatusEnum.SERVICE_ERROR, limit.msg());
            }
        }
        return joinPoint.proceed();
    }
}
