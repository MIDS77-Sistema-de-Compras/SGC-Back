package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Service;

/**
 * Responsável pela funcionalidade de exclusão de Centros de Responsabilidade (CR).
 */
@Service
@RequiredArgsConstructor
public class DeleteCr {
    private final CrRepository crRepository;
    private final CrMapper crMapper;

    /**
     * Realiza a exclusão de um Centro de Responsabilidade (CR)
     * com base no identificador informado.
     * <p>
     * Antes de efetuar a remoção, é realizada uma validação para
     * verificar se o registro existe na base de dados. Caso não seja
     * encontrado, uma exceção {@link CrNotFoundException} é lançada.
     * Após a exclusão bem-sucedida, uma mensagem de confirmação é retornada.
     *
     * @param id identificador único do CR a ser excluído
     * @return {@link MessageDTO} contendo a mensagem de sucesso da operação
     * @throws CrNotFoundException caso não exista um CR com o identificador informado
     */
    public MessageDTO delete(Long id){
        if (!crRepository.existsById(id)) {
            throw new CrNotFoundException(id);
        }
        crRepository.deleteById(id);
        return new MessageDTO("CR Deletado com sucesso!");
    }
}
