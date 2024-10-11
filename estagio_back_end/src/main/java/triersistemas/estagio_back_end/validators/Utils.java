package triersistemas.estagio_back_end.validators;

import triersistemas.estagio_back_end.exceptions.InvalidFoneException;

import java.util.Objects;

public class Utils {
    public static Boolean isNull(Object o ) {
        return Objects.isNull( o );
    }

    public static void validateFone(String fone){
        fone = fone.replaceAll("[^0-9]", "");
        if(fone.length() < 10 || fone.length() > 11){
            throw new InvalidFoneException("Telefone inválido");
        }
    }


}
