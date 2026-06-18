package net.centroweg.gerenciamentocompras.shared.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.InvalidAttachmentException;
import net.centroweg.gerenciamentocompras.shared.exception.InvalidFileTypeException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;

    private final Cloudinary cloudinary;

    public Map upload(MultipartFile file) throws IOException {
        byte[] bytes = validateAndRead(
                file,
                EnumSet.of(FileType.JPEG, FileType.PNG, FileType.WEBP),
                true
        );

        return cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
    }

    public Map<?, ?> uploadFile(MultipartFile file) throws IOException {
        byte[] bytes = validateAndRead(file, EnumSet.allOf(FileType.class), false);

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

    private byte[] validateAndRead(
            MultipartFile file,
            Set<FileType> allowedFileTypes,
            boolean profilePictureUpload
    ) throws IOException {
        if (file == null || file.isEmpty()) {
            throwInvalidFile(profilePictureUpload, "O arquivo enviado esta vazio.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throwInvalidFile(profilePictureUpload, "O arquivo deve possuir no maximo 10 MB.");
        }

        String contentType = file.getContentType();
        FileType contentTypeFileType = FileType.fromContentType(contentType);
        if (contentTypeFileType == null || !allowedFileTypes.contains(contentTypeFileType)) {
            throwInvalidFile(profilePictureUpload, "Tipo de arquivo nao permitido.");
        }

        if (!contentTypeFileType.matchesExtension(file.getOriginalFilename())) {
            throwInvalidFile(profilePictureUpload, "Extensao de arquivo nao permitida.");
        }

        byte[] bytes = file.getBytes();
        if (bytes.length == 0) {
            throwInvalidFile(profilePictureUpload, "O arquivo enviado esta vazio.");
        }

        if (bytes.length > MAX_FILE_SIZE) {
            throwInvalidFile(profilePictureUpload, "O arquivo deve possuir no maximo 10 MB.");
        }

        FileType realFileType = detectFileType(bytes);
        if (realFileType == null || realFileType != contentTypeFileType) {
            throwInvalidFile(profilePictureUpload, "Tipo de arquivo nao permitido.");
        }

        return bytes;
    }

    private FileType detectFileType(byte[] bytes) {
        if (startsWith(bytes, (byte) 0xFF, (byte) 0xD8, (byte) 0xFF)) {
            return FileType.JPEG;
        }

        if (startsWith(bytes, (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47)) {
            return FileType.PNG;
        }

        if (isWebp(bytes)) {
            return FileType.WEBP;
        }

        if (startsWith(bytes, (byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46, (byte) 0x2D)) {
            return FileType.PDF;
        }

        if (isOfficeDocument(bytes, "word/")) {
            return FileType.DOCX;
        }

        if (isOfficeDocument(bytes, "xl/")) {
            return FileType.XLSX;
        }

        return null;
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
                && Arrays.equals(
                Arrays.copyOfRange(bytes, 8, 12),
                new byte[]{(byte) 0x57, (byte) 0x45, (byte) 0x42, (byte) 0x50}
        );
    }

    private boolean isOfficeDocument(byte[] bytes, String requiredFolder) {
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

    private void throwInvalidFile(boolean profilePictureUpload, String message) {
        if (profilePictureUpload) {
            throw new InvalidFileTypeException();
        }

        throw new InvalidAttachmentException(message);
    }

    private enum FileType {
        JPEG("image/jpeg", Set.of("jpg", "jpeg")),
        PNG("image/png", Set.of("png")),
        WEBP("image/webp", Set.of("webp")),
        PDF("application/pdf", Set.of("pdf")),
        DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", Set.of("docx")),
        XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", Set.of("xlsx"));

        private final String contentType;
        private final Set<String> extensions;

        FileType(String contentType, Set<String> extensions) {
            this.contentType = contentType;
            this.extensions = extensions;
        }

        private static FileType fromContentType(String contentType) {
            return Arrays.stream(values())
                    .filter(fileType -> fileType.contentType.equals(contentType))
                    .findFirst()
                    .orElse(null);
        }

        private boolean matchesExtension(String filename) {
            String extension = StringUtils.getFilenameExtension(filename);
            return extension != null
                    && extensions.contains(extension.toLowerCase(Locale.ROOT));
        }
    }
}
