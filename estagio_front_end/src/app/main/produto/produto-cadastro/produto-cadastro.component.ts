import { Component } from '@angular/core';
import { debounce } from 'lodash';
import { MessageService } from 'primeng/api';
import { AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import {
  Apresentacao,
  SituacaoCadastro,
  TipoProduto,
} from '../../../models/app-enums';
import { GrupoProduto } from '../../../models/grupo-produto';
import { Produto } from '../../../models/produto';
import { HttpService } from '../../../services/http/http.service';
import { MessageHandleService } from '../../../services/message-handle.service';
import { verificaBarcode } from '../../../models/externalapi';

@Component({
  selector: 'app-produto-cadastro',
  templateUrl: './produto-cadastro.component.html',
  styleUrl: './produto-cadastro.component.scss',
})
export class ProdutoCadastroComponent {
  loading = false;

  tipoProdutoSelecionado: any;
  apresentacaoSelecionado: any;
  grupoProdutoFilter: GrupoProduto[] = [];
  produto: Produto = {
    codigoBarras: '',
    nome: '',
    descricao: '',
    valorVenda: 0,
    disabled: {
      nome: false,
      descricao: false,
    },
    atualizaPreco: false,
    situacaoCadastro: SituacaoCadastro.ATIVO,
  };

  constructor(
    private http: HttpService,
    private messageHandle: MessageHandleService
  ) {}

  load() {
    this.loading = true;
    if (this.produto.grupoProduto) {
      this.produto.grupoProdutoId = this.produto.grupoProduto.id;
    }
    this.http.postProduto(this.produto).subscribe({
      next: (retorno) => {
        this.messageHandle.showSuccessMessage(retorno);
      },
      error: (erro) => {
        this.messageHandle.showErrorMessage(erro);
      },
    });
    this.loading = false;
  }

  apiBarcode = debounce(() => {
    const barcode = this.produto.codigoBarras;

    if (verificaBarcode(barcode)) {
      this.http.barcode(barcode).subscribe({
        next: (retorno) => {
          console.log(retorno);
          this.produto.nome = retorno.description;
          this.produto.descricao = retorno.ncm.full_description;
          this.produto.disabled.nome = true;
          this.produto.disabled.descricao = true;
        },
        error: () => {
          this.messageHandle.showErrorMessage('codigo de barra não encontrado');
        },
      });
    } else {
      this.messageHandle.showErrorMessage('código de barra inválido');
    }
  }, 300);

  filterItems(event: AutoCompleteCompleteEvent) {
    const data = sessionStorage.getItem('filial');
    const localFilial = data ? JSON.parse(data) : undefined;
    this.http.getGrupoProdutoAllFilter(localFilial.id, event.query).subscribe({
      next: (list) => {
        this.grupoProdutoFilter = Object.values(list);
      },
      error: (erro) => {
        this.messageHandle.showErrorMessage(erro);
      },
    });
  }

  apresentacao = Object.keys(Apresentacao)
    .filter((key) => isNaN(Number(key)))
    .map((key) => ({
      label: key.replace(/_/g, ' '),
      value: Apresentacao[key as keyof typeof Apresentacao],
    }));

  tipoProduto = Object.keys(TipoProduto)
    .filter((key) => isNaN(Number(key)))
    .map((key) => ({
      label: key.replace(/_/g, ' '),
      value: TipoProduto[key as keyof typeof TipoProduto],
    }));

  cadastroApresentacao(event: any) {
    this.produto.apresentacao = event.value;
  }

  cadastroTipoProduto(event: any) {
    this.produto.tipoProduto = event.value;
  }

  verificaCodigoBarra(barcode: string) {
    return verificaBarcode(barcode);
  }
}
