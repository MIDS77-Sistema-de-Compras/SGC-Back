package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
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

@RequiredArgsConstructor
@Service
public class UploadProfilePicture {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final UserMapper userMapper;

    public UserResponse uploadProfilePicture(Long id, MultipartFile file) throws IOException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return upload(user, file);
    }

    public UserResponse uploadLoggedUserProfilePicture(UserPrincipal userPrincipal, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(UserNotFoundException::new);
        return upload(user, file);
    }

    private UserResponse upload(User user, MultipartFile file) throws IOException {
        Map<?, ?> result = cloudinaryService.upload(file);
        Object secureUrl = result.get("secure_url");
        Object fallbackUrl = result.get("url");
        if (secureUrl == null && fallbackUrl == null) {
            throw new IOException("O servico de imagens nao retornou a URL da foto enviada.");
        }
        String url = secureUrl != null ? secureUrl.toString() : fallbackUrl.toString();

        user.setProfilePicture(url);
        return userMapper.toDTO(userRepository.save(user));
    }

}
