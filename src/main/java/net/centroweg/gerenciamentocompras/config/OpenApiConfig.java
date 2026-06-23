package net.centroweg.gerenciamentocompras.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe responsável por fazer a configuração da documentação dos endpoints
 * @author hugo_paim
 * @version 0.1
 * */

@Configuration
public class OpenApiConfig {

    /**
     * Método que gerencia as configurações do Swagger
     * @return Classe que representa a padronização um formato de especificação para descrição de APIs REST
     * */
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("SGC - Sistema de Gerenciamento de Compra")
                        .description("API de gerenciamento de compras")
                        .version("v1.0.0"));

    }
}
