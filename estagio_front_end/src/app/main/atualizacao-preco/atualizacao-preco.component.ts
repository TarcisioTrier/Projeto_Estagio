import { AtualizaPrecoEnum } from './../../models/app-enums';
import { Component, OnInit } from '@angular/core';
import { Produto } from '../../models/produto';
import { GrupoProduto } from '../../models/grupo-produto';
import { HttpService } from '../../services/http/http.service';
import { MessageService } from 'primeng/api';
import {  enumToArray, Filter, TipoGrupo, TipoProduto } from '../../models/app-enums';
import { AtualizaPreco } from '../../models/atualizapreco';
@Component({
  selector: 'app-atualizacao-preco',
  templateUrl: './atualizacao-preco.component.html',
  styleUrl: './atualizacao-preco.component.scss',
})
export class AtualizacaoPrecoComponent implements OnInit {

isRelativo() {
this.atualizacao.isRelativo = !this.atualizacao.isRelativo;
}
isPercentual() {
  this.atualizacao.isPercentual = !this.atualizacao.isPercentual;
}


atualizarPreco() {
  this.atualizacao.valor = this.valor;
  if(this.produtoFilter){
    if(this.selectedProdutos){
      this.atualizacao.produtoId = this.selectedProdutos.map((produto) => produto.id!);
    }
    if(this.atualizacao.all){
      this.atualizacao.produtoFilter = this.produtoFilter;
    }
    console.log(this.atualizacao);
    this.atualizaRequest();
  }else{
    if(this.selectedGrupoProdutos){
    this.atualizacao.grupoProdutoId = this.selectedGrupoProdutos.map((grupoProduto) => grupoProduto.id!);
    }
    if(this.atualizacao.all){
    this.atualizacao.grupoProdutoFilter = this.grupoProdutoFilter;
    }
    console.log(this.atualizacao);
    this.atualizaRequest();
  }

}
  selectedProdutos!: Produto[];
  selectedGrupoProdutos!: GrupoProduto[];
  produtosOptions: any[] = [
    { label: 'Produto', value: true },
    { label: 'Grupo de Produto', value: false },
  ];
  atualizacao: AtualizaPreco = {
    produtoId: [],
    grupoProdutoId: [],
    all: false,
    isProduto: true,
    isRelativo: false,
    isPercentual: false,
  };
  produtos!: Produto[];
  grupoProdutos!: GrupoProduto[];
  totalProdutos!: number;
  rows!: number;
  totalGrupoProdutos!: number;
  grupoProdutoFilter!: GrupoProduto;
  produtoFilter!: Produto;
  tipoProdutoOptions: any[] = enumToArray(TipoProduto);
  tipoGrupoOptions: any[] = enumToArray(TipoGrupo);
  valor!: number;
  tipoAtualizacaoOptions = enumToArray(AtualizaPrecoEnum);
  last: any;

  loadProdutos(event: any) {
    this.produtoFilter = objectFix(this.produtoFilter, event);
    this.produtoFilter.orderer = event.multiSortMeta;
    const page = event.first! / event.rows!;
    this.rows = event.rows!;
    const pager = { page: page, size: this.rows };
    this.http.getProdutoPaged(pager, this.produtoFilter).subscribe({
      next: (data) => {
        this.produtos = data.content;
        this.totalProdutos = data.totalElements;
      }
    });
  }
  loadGrupoProdutos(event: any) {
    this.grupoProdutoFilter = objectFix(this.grupoProdutoFilter, event);
    this.grupoProdutoFilter.orderer = event.multiSortMeta;
    const page = event.first! / event.rows!;
    this.rows = event.rows!;
    const pager = { page: page, size: this.rows };
    this.http.getGrupoProdutoPaged(pager, this.grupoProdutoFilter).subscribe({
      next: (data) => {
        this.grupoProdutos = data.content;
        this.totalGrupoProdutos = data.totalElements;
      }
    });
  }

  ngOnInit(): void {
    const data = sessionStorage.getItem('filial');
    const filial = data ? JSON.parse(data) : undefined;
    const pager = {};
    this.atualizacao.filialId = filial.id!;
    this.grupoProdutoFilter = { filialId: filial.id!,
      filter: new Map<string, string>(),
     };
    this.produtoFilter = {
      filialId: filial.id!,
      filter: new Map<string, string>(),
      disabled: {
        nome: false,
        descricao: false,
      },
    };
    this.http.getProdutoPaged(pager, this.produtoFilter).subscribe((data) => {
      this.totalProdutos = data.totalElements;
      this.produtos = data.content;
      this.rows = data.size;
    });

    this.http
      .getGrupoProdutoPaged(pager, this.grupoProdutoFilter)
      .subscribe((data) => {
        this.totalGrupoProdutos = data.totalElements;
      });
  }

  atualizaRequest(){
    this.http.putAtualizacaoPreco(this.atualizacao).subscribe({
      next: (data) => {
        console.log(data);
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Preço Atualizado',
        });
      },
      error: (erro) => {
        let error = erro.error;
        if (typeof error === 'string') {
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: error,
          });
        } else {
          let erros = Object.values(error);
          for (let erro of erros) {
            this.messageService.add({
              severity: 'error',
              summary: 'Erro',
              detail: String(erro),
            });
          }
        }
      },
    });
  }

  produtoChange(value: number) {
    this.atualizacao.isProduto = value === 0;
  }
  constructor(
    private http: HttpService,
    private messageService: MessageService
  ) {}
}

function objectFix(object: any, event: any): any {
  Object.keys(event.filters).forEach((element: string) => {
    object[element] = event.filters[element].value;
    if(event.filters[element].value === null || event.filters[element].value === undefined || event.filters[element].value === '') return;
    object.filter![element] = event.filters[element].matchMode;
  });
  return object;
}
