package triersistemas.estagio_back_end.validators;

import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.entity.Enderecos;

public interface EnderecosValidator {

    Enderecos validateEndereco(EnderecosDto enderecosDto);
}
