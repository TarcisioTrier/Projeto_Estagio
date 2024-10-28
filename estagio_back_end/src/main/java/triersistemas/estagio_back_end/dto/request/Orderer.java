package triersistemas.estagio_back_end.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Orderer {
    private String field;
    private Integer order;

}
