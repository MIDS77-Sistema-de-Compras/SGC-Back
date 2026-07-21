package net.centroweg.gerenciamentocompras.shared.annotation.aspect;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.config.Bucket4jConfig;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import net.centroweg.gerenciamentocompras.shared.annotation.RateLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Aspect
@Component
public class RateLimitAspect {

    private Bucket loginBucket;
    private Bucket recoveryBucket;

    private List<Map<String, Bucket>> profiles = List.of(
            Map.of("login", loginBucket),
            Map.of("recovery", recoveryBucket)
    );

    @Around("@annotation(RateLimit)")
    public ResponseEntity<MessageDTO> applyRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        Bucket bucket = (Bucket) profiles.stream().map(index -> {
            if(index.containsKey(rateLimit.profile())){
                return index.get(rateLimit.profile());
            }
            return null;
        });

        if(bucket.tryConsume(1)){
            joinPoint.proceed();
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                new MessageDTO("Muitas requisições em um curto período de tempo! Tente novamente em alguns instantes.")
        );
    }

}
