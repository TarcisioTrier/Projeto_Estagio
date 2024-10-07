package triersistemas.estagio_back_end.services.impl;

import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.entity.Enderecos;
import triersistemas.estagio_back_end.exceptions.InvalidCepException;
import triersistemas.estagio_back_end.services.EnderecoService;

@Service
public class EnderecoServiceImpl implements EnderecoService {

    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";

    public Enderecos validateEndereco(EnderecosDto enderecoDto) {

        validateCep(enderecoDto.cep());

        Enderecos endereco = new Enderecos();
        endereco.setLogradouro(enderecoDto.logradouro());
        endereco.setNumero(enderecoDto.numero());
        endereco.setComplemento(enderecoDto.complemento());
        endereco.setCidade(enderecoDto.localidade());
        endereco.setEstado(enderecoDto.estado());
        endereco.setCep(enderecoDto.cep());
        endereco.setBairro(enderecoDto.bairro());
        return endereco;
    }

    private void validateCep(String cep) {
        cep = cep.replaceAll("[^0-9]", "");
        if (cep.length() != 8) {
            throw new InvalidCepException("CEP inválido");
        }
    }
}