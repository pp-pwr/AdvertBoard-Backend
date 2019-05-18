package ppztw.AdvertBoard.Payload.Advert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ReportAdvertRequest {

    @NotNull
    Long advertId;

    @NotNull
    String comment;
}
