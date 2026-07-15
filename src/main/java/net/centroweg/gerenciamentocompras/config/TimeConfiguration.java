package net.centroweg.gerenciamentocompras.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Disponibiliza a fonte de tempo da aplicação para permitir testes determinísticos.
 * O {@link java.time.LocalDateTime} resultante usa o fuso horário padrão da JVM.
 */
@Configuration
public class TimeConfiguration {

    @Bean
    public Clock applicationClock() {
        return Clock.systemDefaultZone();
    }
}
