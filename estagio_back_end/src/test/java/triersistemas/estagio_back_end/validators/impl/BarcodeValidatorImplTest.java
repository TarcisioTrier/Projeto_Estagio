package triersistemas.estagio_back_end.validators.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.exceptions.InvalidBarcodeException;
import triersistemas.estagio_back_end.repository.ProdutoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarcodeValidatorImplTest {

    @Mock
    ProdutoRepository produtoRepository;

    @InjectMocks
    BarcodeValidatorImpl barcodeValidator;

    private final Produto produto = new Produto();
    private final Long filialId = 1L;

    @BeforeEach
    void setUp() {
        produto.setId(1L);
        produto.setCodigoBarras("1234567890128");
    }

    @Nested
    class barcodeValidatorTest {

        @Test
        @DisplayName("Deve validar o código de barras ao cadastrar um produto")
        void barcodeValidatorTest_V1() {
            var barcode = "4006381333931";

            doReturn(Optional.empty()).when(produtoRepository).getProdutoByCodigoBarrasAndFilialId(barcode, filialId);

            assertDoesNotThrow(() -> barcodeValidator.validateBarcodePost(barcode, filialId));

            verify(produtoRepository, times(1)).getProdutoByCodigoBarrasAndFilialId(barcode, filialId);
        }

        @Test
        @DisplayName("Deve validar o código de barras ao dar update em um produto")
        void barcodeValidatorTest_V2() {
            var barcode = "4006381333931";
            var produtoId = 2L;

            doReturn(Optional.empty()).when(produtoRepository).getProdutoByCodigoBarrasAndFilialId(barcode, filialId);

            assertDoesNotThrow(() -> barcodeValidator.validateBarcodeUpdate(barcode, produtoId, filialId));

            verify(produtoRepository, times(1)).getProdutoByCodigoBarrasAndFilialId(barcode, filialId);
        }

        @Test
        @DisplayName("Deve validar o código de barras ao dar update em um produto e não alterar o codigo de barras")
        void barcodeValidatorTest_V3() {
            var barcode = "1234567890128";
            var produtoId = 1L;

            doReturn(Optional.of(produto)).when(produtoRepository).getProdutoByCodigoBarrasAndFilialId(barcode, filialId);

            assertDoesNotThrow(() -> barcodeValidator.validateBarcodeUpdate(barcode, produtoId, filialId));

            verify(produtoRepository, times(1)).getProdutoByCodigoBarrasAndFilialId(barcode, filialId);
        }
    }

    @Nested
    class barcodeValidatorExceptionTest {

        @Test
        @DisplayName("Deve lançar InvalidBarcodeException com um código de barras ja existente ao tentar cadastrar um produto")
        void barcodeValidatorExceptionTest_V1() {
            var barcode = "1234567890128";

            doReturn(Optional.of(produto)).when(produtoRepository).getProdutoByCodigoBarrasAndFilialId(barcode, filialId);

            InvalidBarcodeException exception = assertThrows(InvalidBarcodeException.class, () -> {
                barcodeValidator.validateBarcodePost(barcode, filialId);
            });
            assertEquals("Codigo de Barras já cadastrado", exception.getMessage());

            verify(produtoRepository,times(1)).getProdutoByCodigoBarrasAndFilialId(barcode,filialId);
        }

        @Test
        @DisplayName("Deve lançar InvalidBarcodeException com um código de barras ja existente ao tentar dar update em um produto")
        void barcodeValidatorExceptionTest_V2() {
            var barcode = "1234567890128";
            var produtoId = 2L;

            doReturn(Optional.of(produto)).when(produtoRepository).getProdutoByCodigoBarrasAndFilialId(barcode, filialId);

            InvalidBarcodeException exception = assertThrows(InvalidBarcodeException.class, () -> {
                barcodeValidator.validateBarcodeUpdate(barcode,produtoId, filialId);
            });
            assertEquals("Codigo de Barras já cadastrado", exception.getMessage());

            verify(produtoRepository,times(1)).getProdutoByCodigoBarrasAndFilialId(barcode,filialId);
        }

        @Test
        @DisplayName("Deve lançar InvalidBarcodeException com um código de barras inválido")
        void barcodeValidatorExceptionTest_V3() {
            var barcode = "4006381233930";
            var produtoId = 2L;

            InvalidBarcodeException exception = assertThrows(InvalidBarcodeException.class, () -> {
                barcodeValidator.validateBarcodeUpdate(barcode,produtoId, filialId);
            });

            assertEquals("Codigo de Barras inválido", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar InvalidBarcodeException com um código de barras nulo")
        void barcodeValidatorExceptionTest_V4() {

            var produtoId = 2L;

            InvalidBarcodeException exception = assertThrows(InvalidBarcodeException.class, () -> {
                barcodeValidator.validateBarcodeUpdate(null,produtoId, filialId);
            });

            assertEquals("Codigo de Barras inválido", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar InvalidBarcodeException com um código de barras caso tenha letras")
        void barcodeValidatorExceptionTest_V5() {
            var barcode = "1234b67a9c123";
            var produtoId = 2L;

            InvalidBarcodeException exception = assertThrows(InvalidBarcodeException.class, () -> {
                barcodeValidator.validateBarcodeUpdate(barcode,produtoId, filialId);
            });

            assertEquals("Codigo de Barras inválido", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar InvalidBarcodeException com um código de barras diferente de 13 de tamanho")
        void barcodeValidatorExceptionTest_V6() {
            var barcode = "12343127192123";
            var produtoId = 2L;

            InvalidBarcodeException exception = assertThrows(InvalidBarcodeException.class, () -> {
                barcodeValidator.validateBarcodeUpdate(barcode,produtoId, filialId);
            });

            assertEquals("Codigo de Barras inválido", exception.getMessage());
        }
    }

}