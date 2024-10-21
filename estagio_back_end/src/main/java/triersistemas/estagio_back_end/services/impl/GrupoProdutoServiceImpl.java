package triersistemas.estagio_back_end.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.AtualizaPrecoDto;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepository;
import triersistemas.estagio_back_end.services.AtualizaPrecoService;
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.services.GrupoProdutoService;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoProdutoServiceImpl implements GrupoProdutoService {


    private GrupoProdutoRepository grupoProdutoRepository;
    private FilialService filialService;
    private AtualizaPrecoService atualizaPrecoService;

    @Autowired
    public GrupoProdutoServiceImpl(GrupoProdutoRepository grupoProdutoRepository, FilialService filialService, AtualizaPrecoService atualizaPrecoService) {
        this.grupoProdutoRepository = grupoProdutoRepository;
        this.filialService = filialService;
        this.atualizaPrecoService = atualizaPrecoService;
    }


    @Override
    public GrupoProdutoResponseDto getGrupoProdutoById(Long id) {
        var grupoProduto = grupoProdutoById(id);
        return new GrupoProdutoResponseDto(grupoProduto);
    }

    @Override
    public GrupoProdutoResponseDto addGrupoProduto(GrupoProdutoRequestDto grupoProdutoRequestDto) {
        var filial = filialService.findById(grupoProdutoRequestDto.filialId());
        var grupoProduto = new GrupoProduto(grupoProdutoRequestDto, filial);
        var saved = this.grupoProdutoRepository.save(grupoProduto);
        return new GrupoProdutoResponseDto(saved);
    }

    @Override
    public GrupoProdutoResponseDto updateGrupoProduto(Long id, GrupoProdutoRequestDto grupoProdutoRequestDto) {
        var grupoProduto = grupoProdutoById(id);
        var filial = filialService.buscaFilialPorId(grupoProdutoRequestDto.filialId());
        grupoProduto.alteraGrupoProduto(grupoProdutoRequestDto, filial);
        var saved = this.grupoProdutoRepository.save(grupoProduto);
        return new GrupoProdutoResponseDto(saved);
    }

    @Override
    public GrupoProdutoResponseDto deleteGrupoProdutoById(Long id) {
        var grupoProduto = grupoProdutoById(id);
        this.grupoProdutoRepository.delete(grupoProduto);
        return new GrupoProdutoResponseDto(grupoProduto);

    }

    @Override
    public Page<GrupoProdutoResponseDto> getGrupoProdutoFilter(String nomeGrupo, TipoGrupoProduto tipoGrupo, Long idFilial, Pageable pageable) {
        return grupoProdutoRepository.buscarGrupoProduto(nomeGrupo,tipoGrupo,idFilial,pageable);
    }

    @Override
    public GrupoProdutoResponseDto alteraGrupoProdutoById(Long id, boolean ativar) {
       var grupoProduto =  grupoProdutoById(id);
       if(ativar){
           grupoProduto.setSituacaoCadastro(SituacaoCadastro.ATIVO);
       }else{
           grupoProduto.setSituacaoCadastro(SituacaoCadastro.INATIVO);
       }

        return new GrupoProdutoResponseDto(grupoProduto);
    }

    @Override
    public Optional<GrupoProduto> buscaGrupoProdutoPorId(Long id){
        return this.grupoProdutoRepository.findById(id);
    }

    @Override
    public GrupoProduto grupoProdutoById(Long id){
       return this.buscaGrupoProdutoPorId(id).orElseThrow(() -> new NotFoundException("Grupo de Produto não encontrado"));
    }

    @Override
    public List<GrupoProdutoResponseDto> getAllGrupoProdutoAlteraPreco() {
        return grupoProdutoRepository.getAllGrupoProdutoAlteraPreco();
    }

//    @Override
//    public List<GrupoProdutoResponseDto> alteraPrecoGrupoProduto(AtualizaPrecoDto atualizaProduto) {
//        return atualizaPrecoService.alteraMargemGrupoProduto(atualizaProduto);
//    }

    @Override
    public List<GrupoProdutoResponseDto> getGrupoProdutoFilter(String nomeGrupo, Long filialId) {
        return grupoProdutoRepository.buscarGrupoProduto(nomeGrupo, filialId);
    }



}
