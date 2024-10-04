package triersistemas.estagio_back_end.utils;

import java.util.Objects;

public class Utils {
    public static Boolean isNull(Object o ) {
        return Objects.isNull( o );
    }

    public static String stringFormat(String s) {
        return s.replaceAll("[^0-9]", "");
    }
}
