package triersistemas.estagio_back_end.validators;

import org.springframework.stereotype.Component;


public interface BarcodeValidator {

    void validateBarcodePost(String barcode, Long filialId);

    void validateBarcodeUpdate(String barcode, Long produtoId, Long filialId);
}
