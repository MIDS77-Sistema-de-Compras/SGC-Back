package net.centroweg.gerenciamentocompras.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuração de funções assíncronas como por exemplo: e-mail.
 */

@Configuration
@EnableAsync
public class AsyncConfig {
}
