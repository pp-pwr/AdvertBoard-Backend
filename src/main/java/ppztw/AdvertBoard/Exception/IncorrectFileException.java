package ppztw.AdvertBoard.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IncorrectFileException extends RuntimeException {
    public IncorrectFileException(String fileName, Object fieldValue) {
        super(String.format("Couldn't load file: %s. Error message: %s", fileName, fieldValue));
    }
}
