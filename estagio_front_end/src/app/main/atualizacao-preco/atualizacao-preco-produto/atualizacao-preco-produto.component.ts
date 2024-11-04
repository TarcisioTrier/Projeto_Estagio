import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import {
  enumToArray,
  TipoProduto,
  AtualizaPrecoEnum,
} from '../../../models/app-enums';
import { AtualizaPreco } from '../../../models/atualizapreco';
import { Produto } from '../../../models/produto';
import { HttpService } from '../../../services/http/http.service';

@Component({
  selector: 'app-atualizacao-preco-produto',
  templateUrl: './atualizacao-preco-produto.component.html',
  styleUrl: './atualizacao-preco-produto.component.scss',
})
export class AtualizacaoPrecoProdutoComponent implements OnInit {
  @Output() close = new EventEmitter();

  atualizarPreco() {
    this.atualizacao.valor = this.valor;
    if (this.selectedProdutos) {
      this.atualizacao.produtoId = this.selectedProdutos.map(
        (produto) => produto.id!
      );
    }
    if (this.atualizacao.all) {
      this.atualizacao.produtoFilter = this.produtoFilter;
    }
    this.close.emit(this.atualizacao);
  }
  selectedProdutos!: Produto[];
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
  totalProdutos!: number;
  rows!: number;
  produtoFilter!: Produto;
  tipoProdutoOptions: any[] = enumToArray(TipoProduto);
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
        this.produtos.forEach((produto) => {
          produto.edit = false;
        });
        this.totalProdutos = data.totalElements;
      },
    });
  }
  ngOnInit(): void {
    const pager = {};
    this.atualizacao.filialId = this.http.filialId();
    this.produtoFilter = {
      filialId: this.http.filialId(),
      filter: new Map<string, string>(),
      disabled: {
        nome: false,
        descricao: false,
      },
    };
    this.http.getProdutoPaged(pager, this.produtoFilter).subscribe((data) => {
      this.totalProdutos = data.totalElements;
      this.produtos = data.content;
      this.produtos.forEach((produto) => {
        produto.edit = false;
      });
      this.rows = data.size;
    });
  }
  constructor(
    private http: HttpService
  ) {}
}

function objectFix(object: any, event: any): any {
  Object.keys(event.filters).forEach((element: string) => {
    object[element] = event.filters[element].value;
    if (
      event.filters[element].value === null ||
      event.filters[element].value === undefined ||
      event.filters[element].value === ''
    )
      return;
    object.filter![element] = event.filters[element].matchMode;
  });
  return object;
}
