package triersistemas.estagio_back_end.dto.request;

import java.util.List;
import java.util.Map;

public record PagedRequestDto(
        List<Orderer> orderer,
        Map<String, String> filter
) {
}
