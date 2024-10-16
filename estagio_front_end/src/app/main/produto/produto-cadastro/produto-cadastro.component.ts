import { Component, OnInit } from '@angular/core';
import { Produto } from '../../../models/produto';
import { GrupoProduto } from '../../../models/grupo-produto';
import { AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import { HttpService } from '../../../services/http/http.service';
import { MessageService } from 'primeng/api';
import { Apresentacao, TipoProduto } from '../../../models/app-enums';

@Component({
  selector: 'app-produto-cadastro',
  templateUrl: './produto-cadastro.component.html',
  styleUrl: './produto-cadastro.component.scss',
})
export class ProdutoCadastroComponent {
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
    throw new Error('Method not implemented.');
  }
  produto: Produto = {
    codigoBarras: '',
    nome: '',
    descricao: '',
    disabled: {
      nome: false,
      descricao: false,
    },
    atualizaPreco: false,
  };
  loading = false;
  constructor(
    private http: HttpService,
    private messageService: MessageService
  ) {}
}
