import { Component, OnInit } from '@angular/core';
import { Produto } from '../../models/produto';
import { GrupoProduto } from '../../models/grupo-produto';
import { HttpService } from '../../services/http/http.service';
import { Filial } from '../../models/filial';
import { LazyLoadEvent, MessageService, SortEvent } from 'primeng/api';
import { TableLazyLoadEvent } from 'primeng/table';
import { Apresentacao, Filter, Order, TipoProduto } from '../../models/app-enums';
import { PaginatorState } from 'primeng/paginator';

@Component({
  selector: 'app-atualizacao-preco',
  templateUrl: './atualizacao-preco.component.html',
  styleUrl: './atualizacao-preco.component.scss',
})
export class AtualizacaoPrecoComponent implements OnInit {
  selectedProdutos!: Produto[];
  selectedGrupoProdutos!: GrupoProduto[];
  produtosOptions: any[] = [
    { label: 'Produto', value: true },
    { label: 'Grupo de Produto', value: false },
  ];
  isProduto = true;
  produtos!: Produto[];
  grupoProdutos!: GrupoProduto[];
  totalProdutos!: number;
  rows!: number;
  totalGrupoProdutos!: number;
  grupoProduto!: GrupoProduto;
  produto!: Produto;
tipoProdutoOptions: any[] = Object.keys(TipoProduto).filter((key) => isNaN(Number(key))).map((key) => {
    return { label: key, value: key };
  });

  loadProdutos(event: any) {
    console.log(event);
    Object.keys(event.filters).forEach((element: string) => {
      (this.produto as any)[element] = event.filters[element].value;
      if(event.filters[element].value === null || event.filters[element].value === undefined || event.filters[element].value === '') return;
      this.produto.filter![element] = event.filters[element].matchMode;
    });
    this.produto.orderer = event.multiSortMeta;
    console.log(this.produto);
    const page = event.first! / event.rows!;
    this.rows = event.rows!;
    const pager = { page: page, size: this.rows };
    this.http.getProdutoPaged(pager, this.produto).subscribe({
      next: (data) => {
        this.produtos = data.content;
        this.totalProdutos = data.totalElements;
      }
    });
  }
  loadGrupoProdutos(event: any) {
    let temp: Filter[] = [];
    Object.keys(event.filters).forEach((element: any) => {
      temp.push({field: element, matchMode: event.filters[element].matchMode});
    });
    this.grupoProduto.filter = temp;
    this.grupoProduto.orderer = event.multiSortMeta;
    const page = event.first! / event.rows!;
    this.rows = event.rows!;
    const pager = { page: page, size: this.rows };
    this.http.getGrupoProdutoPaged(pager, this.grupoProduto).subscribe({
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
    this.grupoProduto = { filialId: filial.id! };
    this.produto = {
      filialId: filial.id!,
      filter: new Map<string, string>(),
      disabled: {
        nome: false,
        descricao: false,
      },
    };
    this.http.getProdutoPaged(pager, this.produto).subscribe((data) => {
      this.totalProdutos = data.totalElements;
      this.produtos = data.content;
      this.rows = data.size;
    });

    this.http
      .getGrupoProdutoPaged(pager, this.grupoProduto)
      .subscribe((data) => {
        this.totalGrupoProdutos = data.totalElements;
      });
  }
  produtoChange(value: number) {
    this.isProduto = value === 0;
  }
  constructor(
    private http: HttpService,
    private messageService: MessageService
  ) {}
}
