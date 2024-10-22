package triersistemas.estagio_back_end.validatorsTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.exceptions.InvalidCnpjException;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.repository.FornecedorRepository;
import triersistemas.estagio_back_end.validators.impl.CnpjValidatorImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CnpjValidatorTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private FilialRepository filialRepository;

    @InjectMocks
    private CnpjValidatorImpl cnpjValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Injetando mocks
    }

    @Test
    void validateCnpj_ValidCnpj_NotThrowException() {
        String validCnpj = "12.345.678/0001-95";  // CNPJ válido

        assertDoesNotThrow(() -> cnpjValidator.validateCnpj(validCnpj));  // Testando a lógica real
    }

    @Test
    void validateCnpj_InvalidCnpj_ThrowInvalidCnpjException() {
        String invalidCnpj = "123456789012";  // CNPJ inválido (menos caracteres)

        assertThrows(InvalidCnpjException.class, () -> cnpjValidator.validateCnpj(invalidCnpj));
    }

    @Test
    void validateCnpj_EmptyCnpj_ThrowInvalidCnpjException() {
        String emptyCnpj = "";  // CNPJ vazio

        assertThrows(InvalidCnpjException.class, () -> cnpjValidator.validateCnpj(emptyCnpj));
    }

    @Test
    void validateCnpj_InvalidFormatCnpj_ThrowInvalidCnpjException() {
        String invalidFormatCnpj = "12.345.678/0001-9";  // CNPJ com formato incorreto

        assertThrows(InvalidCnpjException.class, () -> cnpjValidator.validateCnpj(invalidFormatCnpj));
    }

    @Test
    void validateCnpj_SpacesAndCharacters_ThrowInvalidCnpjException() {
        String cnpjWithSpaces = " 12345678901234 ";  // CNPJ válido, mas com espaços
        String cnpjWithSpecialChars = "12.345.678/0001-95";  // CNPJ com caracteres especiais

        assertThrows(InvalidCnpjException.class, () -> cnpjValidator.validateCnpj(cnpjWithSpaces));
    }

    @Test
    void validateCnpjUpdateFornecedor_ValidCnpj_NotThrowException() {
        String validCnpj = "12345678901234";  // CNPJ válido
        Long id = 1L;

        assertDoesNotThrow(() -> cnpjValidator.validateCnpjUpdateFornecedor(validCnpj, id));
    }

    @Test
    void validateCnpjUpdateFornecedor_CnpjAlreadyExists_ThrowInvalidCnpjException() {
        String existingCnpj = "12345678000194";
        Long id = 1L;
        var filial = new Filial();
        filial.setId(id);
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpj(existingCnpj);
        fornecedor.setId(2L);
        fornecedorRepository.save(fornecedor);
        doReturn(Optional.of(fornecedor)).when(fornecedorRepository).findByCnpj(existingCnpj);

        assertThrows(InvalidCnpjException.class, () -> cnpjValidator.validateCnpjUpdateFornecedor(existingCnpj, id));
    }

    @Test
    void validateCnpjUpdateFilial_ValidCnpj_NotThrowException() {
        String validCnpj = "12345678901234";
        Long id = 1L;

        assertDoesNotThrow(() -> cnpjValidator.validateCnpjUpdateFilial(validCnpj, id));

    }

    @Test
    void validateCnpjUpdateFilial_CnpjAlreadyExists_ThrowInvalidCnpjException() {
        String existingCnpj = "12345678000195";
        Long id = 1L;

        Filial filialExistente = new Filial();
        filialExistente.setEndereco(null);
        filialExistente.setId(2L);

        assertThrows(InvalidCnpjException.class, () -> cnpjValidator.validateCnpjUpdateFilial(existingCnpj, id));
        when(filialRepository.findByCnpj(existingCnpj)).thenReturn(Optional.of(filialExistente));


    }

    @Test
    void validateCnpjPostFilial_ValidCnpj_NotThrowException() {
        String validCnpj = "12345678901234";

        assertDoesNotThrow(() -> cnpjValidator.validateCnpjPostFilial(validCnpj));
    }

    @Test
    void validateCnpjPostFilial_CnpjAlreadyExists_ThrowInvalidCnpjException() {
        String existingCnpj = "12345678901234";

        assertThrows(InvalidCnpjException.class, () -> cnpjValidator.validateCnpjPostFilial(existingCnpj));
    }
}
