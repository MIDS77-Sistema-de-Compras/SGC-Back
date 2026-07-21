package net.centroweg.gerenciamentocompras.shared.annotation.aspect;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.github.bucket4j.Bucket;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import net.centroweg.gerenciamentocompras.shared.annotation.RateLimit;

@Aspect
@Component
public class RateLimitAspect {
    private final List<Map<String, Bucket>> profiles;

    @Autowired
    public RateLimitAspect(@Qualifier("loginBucket") Bucket loginBucket, @Qualifier("recoveryBucket") Bucket recoveryBucket){
        this.profiles = List.of(
            Map.of("login", loginBucket),
            Map.of("recovery", recoveryBucket)
        );
    }

    @Around("@annotation(rateLimit)")
    public Object applyRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        AtomicReference<Bucket> bucket = new AtomicReference<>();
        this.profiles.forEach(index -> {
            if (index.containsKey(rateLimit.profile())) {
                bucket.set(index.get(rateLimit.profile()));
            }
        });

        Bucket profileBucket = bucket.get();

        if (profileBucket.tryConsume(1)) {
            return joinPoint.proceed();
        }

        System.out.println("You consumed all your tokens brother.");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                new MessageDTO("Muitas requisições em um curto período de tempo! Tente novamente em alguns instantes.")
        );
    }

}
