package triersistemas.estagio_back_end.services;

import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.entity.Enderecos;

public interface EnderecoService {

    Enderecos validateEndereco(EnderecosDto enderecosDto);
}
