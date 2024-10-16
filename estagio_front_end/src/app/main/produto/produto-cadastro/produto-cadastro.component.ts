import { Component, OnInit } from '@angular/core';
import { Produto } from '../../../models/produto';
import { GrupoProduto } from '../../../models/grupo-produto';
import { AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import { HttpService } from '../../../services/http/http.service';
import { MessageService } from 'primeng/api';
import {
  Apresentacao,
  SituacaoCadastro,
  TipoProduto,
} from '../../../models/app-enums';
import { debounce } from 'lodash';

@Component({
  selector: 'app-produto-cadastro',
  templateUrl: './produto-cadastro.component.html',
  styleUrl: './produto-cadastro.component.scss',
})
export class ProdutoCadastroComponent {

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
}else{
  this.messageService.add({
    severity: 'error',
    summary: 'Erro',
    detail: 'Código de Barras Inválido',
  });
}
}, 300);
  cadastroApresentacao(event: any) {
    this.produto.apresentacao = event.value;
  }
  apresentacao = Object.keys(Apresentacao)
    .filter((key) => isNaN(Number(key)))
    .map((status, index) => ({
      label: status.replace(/_/g, ' '),
      value: index,
    }));

  apresentacaoSelecionado: any;
  cadastroTipoProduto(event: any) {
    this.produto.tipoProduto = event.value;
  }
  tipoProduto = Object.keys(TipoProduto)
    .filter((key) => isNaN(Number(key)))
    .map((status, index) => ({
      label: status.replace(/_/g, ' '),
      value: index,
    }));
  tipoProdutoSelecionado: any;
  filterItems(event: AutoCompleteCompleteEvent) {
    const data = sessionStorage.getItem('filial');
    const localFilial = data ? JSON.parse(data) : undefined;
    this.http.getGrupoProdutoAllFilter(localFilial.id, event.query).subscribe({
      next: (list) => {
        this.grupoProdutoFilter = Object.values(list);
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
  grupoProdutoFilter: GrupoProduto[] = [];
  load() {
    this.loading = true;
    if (this.produto.grupoProduto) {
      this.produto.grupoProdutoId = this.produto.grupoProduto.id;
    }
    this.http.postProduto(this.produto).subscribe({
      next: (retorno) => {
        console.log(retorno);
        this.messageService.add({
          severity: 'success',
          summary: 'Successo',
          detail: 'Cadastrado com Sucesso',
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
      complete: () => {
        this.loading = false;
      },
    });
  }
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
  loading = false;
  constructor(
    private http: HttpService,
    private messageService: MessageService
  ) {}
}
function verificaBarcode(barcode: string): boolean {
  if (!barcode) return false; // Barcode cannot be empty

  const length = barcode.length;
  if (length !== 12 && length !== 13) return false; // Barcode must be 12 or 13 digits

  let sum = 0;
  for (let i = 0; i < length - 1; i++) {
    const digit = parseInt(barcode[i], 10);
    if (isNaN(digit)) return false; // Invalid digit
    sum += (i % 2 === 0) ? digit : digit * 3;
  }

  const checkDigit = (10 - (sum % 10)) % 10;
  return checkDigit == parseInt(barcode[length - 1], 10); // Check digit verification
}

