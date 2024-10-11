package triersistemas.estagio_back_end.validators;

import org.springframework.stereotype.Component;
import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.entity.Enderecos;
import triersistemas.estagio_back_end.exceptions.InvalidCepException;

public interface CnpjValidator {

    void validateCnpj(String cnpj);

    void validateCnpjUpdateFornecedor(String cnpj, Long id);

    void validateCnpjUpdateFilial(String cnpj, Long id);

}
