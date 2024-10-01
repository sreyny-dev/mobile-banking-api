package co.istad.mobilebanking.feature.file;

import co.istad.mobilebanking.feature.file.dto.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    FileResponse uploadProfile(MultipartFile file, String phoneNumber) throws IOException;
}
