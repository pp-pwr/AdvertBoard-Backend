package ppztw.AdvertBoard.Payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ReportProfileRequest {

    @NotNull
    Long profileId;

    @NotNull
    String comment;
}
