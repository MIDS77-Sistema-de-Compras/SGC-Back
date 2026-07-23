package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import net.centroweg.gerenciamentocompras.shared.cloudinary.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

/**
 * Caso de uso responsável pela atualização da foto de perfil de um {@link User}.
 */
@RequiredArgsConstructor
@Service
public class UploadProfilePicture {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final UserMapper userMapper;

    /**
     * Realiza o upload da foto de perfil de um usuário existente no banco de dados.
     * @param id identificador do usuário.
     * @param file arquivo da foto de perfil.
     * @return usuário já atualizado.
     * @throws UserNotFoundException caso nenhum usuário seja encontrado.
     * @throws IOException caso ocorra um erro durante o upload do arquivo.
     */
    public UserResponse uploadProfilePicture(Long id, MultipartFile file) throws IOException{
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(""));
        Map result = cloudinaryService.upload(file);
        String url = (String) result .get("url");

        user.setProfilePicture(url);
        return userMapper.toDTO(userRepository.save(user));
    }


}
