package triersistemas.estagio_back_end.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.exceptions.InvalidCnpjException;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.services.EnderecoService;
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.utils.Utils;

import java.util.List;
import java.util.Optional;

import static triersistemas.estagio_back_end.utils.Utils.validateCnpj;
import static triersistemas.estagio_back_end.utils.Utils.validateFone;

@Service
public class FilialServiceImpl implements FilialService {

    private final FilialRepository filialRepository;
    private final EnderecoService enderecoService;

    public FilialServiceImpl(FilialRepository filialRepository, EnderecoService enderecoService) {
        this.filialRepository = filialRepository;
        this.enderecoService = enderecoService;
    }

    @Override
    public FilialResponseDto addFilial(FilialRequestDto requestDto) {
        validateFilial(requestDto);
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
        Filial filial = buscaFilialPorId(id).orElseThrow(() -> new NotFoundException("Filial não encontrada"));

        if (requestDto.cnpj() != null && !requestDto.cnpj().equals(filial.getCnpj())) {
            validateCnpjUpdate(requestDto.cnpj(), id);
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
        Filial filial = buscaFilialPorId(id).orElseThrow(() -> new NotFoundException("Filial não encontrada"));
        filialRepository.delete(filial);
    }

    @Override
    public FilialResponseDto getFilialById(Long id) {
        var filial = buscaFilialPorId(id).orElseThrow(() -> new NotFoundException("Filial não encontrada"));
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
        if (validateCnpj(requestDto.cnpj())) {
            if (filialRepository.existsByCnpj(requestDto.cnpj())) {
                throw new InvalidCnpjException("CNPJ já está cadastrado.");
            }
        }
        validateFone(requestDto.telefone());
    }

    private void validateCnpjUpdate(String cnpj, Long id) {
        Optional<Filial> filialExistente = filialRepository.findByCnpj(cnpj);
        if (filialExistente.isPresent() && !filialExistente.get().getId().equals(id)) {
            throw new InvalidCnpjException("CNPJ já cadastrado em outra empresa.");
        }
    }
    @Override
    public Optional<Filial> buscaFilialPorId(Long id) {
        return filialRepository.findById(id);
    }

}
