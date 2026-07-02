package net.centroweg.gerenciamentocompras.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do cliente Cloudinary para upload e gerenciamento de arquivos de mídia.
 * @see com.cloudinary.Cloudinary
 */
@Configuration
public class CloudinaryConfig {

    /**
     * Nome da cloud registrada no Cloudinary.
     */
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    /**
     * Chave pública da API do Cloudinary.
     */
    @Value("${cloudinary.api-key}")
    private String cloudinaryApiKey;

    /**
     * Chave secreta da API do Cloudinary (nunca deve ser exposta publicamente).
     */
    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    /**
     * Expõe o cliente {@link Cloudinary} como um bean gerenciado pelo Spring, configurado com as credenciais da aplicação.
     * @return instância configurada de {@link Cloudinary}.
     */
    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
           "cloud_name", cloudName,
                    "api_key", cloudinaryApiKey,
                    "api_secret", apiSecret
        ));
    }
}
