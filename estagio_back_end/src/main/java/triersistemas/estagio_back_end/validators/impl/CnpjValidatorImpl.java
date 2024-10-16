package triersistemas.estagio_back_end.validators.impl;

import org.springframework.stereotype.Component;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.exceptions.InvalidCnpjException;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.repository.FornecedorRepository;
import triersistemas.estagio_back_end.validators.CnpjValidator;
import triersistemas.estagio_back_end.validators.Utils;

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
        cnpj = limpaCnpj(cnpj);
        if (!isCNPJ(cnpj)) {
            throw new InvalidCnpjException("CNPJ inválido");
        }
    }

    public void validateCnpjUpdateFornecedor(String cnpj, Long id) {
        Optional<Fornecedor> fornecedorExistente = fornecedorRepository.findByCnpj(cnpj);
        //validateCnpjEntityUnque(fornecedorExistente, id, "CNPJ de fornecedor já cadastrado ");
        if (fornecedorExistente.isPresent() && !fornecedorExistente.equals(id)) {
            throw new InvalidCnpjException("CNPJ de fornecedor já cadastrado");
        }
    }

  public void validateCnpjUpdateFilial(String cnpj, Long id) {
    Optional<Filial> filialExistente = filialRepository.findByCnpj(cnpj);
    //validateCnpjEntityUnque(filialExistente, id, "CNPJ já cadastrado em outra filial.");
      if (filialExistente.isPresent() && !filialExistente.equals(id)) {
          throw new InvalidCnpjException("CNPJ de filial já cadastrado");
      }
}

    public void validateCnpjPostFilial(String cnpj) {
        Optional<Filial> filialExistente = filialRepository.findByCnpj(cnpj);
        if (filialExistente.isPresent()) {
            throw new InvalidCnpjException("CNPJ já cadastrado em outra filial.");
        }
    }

//    private void validateCnpjEntityUnque(Optional<?> entidade, Long id, String msg) {
//        if (entidade.isPresent()) {
//            Object obj = entidade.get();
//            Long entityId = null;
//            if (obj instanceof Fornecedor) {
//                entityId = ((Fornecedor) obj).getId();
//            } else if (obj instanceof Filial) {
//                entityId = ((Filial) obj).getId();
//            }
//            if (!Utils.isNull(entityId) && !entityId.equals(id)) {
//                throw new InvalidCnpjException(msg);
//            }
//        }
//    }

    private String limpaCnpj(String cnpj) {
        return cnpj.replaceAll("[^0-9]", "");
    }

    private static boolean isCNPJ(String cnpj) {
        if (cnpj.length() != 14) {
            return false;
        }

        char dig13, dig14;
        int sm, i, r, num, peso;
        sm = 0;
        peso = 2;
        for (i = 11; i >= 0; i--) {
            num = (int) (cnpj.charAt(i) - 48);
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

        sm = 0;
        peso = 2;
        for (i = 12; i >= 0; i--) {
            num = (int) (cnpj.charAt(i) - 48);
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

        if ((dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13))) {
            return true;
        } else {
            return false;
        }
    }
}
