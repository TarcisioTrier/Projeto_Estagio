package triersistemas.estagio_back_end.utils;

import triersistemas.estagio_back_end.exceptions.InvalidCnpjException;
import triersistemas.estagio_back_end.exceptions.InvalidFoneException;

import java.util.Objects;

public class Utils {
    public static Boolean isNull(Object o ) {
        return Objects.isNull( o );
    }

    public static boolean validateCnpj(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");
        if (cnpj.length() != 14) {
            throw new InvalidCnpjException("CNPJ inválido");
        }
        return cnpj.equals(cnpj);
    }

    public static void validateFone(String fone){
        fone = fone.replaceAll("[^0-9]", "");
        if(fone.length() < 10 || fone.length() > 11){
            throw new InvalidFoneException("Telefone inválido");
        }
    }
}
