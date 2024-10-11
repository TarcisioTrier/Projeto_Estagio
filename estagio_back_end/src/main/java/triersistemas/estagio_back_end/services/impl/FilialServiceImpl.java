package triersistemas.estagio_back_end.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.services.EnderecoService;
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.utils.CnpjValidator;
import triersistemas.estagio_back_end.utils.Utils;

import java.util.List;
import java.util.Optional;

import static triersistemas.estagio_back_end.utils.Utils.validateFone;

@Service
public class FilialServiceImpl implements FilialService {

    private final FilialRepository filialRepository;
    private final EnderecoService enderecoService;
    private final CnpjValidator cnpjValidator;

    public FilialServiceImpl(FilialRepository filialRepository, EnderecoService enderecoService, CnpjValidator cnpjValidator) {
        this.filialRepository = filialRepository;
        this.enderecoService = enderecoService;
        this.cnpjValidator = cnpjValidator;
    }

    @Override
    public FilialResponseDto addFilial(FilialRequestDto requestDto) {
        validateFilial(requestDto);
        cnpjValidator.validateCnpjPostFilial(requestDto.cnpj());
        var filial = new Filial(requestDto);
        if (!Utils.isNull(requestDto.endereco())) {
            var enderecoValido = enderecoService.validateEndereco(requestDto.endereco());
            filial.setEndereco(enderecoValido);
        }
        filialRepository.save(filial);
        return new FilialResponseDto(filial);
    }

    @Override
    public FilialResponseDto updateFilial(Long id, FilialRequestDto requestDto) {
        Filial filial = findById(id);

        if (requestDto.cnpj() != null && !requestDto.cnpj().equals(filial.getCnpj())) {
            cnpjValidator.validateCnpjUpdateFilial(requestDto.cnpj(), id);
        }

        if (requestDto.telefone() != null) {
            validateFone(requestDto.telefone());
        }

        if (requestDto.endereco() != null) {
            var enderecoValido = enderecoService.validateEndereco(requestDto.endereco());
            filial.setEndereco(enderecoValido);
        }

        filial.alterarDados(requestDto);

        filialRepository.save(filial);

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
        validateFone(requestDto.telefone());
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
