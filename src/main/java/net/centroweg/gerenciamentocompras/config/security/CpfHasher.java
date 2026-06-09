package net.centroweg.gerenciamentocompras.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@Component
public class CpfHasher {


    private final byte[] secret;

    public CpfHasher (@Value("${app.security.cpf-secret}")String phrase){
        this.secret = phrase.getBytes(StandardCharsets.UTF_8);
    }

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
