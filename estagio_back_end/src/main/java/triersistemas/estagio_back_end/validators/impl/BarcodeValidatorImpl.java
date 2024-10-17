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
        var produto = this.repository.getProdutoByCodigoBarrasAndFilialId(barcode, filialId);
        if (produto.isPresent()) {
            throw new InvalidBarcodeException("Codigo de Barras já cadastrado");
        }

    }

    @Override
    public void validateBarcodeUpdate(String barcode, Long produtoId, Long filialId) {
        var produto = this.repository.getProdutoByCodigoBarrasAndFilialId(barcode, filialId);
        if (produto.isPresent() && !produtoId.equals(produto.get().getId())) {
            throw new InvalidBarcodeException("Codigo de Barras já cadastrado");
        }
    }
}
