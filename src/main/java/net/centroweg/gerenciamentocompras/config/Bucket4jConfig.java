package net.centroweg.gerenciamentocompras.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Configuration
public class Bucket4jConfig {

    @Bean
    public Bucket loginBucket(){
        Bandwidth loginBandwidth = Bandwidth.builder().capacity(5).refillGreedy(1, Duration.ofMinutes(1)).build();
        return Bucket.builder().addLimit(loginBandwidth).build();
    }

    @Bean
    public Bucket recoveryBucket(){
        Bandwidth recoveryBandwidth = Bandwidth.builder().capacity(1).refillIntervally(1, Duration.ofMinutes(5)).build();
        return Bucket.builder().addLimit(recoveryBandwidth).build();
    }

}
