package net.centroweg.gerenciamentocompras.shared.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.shared.exception.InvalidFileTypeException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    private static final List<String> ALLOWED_TYPES = List.of(
            "image/png", "image/jpeg", "image/webp"
            );

    public Map upload(MultipartFile file) throws IOException {
        String contentType = file.getContentType();

        if(contentType == null || !ALLOWED_TYPES.contains(contentType)){
            throw new InvalidFileTypeException();
        }

        byte[] bytes = file.getBytes();

        if (!isValidImageBytes(bytes)){
            throw new InvalidFileTypeException();
        }

        return cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
    }

    private boolean isValidImageBytes(byte[] bytes){
        if (bytes.length < 12) return false;

        boolean isJpeg = bytes[0] == (byte) 0xFF &&
                bytes[1] == (byte) 0xD8 &&
                bytes[2] == (byte) 0xFF;

        boolean isPng = bytes[0] == (byte) 0x89 &&
                bytes[1] == (byte) 0x50 &&
                bytes[2] == (byte) 0x4E &&
                bytes[3] == (byte) 0x47;

        boolean isWebp = bytes[0] == (byte) 0x52 &&
                bytes[1] == (byte) 0x49 &&
                bytes[2] == (byte) 0x46 &&
                bytes[3] == (byte) 0x46 &&
                bytes[8] == (byte) 0x57 &&
                bytes[9] == (byte) 0x45 &&
                bytes[10] == (byte) 0x42 &&
                bytes[11] == (byte) 0x50;

        return isJpeg || isPng || isWebp;
    }

}
