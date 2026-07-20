package net.centroweg.gerenciamentocompras.shared.cloudinary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.exception.InvalidFileException;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;

    private final Cloudinary cloudinary;

    public Map upload(MultipartFile file) throws IOException {
        byte[] bytes = validateAndRead(file);
        validateProfilePicture(file, bytes);

        return cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
    }

    public Map<?, ?> uploadFile(MultipartFile file) throws IOException {
        byte[] bytes = validateAndRead(file);
        validateAttachment(file, bytes);

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

    private void validateProfilePicture(MultipartFile file, byte[] bytes) {
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/") || !isImage(bytes)) {
            throw new InvalidFileException("A foto de perfil deve ser uma imagem valida.");
        }
    }

    private void validateAttachment(MultipartFile file, byte[] bytes) throws IOException {
        String contentType = file.getContentType();

        if (contentType == null) {
            throw new InvalidFileException("Tipo de arquivo nao informado.");
        }

        boolean valid = switch (contentType) {
            case "application/pdf" -> startsWith(bytes, "%PDF-".getBytes());
            case "image/png", "image/jpeg" -> isImage(bytes);
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ->
                    isDocx(bytes);
            default -> false;
        };

        if (!valid) {
            throw new InvalidFileException("O conteudo do arquivo nao corresponde ao tipo informado.");
        }
    }

    private boolean isImage(byte[] bytes) {
        return startsWith(bytes, new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47})
                || startsWith(bytes, new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});
    }

    private boolean isDocx(byte[] bytes) throws IOException {
        boolean hasContentTypes = false;
        boolean hasDocument = false;

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(bytes))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if ("[Content_Types].xml".equals(entry.getName())) {
                    hasContentTypes = true;
                }
                if ("word/document.xml".equals(entry.getName())) {
                    hasDocument = true;
                }
            }
        }

        return hasContentTypes && hasDocument;
    }

    private boolean startsWith(byte[] bytes, byte[] prefix) {
        if (bytes.length < prefix.length) {
            return false;
        }

        for (int i = 0; i < prefix.length; i++) {
            if (bytes[i] != prefix[i]) {
                return false;
            }
        }

        return true;
    }
}
