package co.istad.mobilebanking.feature.file;

import co.istad.mobilebanking.feature.file.dto.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileResponse upload(@RequestPart MultipartFile file, @RequestParam String phoneNumber) throws IOException {
        return fileService.uploadProfile(file, phoneNumber);
    }


}
