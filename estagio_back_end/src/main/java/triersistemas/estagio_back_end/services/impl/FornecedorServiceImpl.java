package triersistemas.estagio_back_end.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
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
import triersistemas.estagio_back_end.validators.FoneValidator;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final FilialService filialService;
    private final CnpjValidator cnpjValidator;
    private final FoneValidator foneValidator;

    public FornecedorServiceImpl(FornecedorRepository fornecedorRepository, FilialService filialService, CnpjValidator cnpjValidator, FoneValidator foneValidator) {
        this.fornecedorRepository = fornecedorRepository;
        this.filialService = filialService;
        this.cnpjValidator = cnpjValidator;
        this.foneValidator = foneValidator;
    }
    @Override
    public FornecedorResponseDto addFornecedor(FornecedorRequestDto fornecedorDto) {
        Filial filial = filialService.findById(fornecedorDto.filialId());
        validateFornecedor(fornecedorDto);
        var fornecedor = new Fornecedor(fornecedorDto, filial);
        return new FornecedorResponseDto(fornecedorRepository.save(fornecedor));
    }

    @Override
    public FornecedorResponseDto getFornecedorById(Long id) {
        var fornecedor = findById(id);
        return new FornecedorResponseDto(fornecedor);
    }
    @Override
    public List<FornecedorResponseDto> getFornecedorFilter(String nome, Long filialId) {
        return fornecedorRepository.buscarFornecedores(nome, filialId);
    }

    @Override
    public Page<FornecedorResponseDto> getFornecedorPaged(String nome, String cnpj, SituacaoCadastro situacaoCadastro, PageRequest of) {
        return fornecedorRepository.buscarFornecedores(nome,cnpj,situacaoCadastro,of);
    }

    @Override
    public FornecedorResponseDto updateFornecedor(Long id, FornecedorRequestDto fornecedorDto) {
        Fornecedor fornecedor = findById(id);
        var filial = filialService.buscaFilialPorId(fornecedorDto.filialId());
        Optional.ofNullable(fornecedorDto.cnpj()).ifPresent(cnpj->{
            if(!cnpj.equals(fornecedor.getCnpj()))
                cnpjValidator.validateCnpjUpdateFornecedor(cnpj, id);
        });
        Optional.ofNullable(fornecedorDto.telefone()).ifPresent(foneValidator::validateFone);
        fornecedor.alterarDados(fornecedorDto, filial);
        fornecedorRepository.save(fornecedor);
        return new FornecedorResponseDto(fornecedor);
    }

    @Override
    public void alteraSituacao(Long id) {
        Fornecedor fornecedor = findById(id);
        fornecedor.alterarSituacao();
        fornecedorRepository.save(fornecedor);
    }

    private Fornecedor findById(Long id) {
      return fornecedorRepository.findById(id).orElseThrow(() -> new NotFoundException("Fornecedor não encontrado."));
    }

    private void validateFornecedor(FornecedorRequestDto requestDto) {
        cnpjValidator.validateCnpj(requestDto.cnpj());
        cnpjValidator.validateCnpjPostFornecedor(requestDto.cnpj());
        foneValidator.validateFone(requestDto.telefone());
    }
}
