package triersistemas.estagio_back_end.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.request.FornecedorRequestDto;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.FornecedorRepository;
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.services.FornecedorService;
import triersistemas.estagio_back_end.validators.CnpjValidator;
import triersistemas.estagio_back_end.validators.Utils;

import static triersistemas.estagio_back_end.validators.Utils.validateFone;

@Service
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final FilialService filialService;
    private final CnpjValidator cnpjValidator;

    public FornecedorServiceImpl(FornecedorRepository fornecedorRepository, FilialService filialService, CnpjValidator cnpjValidator) {
        this.fornecedorRepository = fornecedorRepository;
        this.filialService = filialService;
        this.cnpjValidator = cnpjValidator;
    }
    @Override
    public FornecedorResponseDto addFornecedor(FornecedorRequestDto requestDto) {
        Filial filial = filialService.findById(requestDto.filialId());
        validateFornecedor(requestDto);
        if(Utils.isNull(filial)){
            throw new NotFoundException("Filial não encontrada.");
        }
        var fornecedor = new Fornecedor(requestDto, filial);
        return new FornecedorResponseDto(fornecedorRepository.save(fornecedor));
    }

    @Override
    public FornecedorResponseDto getFornecedorById(Long id) {
        var fornecedor = findById(id);
        return new FornecedorResponseDto(fornecedor);
    }

    @Override
    public Page<FornecedorResponseDto> getFornecedorFilter(String nome, String cnpj, SituacaoCadastro situacaoCadastro, PageRequest of) {
        return fornecedorRepository.buscarFornecedores(nome,cnpj,situacaoCadastro,of);
    }

    @Override
    public FornecedorResponseDto updateFornecedor(Long id, FornecedorRequestDto requestDto) {
        Fornecedor fornecedor = findById(id);
        Filial filial = null;

        if (requestDto.filialId() != null) {
            filial = filialService.findById(requestDto.filialId());
        }
        if (requestDto.cnpj() != null && !requestDto.cnpj().equals(fornecedor.getCnpj())) {
            cnpjValidator.validateCnpjUpdateFornecedor(requestDto.cnpj(), id);
        }

        if (requestDto.telefone() != null) {
            validateFone(requestDto.telefone());
        }

        fornecedor.alterarDados(requestDto, filial);
        fornecedorRepository.save(fornecedor);
        return new FornecedorResponseDto(fornecedor);
    }


    @Override
    public void alteraSituacao(Long id) {
        Fornecedor fornecedor = findById(id);
        fornecedor.alterarSituacao();
        fornecedorRepository.save(fornecedor);
    }

    private void validateFornecedor(FornecedorRequestDto requestDto) {
        cnpjValidator.validateCnpj(requestDto.cnpj());
        validateFone(requestDto.telefone());
    }

    private Fornecedor findById(Long id) {
      return fornecedorRepository.findById(id).orElseThrow(() -> new NotFoundException("Fornecedor não encontrado."));
    }
}
