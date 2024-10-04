package triersistemas.estagio_back_end.services.impl;

import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.entity.Enderecos;
import triersistemas.estagio_back_end.services.EnderecoService;

@Service
public class EnderecoServiceImpl implements EnderecoService {

    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";

    public Enderecos validateEndereco(EnderecosDto enderecoDto) {

        Enderecos endereco = new Enderecos();
        endereco.setLogradouro(enderecoDto.logradouro());
        endereco.setNumero(enderecoDto.numero());
        endereco.setComplemento(enderecoDto.complemento());
        endereco.setCidade(enderecoDto.cidade());
        endereco.setEstado(enderecoDto.estado());
        endereco.setCep(enderecoDto.cep());
        endereco.setBairro(enderecoDto.bairro());
        return endereco;
    }
}