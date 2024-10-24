package triersistemas.estagio_back_end.dto.request;

import java.util.List;

public record PagedRequestDto(
        List<Orderer> orderer,
        List<Filter> filter
) {
}
