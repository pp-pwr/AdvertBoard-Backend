package ppztw.AdvertBoard.Util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class StringToMapConverter  implements Converter<String, Map<?, ?>> {

    @Override
    public Map<?, ?> convert(String source) {
        try {
            return new ObjectMapper().readValue(source, new TypeReference<Map<?, ?>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
