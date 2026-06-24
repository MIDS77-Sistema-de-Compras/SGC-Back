package net.centroweg.gerenciamentocompras.shared.email.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class DefaultEmail{
    @NonNull
    private String subject;

    @NonNull
    private String sendTo;

    private String text;

}