import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { enumToArray, TipoGrupo, AtualizaPrecoEnum } from '../../../models/app-enums';
import { AtualizaPreco } from '../../../models/atualizapreco';
import { GrupoProduto } from '../../../models/grupo-produto';
import { HttpService } from '../../../services/http/http.service';
import { MessageHandleService } from '../../../services/message-handle.service';

@Component({
  selector: 'app-atualizacao-preco-grupo-produto',
  templateUrl: './atualizacao-preco-grupo-produto.component.html',
  styleUrl: './atualizacao-preco-grupo-produto.component.scss'
})
export class AtualizacaoPrecoGrupoProdutoComponent implements OnInit{
  @Output() close = new EventEmitter();
  atualizarPreco() {
    this.atualizacao.valor = this.valor;
      if (this.selectedGrupoProdutos) {
        this.atualizacao.grupoProdutoId = this.selectedGrupoProdutos.map(
          (grupoProduto) => grupoProduto.id!
        );
      }
      if (this.atualizacao.all) {
        this.atualizacao.grupoProdutoFilter = this.grupoProdutoFilter;
      }
      this.close.emit(this.atualizacao);
  }
  selectedGrupoProdutos!: GrupoProduto[];
  produtosOptions: any[] = [
    { label: 'Produto', value: true },
    { label: 'Grupo de Produto', value: false },
  ];
  atualizacao: AtualizaPreco = {
    produtoId: [],
    grupoProdutoId: [],
    all: false,
    isProduto: false,
    isRelativo: false,
    isPercentual: false,
  };
  grupoProdutos!: GrupoProduto[];
  rows!: number;
  totalGrupoProdutos!: number;
  grupoProdutoFilter!: GrupoProduto;
  tipoGrupoOptions: any[] = enumToArray(TipoGrupo);
  valor!: number;
  tipoAtualizacaoOptions = enumToArray(AtualizaPrecoEnum);
  last: any;
  loadGrupoProdutos(event: any) {
    Object.keys(event.filters).forEach((element: string) => {
      if (
        event.filters[element].value === null ||
        event.filters[element].value === undefined ||
        event.filters[element].value === ''
      ){
        return;
      }else{
        (this.grupoProdutoFilter as any)[element] = event.filters[element].value;
        (this.grupoProdutoFilter as any).filter![element] = event.filters[element].matchMode;
      }
    });
    this.grupoProdutoFilter.orderer = event.multiSortMeta;
    const page = event.first! / event.rows!;
    this.rows = event.rows!;
    const pager = { page: page, size: this.rows };
    this.http.getGrupoProdutoPaged(pager, this.grupoProdutoFilter).subscribe({
      next: (data) => {
        this.grupoProdutos = data.content;
        this.totalGrupoProdutos = data.totalElements;
      },
    });
  }

  ngOnInit(): void {
    const data = sessionStorage.getItem('filial');
    const filial = data ? JSON.parse(data) : undefined;
    const pager = {};
    this.atualizacao.filialId = filial.id!;
    this.grupoProdutoFilter = {
      filialId: filial.id!,
      filter: new Map<string, string>(),
    };

    this.http
      .getGrupoProdutoPaged(pager, this.grupoProdutoFilter)
      .subscribe((data) => {
        this.totalGrupoProdutos = data.totalElements;
      });
  }
  produtoChange(value: number) {
    this.atualizacao.isProduto = value === 0;
  }
  constructor(
    private http: HttpService
  ) {}
}


