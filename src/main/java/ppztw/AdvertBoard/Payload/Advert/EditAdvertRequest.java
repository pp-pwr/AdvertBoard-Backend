package ppztw.AdvertBoard.Payload.Advert;

import lombok.Getter;
import lombok.Setter;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class EditAdvertRequest {

    @NotNull
    private Long id;

    @Nullable
    private String title;

    @Nullable
    private List<String> tags;

    @Nullable
    private String description;

    @Nullable
    private MultipartFile imageFile;

    @Nullable
    private Map<String, String> additionalInfo;

}
