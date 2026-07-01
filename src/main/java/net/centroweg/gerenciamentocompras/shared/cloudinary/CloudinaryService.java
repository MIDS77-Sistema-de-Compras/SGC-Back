package net.centroweg.gerenciamentocompras.shared.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.exception.InvalidFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;

    private final Cloudinary cloudinary;

    public Map upload(MultipartFile file) throws IOException {
        byte[] bytes = validateAndRead(file);
        return cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
    }

    public Map<?, ?> uploadFile(MultipartFile file) throws IOException {
        byte[] bytes = validateAndRead(file);

        return cloudinary.uploader().upload(
                bytes,
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "folder", "request-attachments",
                        "use_filename", true,
                        "unique_filename", true
                )
        );
    }

    private byte[] validateAndRead(MultipartFile file) throws IOException {
        if (file == null) {
            throw new InvalidFileException("O arquivo enviado nao pode ser nulo.");
        }

        if (file.isEmpty()) {
            throw new InvalidFileException("O arquivo enviado esta vazio.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException("O arquivo deve possuir no maximo 10 MB.");
        }

        byte[] bytes = file.getBytes();
        if (bytes.length == 0) {
            throw new InvalidFileException("O arquivo enviado esta vazio.");
        }

        if (bytes.length > MAX_FILE_SIZE) {
            throw new InvalidFileException("O arquivo deve possuir no maximo 10 MB.");
        }

        return bytes;
    }

}
