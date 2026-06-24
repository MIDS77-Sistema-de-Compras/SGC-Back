package net.centroweg.gerenciamentocompras.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/**
 * Componente responsável por criar Hashes de CPFs utilizando HMAC-SHA256
 *
 * @see javax.crypto.Mac
 */
@Component
public class CpfHasher {

    /**
     * Chave secreta em bytes utilizada na criação do HMAC
     */
    private final byte[] secret;

    /**
     * Constrói o {@code CpfHasher} com a chave secreta definia nas propriedades da aplicação
     *
     * @param phrase frase secreta injetada via {@code app.security.cpf-secret}
     */
    public CpfHasher (@Value("${app.security.cpf-secret}")String phrase){
        this.secret = phrase.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Gera um hash HMAC-SHA256 do CPF fornecido
     * @param cpf CPF a ser hasheado
     * @return String hexadecimal representando o hash HMAC-SHA256 do usuário
     * @throws RuntimeException se ocorrer algum erro durante o processo de hashing
     */
    public String hash(String cpf) {
        try{
            String key = new String (secret, StandardCharsets.UTF_8);
            Mac mac = Mac.getInstance("HmacSHA256");

            mac.init(new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"
            ));
            byte [] hashBytes = mac.doFinal(cpf.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashBytes);

        }catch (Exception e){
            throw new RuntimeException("Error", e);
        }
    }
}
