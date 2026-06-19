package net.centroweg.gerenciamentocompras.shared.cloudinary;

import net.centroweg.gerenciamentocompras.shared.exception.InvalidFileException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CloudinaryServiceTest {

    private final CloudinaryService cloudinaryService = new CloudinaryService(null);

    @Test
    void shouldRejectNullAttachment() {
        assertThrows(
                InvalidFileException.class,
                () -> cloudinaryService.uploadFile(null)
        );
    }

    @Test
    void shouldRejectEmptyAttachment() {
        MultipartFile file = new MockMultipartFile(
                "files",
                "empty.pdf",
                "application/pdf",
                new byte[0]
        );

        assertThrows(
                InvalidFileException.class,
                () -> cloudinaryService.uploadFile(file)
        );
    }

    @Test
    void shouldRejectAttachmentGreaterThanTenMegabytes() {
        MultipartFile file = new MockMultipartFile(
                "files",
                "large.pdf",
                "application/pdf",
                new byte[(10 * 1024 * 1024) + 1]
        );

        assertThrows(
                InvalidFileException.class,
                () -> cloudinaryService.uploadFile(file)
        );
    }

    @Test
    void shouldRejectAttachmentWhenContentTypeDoesNotMatchBytes() {
        MultipartFile file = new MockMultipartFile(
                "files",
                "fake.pdf",
                "application/pdf",
                new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47}
        );

        assertThrows(
                InvalidFileException.class,
                () -> cloudinaryService.uploadFile(file)
        );
    }

    @Test
    void shouldRejectZipWithoutDocxStructure() throws IOException {
        MultipartFile file = new MockMultipartFile(
                "files",
                "fake.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                zipWithEntries("[Content_Types].xml", "xl/workbook.xml")
        );

        assertThrows(
                InvalidFileException.class,
                () -> cloudinaryService.uploadFile(file)
        );
    }

    @Test
    void shouldRejectNonImageProfilePictureUpload() {
        MultipartFile file = new MockMultipartFile(
                "file",
                "document.pdf",
                "application/pdf",
                "%PDF-1.7".getBytes()
        );

        assertThrows(
                InvalidFileException.class,
                () -> cloudinaryService.upload(file)
        );
    }

    private byte[] zipWithEntries(String... entries) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            for (String entry : entries) {
                zipOutputStream.putNextEntry(new ZipEntry(entry));
                zipOutputStream.write("content".getBytes());
                zipOutputStream.closeEntry();
            }
        }

        return outputStream.toByteArray();
    }
}
