import { Component, OnInit } from '@angular/core';
import { Produto } from '../../models/produto';
import { GrupoProduto } from '../../models/grupo-produto';
import { HttpService } from '../../services/http/http.service';
import { Filial } from '../../models/filial';
import { LazyLoadEvent, MessageService, SortEvent } from 'primeng/api';
import { TableLazyLoadEvent } from 'primeng/table';
import { Apresentacao, Order, TipoProduto } from '../../models/app-enums';
import { PaginatorState } from 'primeng/paginator';

@Component({
  selector: 'app-atualizacao-preco',
  templateUrl: './atualizacao-preco.component.html',
  styleUrl: './atualizacao-preco.component.scss',
})
export class AtualizacaoPrecoComponent implements OnInit {
  test() {
    console.log(this.selectedProdutos);
  }
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

  loadProdutos(event: any) {
    console.log(event);
    this.produto.order = event.multiSortMeta;
    console.log(this.produto);
    const page = event.first! / event.rows!;
    this.rows = event.rows!;
    const pager = { page: page, size: this.rows };
    this.http.getProdutoPaged(pager, this.produto).subscribe({
      next: (data) => {
        this.produtos = data.content;
        this.totalProdutos = data.totalElements;
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
  produtoChange(value: number) {
    this.isProduto = value === 0;
  }
  loadGrupoProdutos(event: any) {
    console.log(event);
    const page = event.first! / event.rows!;
    this.rows = event.rows!;
    const pager = { page: page, size: this.rows };

    console.log(pager);
    this.http.getGrupoProdutoPaged(pager, this.grupoProduto).subscribe({
      next: (data) => {
        this.grupoProdutos = data.content;
        this.totalGrupoProdutos = data.totalElements;
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  localFilial: () => number = () => {
    const data = sessionStorage.getItem('filial');
    const temp = data ? JSON.parse(data) : undefined;
    return Number(temp.id);
  };

  ngOnInit(): void {
    const pager = {};
    this.grupoProduto = { filialId: this.localFilial() };
    this.produto = {
      filialId: this.localFilial(),
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

  constructor(
    private http: HttpService,
    private messageService: MessageService
  ) {}
}
