package triersistemas.estagio_back_end.services.impl;

import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.exceptions.InvalidCnpjException;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
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
        validateFilial(requestDto);
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
        Filial filial = getFilialById(id);
         filialRepository.delete(filial);
    }

    @Override
    public Filial getFilialById(Long id) {
        return filialRepository.findById(id).orElseThrow(() -> new NotFoundException("Filial não encontrada"));
    }

    private void validateFilial(FilialRequestDto requestDto){
        if(Utils.validateCnpj(requestDto.cnpj())) {
            if (filialRepository.existsByCnpj(requestDto.cnpj())) {
                throw new InvalidCnpjException("CNPJ já está cadastrado.");
            }
        }
        Utils.validateFone(requestDto.telefone());
    }
}
