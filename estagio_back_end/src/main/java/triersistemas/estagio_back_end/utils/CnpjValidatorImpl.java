package triersistemas.estagio_back_end.utils;

import org.springframework.stereotype.Component;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.exceptions.InvalidCnpjException;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.repository.FornecedorRepository;

import java.util.Optional;

@Component
public class CnpjValidatorImpl implements CnpjValidator {

    private final FornecedorRepository fornecedorRepository;
    private final FilialRepository filialRepository;

    public CnpjValidatorImpl(FornecedorRepository fornecedorRepository, FilialRepository filialRepository) {
        this.fornecedorRepository = fornecedorRepository;
        this.filialRepository = filialRepository;
    }

    public void validateCnpj(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");
        if (cnpj.length() != 14) {
            throw new InvalidCnpjException("CNPJ inválido");
        }
    }

    public void validateCnpjUpdateFornecedor(String cnpj, Long id) {
        Optional<Fornecedor> fornecedorExistente = fornecedorRepository.findByCnpj(cnpj);
        if (fornecedorExistente.isPresent() && !fornecedorExistente.get().getId().equals(id)) {
            throw new InvalidCnpjException("CNPJ já cadastrado em outra empresa.");
        }
    }

    public void validateCnpjUpdateFilial(String cnpj, Long id) {
        Optional<Filial> filialExistente = filialRepository.findByCnpj(cnpj);
        if (filialExistente.isPresent() && !filialExistente.get().getId().equals(id)) {
            throw new InvalidCnpjException("CNPJ já cadastrado em outra empresa.");
        }
    }
}
