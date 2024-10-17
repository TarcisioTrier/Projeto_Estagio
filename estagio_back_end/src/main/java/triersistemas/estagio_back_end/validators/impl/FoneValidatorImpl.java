package triersistemas.estagio_back_end.validators.impl;

import org.springframework.stereotype.Component;
import triersistemas.estagio_back_end.exceptions.InvalidFoneException;
import triersistemas.estagio_back_end.validators.FoneValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FoneValidatorImpl implements FoneValidator {

    private static final String TELEFONE_REGEX = "^[1-9][0-9](?:9[1-9][0-9]{7})$";

    public void validateFone(String telefone) {
        Pattern pattern = Pattern.compile(TELEFONE_REGEX);
        Matcher matcher = pattern.matcher(telefone);

//        if (!matcher.matches()) {
//            throw new InvalidFoneException("Telefone inválido: " + telefone);
//        }
    }
}
