package triersistemas.estagio_back_end.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
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
    public FilialResponseDto addFilial(FilialRequestDto requestDto) {
        validateFilial(requestDto);
        cnpjValidator.validateCnpjPostFilial(requestDto.cnpj());
        var filial = new Filial(requestDto);
        if (!Utils.isNull(requestDto.endereco())) {
            var enderecoValido = enderecosValidator.validateEndereco(requestDto.endereco());
            filial.setEndereco(enderecoValido);
        }
        filial = filialRepository.save(filial);
        return new FilialResponseDto(filial);
    }

    @Override
    public FilialResponseDto updateFilial(Long id, FilialRequestDto requestDto) {
        Filial filial = findById(id);

        if (requestDto.cnpj() != null && !requestDto.cnpj().equals(filial.getCnpj())) {
            cnpjValidator.validateCnpjUpdateFilial(requestDto.cnpj(), id);
        }

        if (!Utils.isNull(requestDto)) {
            validateFilial(requestDto);
        }

        if (requestDto.endereco() != null) {
            var enderecoValido = enderecosValidator.validateEndereco(requestDto.endereco());
            filial.setEndereco(enderecoValido);
        }

        filial.alterarDados(requestDto);

        filial = filialRepository.save(filial);

        return new FilialResponseDto(filial);
    }

    @Override
    public void deleteFilial(Long id) {
        Filial filial = findById(id);
        filialRepository.delete(filial);
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

    private void validateFilial(FilialRequestDto requestDto) {
        cnpjValidator.validateCnpj(requestDto.cnpj());
        foneValidator.validateFone(requestDto.telefone());
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

}
