package triersistemas.estagio_back_end.validators;

public interface CnpjValidator {

    void validateCnpjUpdateFornecedor(String cnpj, Long id);

    void validateCnpjUpdateFilial(String cnpj, Long id);

    void validateCnpjPostFilial(String cnpj);

    void validateCnpjPostFornecedor(String cnpj);
}
