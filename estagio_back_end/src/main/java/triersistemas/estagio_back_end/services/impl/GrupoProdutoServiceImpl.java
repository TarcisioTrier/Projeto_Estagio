package triersistemas.estagio_back_end.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoChartDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepository;
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.services.GrupoProdutoService;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoProdutoServiceImpl implements GrupoProdutoService {


    private final GrupoProdutoRepository grupoProdutoRepository;
    private final FilialService filialService;


    public GrupoProdutoServiceImpl(GrupoProdutoRepository grupoProdutoRepository, FilialService filialService) {
        this.grupoProdutoRepository = grupoProdutoRepository;
        this.filialService = filialService;
    }


    @Override
    public GrupoProdutoResponseDto getGrupoProdutoById(Long id) {
        var grupoProduto = findById(id);
        return new GrupoProdutoResponseDto(grupoProduto);
    }

    @Override
    public GrupoProdutoResponseDto addGrupoProduto(GrupoProdutoRequestDto grupoProdutoDto) {
        var filial = filialService.findById(grupoProdutoDto.filialId());
        var grupoProduto = new GrupoProduto(grupoProdutoDto, filial);
        var saved = this.grupoProdutoRepository.save(grupoProduto);
        return new GrupoProdutoResponseDto(saved);
    }

    @Override
    public GrupoProdutoResponseDto updateGrupoProduto(Long id, GrupoProdutoRequestDto grupoProdutoDto) {
        var grupoProduto = findById(id);
        var filial = filialService.buscaFilialPorId(grupoProdutoDto.filialId());
        grupoProduto.alteraGrupoProduto(grupoProdutoDto, filial);
        var saved = this.grupoProdutoRepository.save(grupoProduto);
        return new GrupoProdutoResponseDto(saved);
    }

    @Override
    public GrupoProdutoResponseDto deleteGrupoProduto(Long id) {
        var grupoProduto = findById(id);
        this.grupoProdutoRepository.delete(grupoProduto);
        return new GrupoProdutoResponseDto(grupoProduto);

    }

    @Override
    public Page<GrupoProdutoResponseDto> getGrupoProdutoPaged(GrupoProdutoPagedRequestDto grupoProdutoDto, Long idFilial, Pageable pageable) {
        return grupoProdutoRepository.buscarGrupoProduto(grupoProdutoDto, idFilial, pageable);
    }

    @Override
    public List<GrupoProdutoChartDto> getProdutos(Long id) {
        var filial = filialService.findById(id);
        List<GrupoProduto> gruposProduto = filial.getGrupoProdutos();
        return gruposProduto.stream().map(GrupoProdutoChartDto::new).toList();
    }

    @Override
    public GrupoProdutoResponseDto removeGrupoProduto(Long id) {
       var grupoProduto =  grupoProdutoById(id);
       grupoProduto.setSituacaoCadastro(SituacaoCadastro.INATIVO);
        return new GrupoProdutoResponseDto(grupoProduto);
    }

    @Override
    public Optional<GrupoProduto> buscaGrupoProdutoPorId(Long id) {
        return this.grupoProdutoRepository.findById(id);
    }

    @Override
    public GrupoProduto findById(Long id) {
        return this.buscaGrupoProdutoPorId(id).orElseThrow(() -> new NotFoundException("Grupo de Produto não encontrado"));
    }

    @Override
    public List<GrupoProdutoResponseDto> getGrupoProdutoFilter(String nomeGrupo, Long filialId) {
        return grupoProdutoRepository.buscarGrupoProduto(nomeGrupo, filialId);
    }


}
