package net.centroweg.gerenciamentocompras.modules.cr.service.crservice;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrMapper;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCr {
    private final CrRepository crRepository;
    private final CrMapper crMapper;

    public MessageDTO delete(long id){
        if (!crRepository.existsById(id)) {
            throw new CrNotFoundException(id);
        }
        crRepository.deleteById(id);
        return new MessageDTO("CR Deletado com sucesso!");
    }
}
