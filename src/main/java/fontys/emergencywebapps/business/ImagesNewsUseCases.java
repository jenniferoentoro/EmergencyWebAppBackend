package fontys.emergencywebapps.business;

import fontys.emergencywebapps.controllers.dto.ImagesNewsDto;

import java.io.IOException;

public interface ImagesNewsUseCases {
    void saveImage(ImagesNewsDto imagesNewsDto) throws IOException;
}
