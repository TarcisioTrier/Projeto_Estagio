package triersistemas.estagio_back_end.validators;

import triersistemas.estagio_back_end.exceptions.InvalidFoneException;

import java.util.Objects;

public class Utils {
    public static Boolean isNull(Object o ) {
        return Objects.isNull( o );
    }
}
