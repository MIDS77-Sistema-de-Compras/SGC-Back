package net.centroweg.gerenciamentocompras.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class Bucket4jConfig {

    @Bean("loginBucket")
    public Bucket loginBucket(){
        Bandwidth loginBandwidth = Bandwidth.builder().capacity(5).refillGreedy(1, Duration.ofMinutes(1)).build();
        return Bucket.builder().addLimit(loginBandwidth).build();
    }

    @Bean("recoveryBucket")
    public Bucket recoveryBucket(){
        Bandwidth recoveryBandwidth = Bandwidth.builder().capacity(2).refillIntervally(2, Duration.ofMinutes(5)).build();
        return Bucket.builder().addLimit(recoveryBandwidth).build();
    }

}
