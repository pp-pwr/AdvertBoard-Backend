package ppztw.AdvertBoard.Payload.Advert;


import lombok.Getter;
import lombok.Setter;

import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CreateAdvertRequest {

    @NotBlank
    private String title;

    @Nullable
    private List<String> tags;

    @NotBlank
    private String description;

    @NotNull
    private Long category;

//    @Nullable
//    private ImagePayload image;

    @Nullable
    private MultipartFile imageFile;

    @Nullable
    private Map<String, String> additionalInfo;
}
