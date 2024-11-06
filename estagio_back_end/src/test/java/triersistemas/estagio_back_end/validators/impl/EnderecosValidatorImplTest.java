package triersistemas.estagio_back_end.validators.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;
import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.entity.Enderecos;
import triersistemas.estagio_back_end.exceptions.InvalidCepException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnderecosValidatorImplTest {

    @InjectMocks
    private EnderecosValidatorImpl enderecosValidator;
    private EnderecosDto enderecosDto;

    @Nested
    class enderecosValidatorTest{

        @Test
        @DisplayName("Deve retornar um endereço valido")
        void enderecosValidatorTest_V1() {
            enderecosDto = createEnderecosDto("Quadra 12",null,null,"Águas Lindas de Goiás","Goiás","72926-618","Mansões Savana");
            var expected = new Enderecos(enderecosDto);

            var actual = assertDoesNotThrow(() -> enderecosValidator.validateEndereco(enderecosDto));
            assertEquals(expected,actual);
        }
    }

    @Nested
    class enderecosValidatorExceptionTest{

        @Test
        @DisplayName("Deve lançar InvalidCepException quando o cep for inválido")
        void enderecosValidatorExceptionTest_V1() {
            enderecosDto = createEnderecosDto("Quadra 12",null,null,"Águas Lindas de Goiás","Goiás","729262618","Mansões Savana");

            InvalidCepException exception = assertThrows(InvalidCepException.class, () ->{
                enderecosValidator.validateEndereco(enderecosDto);
            });
            assertEquals("CEP inválido.", exception.getMessage());
        }
    }

    private EnderecosDto createEnderecosDto(String logradouro,Integer numero, String complemento, String localidade, String estado, String cep, String bairro){
       return new EnderecosDto(
                logradouro,
                numero,
                complemento,
                localidade,
                estado,
                cep,
                bairro
        );
    }
}