package triersistemas.estagio_back_end.utils;

public interface CnpjValidator {

    void validateCnpj(String cnpj);

    void validateCnpjUpdateFornecedor(String cnpj, Long id);

    void validateCnpjUpdateFilial(String cnpj, Long id);
}
