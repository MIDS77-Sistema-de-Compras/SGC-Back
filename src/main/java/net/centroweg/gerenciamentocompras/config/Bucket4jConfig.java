package net.centroweg.gerenciamentocompras.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Classe de configuração do Bucket4j
 *
 * A classe cria 2 beans atualmente, que são os profiles disponíveis.
 * <ul>
 *     <li>login -> Mapeia para {@code loginBucket}</li>
 *     <li>recovery -> Mapeia para {@code recoveryBucket}</li>
 * </ul>
 *
 * Em caso de expansão, crie beans com os métodos retornando {@code Bucket}.
 * @author gabrielEFagundes
 * @since 21/07/2026
 */
@Configuration
@EnableAspectJAutoProxy
public class Bucket4jConfig {

    /**
     * Bean de criação do Bucket de limitação de Login
     * <p>
     * Limita as requisições do perfil "login" em 5 por minuto, com os tokens sendo restaurados gradualmente.
     * @return {@code Bucket} O {@code Bucket} contendo o limite do profile "login"
     */
    @Bean("loginBucket")
    public Bucket loginBucket(){
        Bandwidth loginBandwidth = Bandwidth.builder().capacity(5).refillGreedy(1, Duration.ofMinutes(1)).build();
        return Bucket.builder().addLimit(loginBandwidth).build();
    }

    /**
     * Bean de criação do {@code Bucket} de limitação da Recuperação de Senha
     * <p>
     * Limita as requisições do perfil "recovery" em 2 a cada 5 minutos, com os tokens sendo restaurados de uma vez após o período de 5 minutos.
     * @return {@code Bucket} O {@code Bucket} contendo o limite do profile "recovery"
     */
    @Bean("recoveryBucket")
    public Bucket recoveryBucket(){
        Bandwidth recoveryBandwidth = Bandwidth.builder().capacity(2).refillIntervally(2, Duration.ofMinutes(5)).build();
        return Bucket.builder().addLimit(recoveryBandwidth).build();
    }

}
