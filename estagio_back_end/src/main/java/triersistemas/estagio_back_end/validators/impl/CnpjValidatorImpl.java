package triersistemas.estagio_back_end.validators.impl;

import org.springframework.stereotype.Component;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.exceptions.InvalidCnpjException;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.repository.FornecedorRepository;
import triersistemas.estagio_back_end.validators.CnpjValidator;

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
        if (!isCNPJ(cnpj)) {
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

    public void validateCnpjPostFilial(String cnpj) {
        Optional<Filial> filialExistente = filialRepository.findByCnpj(cnpj);
        if (filialExistente.isPresent()) {
            throw new InvalidCnpjException("CNPJ já cadastrado em outra empresa.");
        }
    }

    private static boolean isCNPJ(String CNPJ) {

        if (CNPJ.length() != 14) {
            return false;
        }


        char dig13, dig14;
        int sm, i, r, num, peso;
        sm = 0;
        peso = 2;
        for (i = 11; i >= 0; i--) {
            num = (int) (CNPJ.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso + 1;
            if (peso == 10)
                peso = 2;
        }
        r = sm % 11;
        if ((r == 0) || (r == 1)) {
            dig13 = '0';
        } else {
            dig13 = (char) ((11 - r) + 48);
        }

        // Cálculo do segundo dígito verificador
        sm = 0;
        peso = 2;
        for (i = 12; i >= 0; i--) {
            num = (int) (CNPJ.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso + 1;
            if (peso == 10)
                peso = 2;
        }

        r = sm % 11;
        if ((r == 0) || (r == 1)) {
            dig14 = '0';
        } else {
            dig14 = (char) ((11 - r) + 48);
        }


        // Verifica se os dígitos calculados conferem com os dígitos informados
        if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13))) {
            return true;
        } else {
            return false;
        }
    }

}
