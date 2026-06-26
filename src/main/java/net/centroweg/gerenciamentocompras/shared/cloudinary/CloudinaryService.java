package net.centroweg.gerenciamentocompras.shared.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.exception.InvalidFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;
    private static final String JPEG_CONTENT_TYPE = "image/jpeg";
    private static final String PNG_CONTENT_TYPE = "image/png";
    private static final String WEBP_CONTENT_TYPE = "image/webp";
    private static final String PDF_CONTENT_TYPE = "application/pdf";
    private static final String DOCX_CONTENT_TYPE =
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private static final String XLSX_CONTENT_TYPE =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final Cloudinary cloudinary;

    public Map upload(MultipartFile file) throws IOException {
        byte[] bytes = validateAndRead(file);
        if (!isImageContentType(file.getContentType())) {
            throw new InvalidFileException("Tipo de arquivo nao permitido.");
        }

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

        String contentType = file.getContentType();
        if (!isAllowedContentType(contentType)) {
            throw new InvalidFileException("Tipo de arquivo nao permitido.");
        }

        byte[] bytes = file.getBytes();
        if (bytes.length == 0) {
            throw new InvalidFileException("O arquivo enviado esta vazio.");
        }

        if (bytes.length > MAX_FILE_SIZE) {
            throw new InvalidFileException("O arquivo deve possuir no maximo 10 MB.");
        }

        if (!isValidBytesForContentType(bytes, contentType)) {
            throw new InvalidFileException("Tipo de arquivo nao permitido.");
        }

        return bytes;
    }

    private boolean isAllowedContentType(String contentType) {
        return JPEG_CONTENT_TYPE.equals(contentType)
                || PNG_CONTENT_TYPE.equals(contentType)
                || WEBP_CONTENT_TYPE.equals(contentType)
                || PDF_CONTENT_TYPE.equals(contentType)
                || DOCX_CONTENT_TYPE.equals(contentType)
                || XLSX_CONTENT_TYPE.equals(contentType);
    }

    private boolean isImageContentType(String contentType) {
        return JPEG_CONTENT_TYPE.equals(contentType)
                || PNG_CONTENT_TYPE.equals(contentType)
                || WEBP_CONTENT_TYPE.equals(contentType);
    }

    private boolean isValidBytesForContentType(byte[] bytes, String contentType) {
        return switch (contentType) {
            case JPEG_CONTENT_TYPE -> isJpeg(bytes);
            case PNG_CONTENT_TYPE -> isPng(bytes);
            case WEBP_CONTENT_TYPE -> isWebp(bytes);
            case PDF_CONTENT_TYPE -> isPdf(bytes);
            case DOCX_CONTENT_TYPE -> isDocx(bytes);
            case XLSX_CONTENT_TYPE -> isXlsx(bytes);
            default -> false;
        };
    }

    private boolean isJpeg(byte[] bytes) {
        return startsWith(bytes, (byte) 0xFF, (byte) 0xD8, (byte) 0xFF);
    }

    private boolean isPng(byte[] bytes) {
        return startsWith(bytes, (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47);
    }

    private boolean startsWith(byte[] bytes, byte... expectedBytes) {
        if (bytes.length < expectedBytes.length) {
            return false;
        }

        for (int i = 0; i < expectedBytes.length; i++) {
            if (bytes[i] != expectedBytes[i]) {
                return false;
            }
        }

        return true;
    }

    private boolean isWebp(byte[] bytes) {
        return bytes.length >= 12
                && startsWith(bytes, (byte) 0x52, (byte) 0x49, (byte) 0x46, (byte) 0x46)
                && bytes[8] == (byte) 0x57
                && bytes[9] == (byte) 0x45
                && bytes[10] == (byte) 0x42
                && bytes[11] == (byte) 0x50;
    }

    private boolean isPdf(byte[] bytes) {
        return startsWith(bytes, (byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46, (byte) 0x2D);
    }

    private boolean isDocx(byte[] bytes) {
        return isOfficeDocument(bytes, "word/");
    }

    private boolean isXlsx(byte[] bytes) {
        return isOfficeDocument(bytes, "xl/");
    }

    private boolean isOfficeDocument(byte[] bytes, String requiredFolder) {
        if (!isZip(bytes)) {
            return false;
        }

        boolean hasContentTypes = false;
        boolean hasRequiredFolder = false;

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(bytes))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();

                if ("[Content_Types].xml".equals(entryName)) {
                    hasContentTypes = true;
                }

                if (entryName.startsWith(requiredFolder)) {
                    hasRequiredFolder = true;
                }

                if (hasContentTypes && hasRequiredFolder) {
                    return true;
                }
            }
        } catch (IOException exception) {
            return false;
        }

        return false;
    }

    private boolean isZip(byte[] bytes) {
        return startsWith(bytes, (byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04);
    }
}
