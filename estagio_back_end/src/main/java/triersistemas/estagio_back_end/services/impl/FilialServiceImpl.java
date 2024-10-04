package triersistemas.estagio_back_end.services.impl;

import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.FilialRequestDto;
import triersistemas.estagio_back_end.dto.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.utils.Utils;

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
        var filial = new Filial(requestDto);
        if(!Utils.isNull(requestDto.endereco())) {
            var enderecoValido = enderecoService.validateEndereco(requestDto.endereco());
            filial.setEndereco(enderecoValido);
        }
        filialRepository.save(filial);
        return new FilialResponseDto(filial);
    }

    @Override
    public FilialResponseDto updateFilial(Long id, FilialRequestDto requestDto) {
        return null;
    }

    @Override
    public void deleteFilial(Long id) {

    }

    @Override
    public FilialResponseDto getFilialById(Long id) {
        return null;
    }

}
