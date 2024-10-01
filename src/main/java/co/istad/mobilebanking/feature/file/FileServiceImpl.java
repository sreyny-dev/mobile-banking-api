package co.istad.mobilebanking.feature.file;

import co.istad.mobilebanking.domain.User;
import co.istad.mobilebanking.feature.file.dto.FileResponse;
import co.istad.mobilebanking.feature.user.UserRepository;
import co.istad.mobilebanking.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final UserRepository userRepository;

    @Value("${file-server.server-path}")
    private String serverPath;

    @Value("${file-server.base-uri}")
    private String baseUri;

    @Override
    public FileResponse uploadProfile(MultipartFile file, String phoneNumber) throws IOException {


        if(!userRepository.existsByPhoneNumber(phoneNumber)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number");
        }

        //give new name to the file
        String newName= FileUtil.generateFileName(file.getOriginalFilename());
        //get extension
        String extension=FileUtil.extractionFileName(file.getOriginalFilename());

        User user=userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        //check if user already has profile picture
        //delete the old profile picture

        if (user.getProfilePicture() != null) {
            String existingFileName = user.getProfilePicture().substring(baseUri.length());
            Path existingFilePath = Path.of(serverPath + existingFileName);
            if (Files.exists(existingFilePath)) {
                Files.delete(existingFilePath);
            }
        }

        // Upload new profile picture
        Path filePath = Path.of(serverPath + newName);
        Files.copy(file.getInputStream(), filePath);

        // Update user profile picture
        user.setProfilePicture(baseUri + newName);
        userRepository.save(user);

        return FileResponse.builder()
                .name(newName)
                .size(file.getSize())
                .extension(extension)
                .contentType(file.getContentType())
                .uri(baseUri+newName)
                .build();
    }

}
