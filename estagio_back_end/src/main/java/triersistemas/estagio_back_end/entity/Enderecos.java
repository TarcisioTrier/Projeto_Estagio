package triersistemas.estagio_back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import triersistemas.estagio_back_end.dto.EnderecosDto;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Enderecos")
public class Enderecos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cep", length = 9, nullable = false)
    private String cep;

    @Column(name = "logradouro", nullable = false)
    private String logradouro;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "bairro", nullable = false)
    private String bairro;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "estado", nullable = false)
    private String estado;

    @OneToOne(mappedBy = "endereco")
    private Filial filial;

    public Enderecos(String logradouro, Integer numero, String complemento, String cidade, String estado, String cep, String bairro) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.bairro = bairro;
    }

    public void alterarDados(EnderecosDto dto) {
        Optional.ofNullable(dto.logradouro()).ifPresent(this::setLogradouro);
        Optional.ofNullable(dto.numero()).ifPresent(this::setNumero);
        Optional.ofNullable(dto.complemento()).ifPresent(this::setComplemento);
        Optional.ofNullable(dto.localidade()).ifPresent(this::setCidade);
        Optional.ofNullable(dto.estado()).ifPresent(this::setEstado);
        Optional.ofNullable(dto.cep()).ifPresent(this::setCep);
        Optional.ofNullable(dto.bairro()).ifPresent(this::setBairro);
    }
}