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
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.utils.Utils;

import java.util.Optional;

import static triersistemas.estagio_back_end.utils.Utils.validateCnpj;
import static triersistemas.estagio_back_end.utils.Utils.validateFone;

@Service
public class FilialServiceImpl implements FilialService {

    private final FilialRepository filialRepository;
    private final EnderecoServiceImpl enderecoService;

    public FilialServiceImpl(FilialRepository filialRepository, EnderecoServiceImpl enderecoService) {
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
        Filial filial = getFilialById(id);

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
        Filial filial = getFilialById(id);
        filialRepository.delete(filial);
    }

    @Override
    public Filial getFilialById(Long id) {
        return filialRepository.findById(id).orElseThrow(() -> new NotFoundException("Filial não encontrada"));
    }

    @Override
    public Filial getAllFiliais() {
        return null;
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
}
