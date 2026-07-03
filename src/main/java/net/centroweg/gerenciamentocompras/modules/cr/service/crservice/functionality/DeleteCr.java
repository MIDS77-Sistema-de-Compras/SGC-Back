package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;

/**
 * Caso de uso responsável por deletar um {@link Cr}.
 */
@Service
@RequiredArgsConstructor
public class DeleteCr {
    private final CrRepository crRepository;
    private final CrMapper crMapper;

    /**
     * Deleta um CR do banco de dados.
     * @param id identificador único do CR.
     * @return mensagem de sucesso da remoção.
     * @throws CrNotFoundException caso não exista um CR com o identificador informado.
     */
    public MessageDTO delete(Long id){
        if (!crRepository.existsById(id)) {
            throw new CrNotFoundException(id);
        }
        crRepository.deleteById(id);
        return new MessageDTO("CR Deletado com sucesso!");
    }
}
