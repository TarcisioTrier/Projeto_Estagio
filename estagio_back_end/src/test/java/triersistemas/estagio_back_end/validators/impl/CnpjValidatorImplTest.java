package triersistemas.estagio_back_end.validators.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.exceptions.InvalidCnpjException;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.repository.FornecedorRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CnpjValidatorImplTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private FilialRepository filialRepository;

    @InjectMocks
    private CnpjValidatorImpl cnpjValidator;

    private final Filial filial = new Filial();
    private final Fornecedor fornecedor = new Fornecedor();

    @BeforeEach
    void setUp() {
        filial.setCnpj("52.353.295/0001-83");
        filial.setId(1L);

        fornecedor.setCnpj("10.333.032/0001-62");
        fornecedor.setId(1L);
    }

    @Nested
    class cnpjValidatorTest{

        @Test
        @DisplayName("Não deve lançar InvalidCnpjException ao não ter uma filial com o cnpj no post")
        void cnpjValidatorTest_V1() {
            var cnpj = "30.860.615/0001-59";

            assertDoesNotThrow(() -> cnpjValidator.validateCnpjPostFilial(cnpj));
        }

        @Test
        @DisplayName("Não deve lançar InvalidCnpjException ao não ter uma filial com o cnpj no update")
        void cnpjValidatorTest_V2() {
            var cnpj = "30.860.615/0001-59";
            var filialId = 1L;

            doReturn(Optional.empty()).when(filialRepository).findByCnpj(cnpj);
            assertDoesNotThrow(() -> cnpjValidator.validateCnpjUpdateFilial(cnpj, filialId));

            verify(filialRepository,times(1)).findByCnpj(cnpj);
        }

        @Test
        @DisplayName("Não deve lançar InvalidCnpjException ao não ter uma filial com o cnpj não alterado no update")
        void cnpjValidatorTest_V3() {
            var cnpj = "52.353.295/0001-83";
            var filialId = 1L;

            doReturn(Optional.of(filial)).when(filialRepository).findByCnpj(cnpj);
            assertDoesNotThrow(() -> cnpjValidator.validateCnpjUpdateFilial(cnpj, filialId));

            verify(filialRepository,times(1)).findByCnpj(cnpj);
        }

        @Test
        @DisplayName("Não deve lançar InvalidCnpjException ao não ter um fornecedor com o cnpj no post")
        void cnpjValidatorTest_V4() {
            var cnpj = "30.860.615/0001-59";

            doReturn(Optional.empty()).when(fornecedorRepository).findByCnpj(cnpj);

            assertDoesNotThrow(() -> cnpjValidator.validateCnpjPostFornecedor(cnpj));

            verify(fornecedorRepository,times(1)).findByCnpj(cnpj);
        }

        @Test
        @DisplayName("Não deve lançar InvalidCnpjException ao não ter um fornecedor com o cnpj no update")
        void cnpjValidatorTest_V5() {
            var cnpj = "30.860.615/0001-59";
            var fornecedorId = 1L;

            doReturn(Optional.empty()).when(fornecedorRepository).findByCnpj(cnpj);

            assertDoesNotThrow(() -> cnpjValidator.validateCnpjUpdateFornecedor(cnpj, fornecedorId));

            verify(fornecedorRepository,times(1)).findByCnpj(cnpj);
        }

        @Test
        @DisplayName("Não deve lançar InvalidCnpjException ao não ter um fornecedor com o cnpj não alterado no update")
        void cnpjValidatorTest_V6() {
            var cnpj = "30.860.615/0001-59";
            var fornecedorId = 1L;

            doReturn(Optional.of(fornecedor)).when(fornecedorRepository).findByCnpj(cnpj);

            assertDoesNotThrow(() -> cnpjValidator.validateCnpjUpdateFornecedor(cnpj, fornecedorId));

            verify(fornecedorRepository,times(1)).findByCnpj(cnpj);
        }
    }
    @Nested
    class cnpjValidatorExceptionTest{

        @Test
        @DisplayName("Deve lançar InvalidCnpjException ao ter uma filial com o mesmo cnpj no post")
        void cnpjValidatorTest_V1() {
            var cnpj = "52.353.295/0001-83";

            doReturn(Optional.of(filial)).when(filialRepository).findByCnpj(cnpj);

            InvalidCnpjException exception = assertThrows(InvalidCnpjException.class,() ->{
                cnpjValidator.validateCnpjPostFilial(cnpj);
            });

            assertEquals("CNPJ já cadastrado em outra empresa.", exception.getMessage());
            verify(filialRepository,times(1)).findByCnpj(cnpj);
        }

        @Test
        @DisplayName("Deve lançar InvalidCnpjException ao ter uma filial com o mesmo cnpj no update")
        void cnpjValidatorTest_V2() {
            var cnpj = "52.353.295/0001-83";
            var filialId = 2L;

            doReturn(Optional.of(filial)).when(filialRepository).findByCnpj(cnpj);
            InvalidCnpjException exception = assertThrows(InvalidCnpjException.class,() ->{
                cnpjValidator.validateCnpjUpdateFilial(cnpj, filialId);
            });

            assertEquals("CNPJ já cadastrado em outra empresa.", exception.getMessage());
            verify(filialRepository,times(1)).findByCnpj(cnpj);
        }

        @Test
        @DisplayName("Deve lançar InvalidCnpjException ao ter um fornecedor com o mesmo cnpj no post")
        void cnpjValidatorTest_V3() {
            var cnpj = "10.333.032/0001-62";

            doReturn(Optional.of(fornecedor)).when(fornecedorRepository).findByCnpj(cnpj);
            InvalidCnpjException exception = assertThrows(InvalidCnpjException.class,() ->{
                cnpjValidator.validateCnpjPostFornecedor(cnpj);
            });

            assertEquals("CNPJ de fornecedor já cadastrado nesta empresa.", exception.getMessage());
            verify(fornecedorRepository,times(1)).findByCnpj(cnpj);
        }

        @Test
        @DisplayName("Deve lançar InvalidCnpjException ao ter uma filial com o mesmo cnpj no update")
        void cnpjValidatorTest_V4() {
            var cnpj = "52.353.295/0001-83";
            var fornecedorId = 2L;

            doReturn(Optional.of(fornecedor)).when(fornecedorRepository).findByCnpj(cnpj);
            InvalidCnpjException exception = assertThrows(InvalidCnpjException.class,() ->{
                cnpjValidator.validateCnpjUpdateFornecedor(cnpj, fornecedorId);
            });

            assertEquals("CNPJ de fornecedor já cadastrado nesta empresa.", exception.getMessage());
            verify(fornecedorRepository,times(1)).findByCnpj(cnpj);
        }

        @Test
        @DisplayName("Deve lançar InvalidCnpjException ao ter um cnpj inválido")
        void cnpjValidatorTest_V5() {
            var cnpj = "04.252.011/0001-89";

            InvalidCnpjException exception = assertThrows(InvalidCnpjException.class,() ->{
                cnpjValidator.validateCnpjPostFilial(cnpj);
            });

            assertEquals("CNPJ inválido.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar InvalidCnpjException ao ter um cnpj com numeros inválidos")
        void cnpjValidatorTest_V6() {
            var cnpj = "04.252.011/0001-89";

            InvalidCnpjException exception = assertThrows(InvalidCnpjException.class,() ->{
                cnpjValidator.validateCnpjPostFilial(cnpj);
            });

            assertEquals("CNPJ inválido.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar InvalidCnpjException ao ter um cnpj inválido")
        void cnpjValidatorTest_V7() {
            var cnpj = "cnpj inválido";

            InvalidCnpjException exception = assertThrows(InvalidCnpjException.class,() ->{
                cnpjValidator.validateCnpjPostFilial(cnpj);
            });

            assertEquals("CNPJ inválido.", exception.getMessage());
        }
    }

}