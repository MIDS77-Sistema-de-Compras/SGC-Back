package net.centroweg.gerenciamentocompras.shared.annotation.aspect;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.github.bucket4j.Bucket;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import net.centroweg.gerenciamentocompras.shared.annotation.RateLimit;

/**
 * Aspect contendo o método executado antes de cada método com a anotação {@code @RateLimit}
 *
 * @author gabrielEFagundes
 * @since 21/07/2026
 */
@Aspect
@Component
public class RateLimitAspect {
    private final List<Map<String, Bucket>> profiles;
    private final Environment environment;

    @Autowired
    public RateLimitAspect(@Qualifier("loginBucket") Bucket loginBucket, @Qualifier("recoveryBucket") Bucket recoveryBucket, Environment environment){
        this.profiles = List.of(
            Map.of("login", loginBucket),
            Map.of("recovery", recoveryBucket)
        );
        this.environment = environment;
    }

    /**
     * Método executado antes de cada método anotado com {@code @RateLimit}
     * @param joinPoint Utilizado para a execução do método anotado, em caso de sucesso
     * @param rateLimit Anotação utilizada para extrair o profile escolhido.
     * @return {@code Object} Uma {@code ResponseEntity} contendo uma {@code MessageDTO} OU o método anotado
     * @throws Throwable Possível {@code IllegalArgumentException}, caso um profile inexistente seja escolhido
     */
    @Around("@annotation(rateLimit)")
    public Object applyRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        // ignores if it's on test environment
        if(this.environment.acceptsProfiles(Profiles.of("test")))
            return joinPoint.proceed();

        AtomicReference<Bucket> bucket = new AtomicReference<>();
        this.profiles.forEach(index -> {
            if (index.containsKey(rateLimit.profile())) {
                bucket.set(index.get(rateLimit.profile()));
            }
        });

        Bucket profileBucket = bucket.get();

        if(profileBucket == null){
            throw new IllegalArgumentException("""
                    Chosen profile probably doesn't exist. Consider looking inside the class Bucket4jConfig
                    and choose an existing profile in there.
                    
                    @RateLimit("%s")
                    ~~~~~~~~~~^^%s^^ HERE
                    """.formatted(rateLimit.profile(), "^".repeat(rateLimit.profile().length())));
        }

        if (profileBucket.tryConsume(1)) {
            return joinPoint.proceed();
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                new MessageDTO("Muitas requisições em um curto período de tempo! Tente novamente em alguns instantes.")
        );
    }

}
