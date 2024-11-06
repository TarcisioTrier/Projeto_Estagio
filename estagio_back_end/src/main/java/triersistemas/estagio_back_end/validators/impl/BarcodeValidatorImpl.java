package triersistemas.estagio_back_end.validators.impl;

import org.springframework.stereotype.Component;
import triersistemas.estagio_back_end.exceptions.InvalidBarcodeException;
import triersistemas.estagio_back_end.repository.ProdutoRepository;
import triersistemas.estagio_back_end.validators.BarcodeValidator;

@Component
public class BarcodeValidatorImpl implements BarcodeValidator {

    private final ProdutoRepository repository;

    public BarcodeValidatorImpl(ProdutoRepository repository) {
        this.repository = repository;
    }


    @Override
    public void validateBarcodePost(String barcode, Long filialId) {
        validateBarcode(barcode);
        var produto = this.repository.getProdutoByCodigoBarrasAndFilialId(barcode, filialId);
        produto.ifPresent(p -> {
            throw new InvalidBarcodeException("Codigo de Barras já cadastrado");
        });

    }

    @Override
    public void validateBarcodeUpdate(String barcode, Long produtoId, Long filialId) {
        validateBarcode(barcode);
        var produto = this.repository.getProdutoByCodigoBarrasAndFilialId(barcode, filialId);
        produto.ifPresent(p -> {
            if (!p.getId().equals(produtoId)) {
                throw new InvalidBarcodeException("Codigo de Barras já cadastrado");
            }
        });
    }

    private void validateBarcode(String barcode) {

        if (!isBarcode(barcode)) {
            throw new InvalidBarcodeException("Codigo de Barras inválido");
        }
    }

    private boolean isBarcode(String barcode) {

        if (barcode == null || barcode.length() != 13 || !barcode.matches("\\d+")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(barcode.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int calculatedCheckDigit = (10 - (sum % 10)) % 10;

        int providedCheckDigit = Character.getNumericValue(barcode.charAt(12));
        return calculatedCheckDigit == providedCheckDigit;
    }
}


