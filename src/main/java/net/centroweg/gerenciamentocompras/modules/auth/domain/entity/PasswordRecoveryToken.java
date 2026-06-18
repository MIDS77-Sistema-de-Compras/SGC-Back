package net.centroweg.gerenciamentocompras.modules.auth.domain.entity;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PasswordRecoveryToken {
    
    @NonNull private Long userId;

    @NonNull private String hashedToken;

    @NonNull private LocalTime expiration;

}
