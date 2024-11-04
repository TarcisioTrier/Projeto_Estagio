import { Component } from '@angular/core';
import { objectFix } from '../../../../models/externalapi';
import { HttpService } from '../../../../services/http/http.service';
import { MessageHandleService } from '../../../../services/message-handle.service';
import { Produto } from '../../../../models/produto';
import {
  Apresentacao,
  enumToArray,
  TipoProduto,
} from '../../../../models/app-enums';

@Component({
  selector: 'app-dashboard-table-produto',
  templateUrl: './dashboard-table-produto.component.html',
  styleUrl: './dashboard-table-produto.component.scss',
})
export class DashboardTableProdutoComponent {
  isNumber(item: any): boolean {
    return this.isMoney(item) || this.isPercent(item);
  }
  enumOptions(item: any): any[] | undefined {
    if (item.field === 'tipoProduto') {
      return enumToArray(TipoProduto);
    }
    if (item.field === 'apresentacao') {
      return enumToArray(Apresentacao);
    }
    return undefined;
  }
  isEnum(item: any): boolean {
    return item.field === 'tipoProduto' || item.field === 'apresentacao';
  }
  isMoney(item: any): boolean {
    return item.field === 'valorProduto' || item.field === 'valorVenda';
  }
  isPercent(item: any): boolean {
    return item.field === 'margemLucro';
  }
  isBool(item: any): boolean {
    return item.field === 'atualizaPreco';
  }

  editGrupoProduto(item: any) {
    item.edit = true;
  }
  change(produto: Produto) {
    this.http.editarProduto(produto).subscribe({
      next: (retorno) => {
        this.messageHandler.showCadastroMessage(retorno);
      },
      error: (erro) => {
        this.messageHandler.showErrorMessage(erro);
      },
    });
    this.produtos.forEach((produto) => {
      produto.edit = false;
    });
  }

  totalProdutos!: number;
  cols = [
    { field: 'codigoBarras', header: 'Codigo de Barras' },
    { field: 'nome', header: 'Nome' },
    { field: 'descricao', header: 'Descrição' },
    { field: 'tipoProduto', header: 'Tipo de Produto' },
    { field: 'apresentacao', header: 'Apresentação' },
    { field: 'margemLucro', header: 'Margem de Lucro' },
    { field: 'atualizaPreco', header: 'Atualização de Preço' },
    { field: 'valorProduto', header: 'Valor do Produto' },
    { field: 'valorVenda', header: 'Valor de Venda' },
  ];

  selectedColumns = [
    { field: 'codigoBarras', header: 'Codigo de Barras' },
    { field: 'nome', header: 'Nome' },
    { field: 'descricao', header: 'Descrição' },
    { field: 'tipoProduto', header: 'Tipo de Produto' },
    { field: 'apresentacao', header: 'Apresentação' },
    { field: 'margemLucro', header: 'Margem de Lucro' },
    { field: 'atualizaPreco', header: 'Atualização de Preço' },
    { field: 'valorProduto', header: 'Valor do Produto' },
    { field: 'valorVenda', header: 'Valor de Venda' },
  ];
  load(event: any) {
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
      error: (erro) => {
        this.messageHandler.showErrorMessage(erro);
      },
    });
  }
  produtos!: Produto[];
  produtoFilter: Produto = {
    filter: new Map<string, string>(),
    disabled: {
      nome: false,
      descricao: false,
    },
  };
  rows: number = 10;
  constructor(
    private http: HttpService,
    private messageHandler: MessageHandleService
  ) {}
}
