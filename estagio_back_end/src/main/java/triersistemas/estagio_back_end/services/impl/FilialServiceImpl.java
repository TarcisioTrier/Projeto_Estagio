package triersistemas.estagio_back_end.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.enuns.SituacaoContrato;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.validators.CnpjValidator;
import triersistemas.estagio_back_end.validators.EnderecosValidator;
import triersistemas.estagio_back_end.validators.FoneValidator;
import triersistemas.estagio_back_end.validators.Utils;

import java.util.List;
import java.util.Optional;


@Service
public class FilialServiceImpl implements FilialService {

    private final FilialRepository filialRepository;
    private final EnderecosValidator enderecosValidator;
    private final CnpjValidator cnpjValidator;
    private final FoneValidator foneValidator;

    @Autowired
    public FilialServiceImpl(FilialRepository filialRepository,
                             EnderecosValidator enderecosValidator,
                             CnpjValidator cnpjValidator,
                             FoneValidator foneValidator) {
        this.filialRepository = filialRepository;
        this.enderecosValidator = enderecosValidator;
        this.cnpjValidator = cnpjValidator;
        this.foneValidator = foneValidator;
    }

    @Override
    public FilialResponseDto addFilial(FilialRequestDto filialDto) {
        validateFilial(filialDto);
        cnpjValidator.validateCnpjPostFilial(filialDto.cnpj());
        var filial = new Filial(filialDto);
        Optional.ofNullable(filialDto.endereco()).map(enderecosValidator::validateEndereco).ifPresent(filial::setEndereco);
        var saved = filialRepository.save(filial);
        return new FilialResponseDto(saved);
    }

    @Override
    public FilialResponseDto updateFilial(Long id, FilialRequestDto filialDto) {
        Filial filial = findById(id);
        Optional.ofNullable(filialDto).ifPresent(this::validateFilial);
        Optional.ofNullable(filialDto.cnpj()).ifPresent(cnpj->{
                    if(!cnpj.equals(filial.getCnpj()))
                        cnpjValidator.validateCnpjUpdateFilial(cnpj, filial.getId());
        });
        Optional.ofNullable(filialDto.endereco()).map(enderecosValidator::validateEndereco).ifPresent(filial::setEndereco);
        filial.alterarDados(filialDto);
        var saved = filialRepository.save(filial);

        return new FilialResponseDto(saved);
    }

    @Override
    public FilialResponseDto deleteFilial(Long id) {
        var filial = findById(id);
        filialRepository.delete(filial);
        return new FilialResponseDto(filial);
    }

    @Override
    public FilialResponseDto getFilialById(Long id) {
        var filial = findById(id);
        return new FilialResponseDto(filial);
    }

    @Override
    public List<FilialResponseDto> getAllFiliais() {
        var filial = filialRepository.findAll();
        return filial.stream().map(FilialResponseDto::new).toList();
    }

    @Override
    public Page<FilialResponseDto> getFilialFilter(String nome, String cnpj, Pageable pageable) {
        return filialRepository.buscarFiliais(nome, cnpj, pageable);
    }

    @Override
    public Optional<Filial> buscaFilialPorId(Long id) {
        return filialRepository.findById(id);
    }

    @Override
    public Filial findById(Long id) {
        return buscaFilialPorId(id).orElseThrow(() -> new NotFoundException("Filial não Encontrada"));
    }

    @Override
    public List<FilialResponseDto> getFilialFilter(String nome) {
        return filialRepository.buscarFiliais(nome);
    }

    @Override
    public FilialResponseDto removeFilial(Long id) {
        var filial = findById(id);
        filial.setSituacaoContrato(SituacaoContrato.INATIVO);
        var saved = filialRepository.save(filial);
        return new FilialResponseDto(saved);
    }

    private void validateFilial(FilialRequestDto requestDto) {
        cnpjValidator.validateCnpj(requestDto.cnpj());
        foneValidator.validateFone(requestDto.telefone());
    }

}
