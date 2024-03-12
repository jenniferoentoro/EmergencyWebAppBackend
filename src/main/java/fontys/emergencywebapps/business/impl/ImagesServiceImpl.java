package fontys.emergencywebapps.business.impl;

import fontys.emergencywebapps.controllers.dto.ImagesNewsDto;
import fontys.emergencywebapps.persistence.entities.ImagesNews;
import fontys.emergencywebapps.persistence.repos.ImagesNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import fontys.emergencywebapps.business.ImagesNewsUseCases;
import fontys.emergencywebapps.business.exceptions.CustomException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImagesServiceImpl implements ImagesNewsUseCases {

    private final ImagesNewsRepository imagesNewsRepository;

    @Override
    public void saveImage(ImagesNewsDto imagesNewsDto) throws IOException {
        if (imagesNewsDto.getFile().getSize() > 3000000) {
            throw new CustomException("File size is too large");
        }


        UUID uuid = UUID.randomUUID();
        String fileExtension = "";

        if (imagesNewsDto.getFile() == null || imagesNewsDto.getFile().getOriginalFilename() == null) {
            throw new CustomException("File extension is not valid");
        }

        String originalFilename = imagesNewsDto.getFile().getOriginalFilename();
        if (originalFilename !=null && originalFilename.contains(".")) {
            String[] splitFileName = originalFilename.split("\\.");
            if (splitFileName.length > 1) {
                fileExtension = splitFileName[1];
            }
        } else {
            throw new CustomException("File extension is not valid");
        }


        String fileName = uuid.toString() + "." + fileExtension;
        String uploadDirPath = "D:\\Kuliah\\Fontys\\Semester3\\Java\\Project\\emergency-web-apps-frontend\\public\\" + "imagesNews";
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        imagesNewsDto.getFile().transferTo(new File(uploadDirPath + File.separator + fileName));

        ImagesNews imagesNews = ImagesNews.builder()
                .photoUrl(fileName)
                .build();

        imagesNewsRepository.save(imagesNews);

    }
}
