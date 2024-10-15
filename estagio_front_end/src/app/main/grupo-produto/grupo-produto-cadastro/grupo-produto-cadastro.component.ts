import { Component } from '@angular/core';
import { GrupoProduto } from '../../../models/grupo-produto';
import { SituacaoCadastro, TipoGrupoProduto } from '../../../models/app-enums';
import { HttpService } from '../../../services/http.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-grupo-produto-cadastro',
  templateUrl: './grupo-produto-cadastro.component.html',
  styleUrl: './grupo-produto-cadastro.component.scss'
})
export class GrupoProdutoCadastroComponent {
  constructor(
    private http: HttpService,
    private messageService: MessageService
  ) {}
load() {
  this.loading = true;
  const data = sessionStorage.getItem('filial');
  const localFilial = data ? JSON.parse(data) : undefined;
  this.grupoProduto.filialId = localFilial.id;
  this.http.postGrupoProduto(this.grupoProduto).subscribe({
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
  loading = false;
cadastroTipoGrupo(event: any) {
  this.grupoProduto.tipoGrupo = event.value;
}
  tipoGrupoProdutoSelecionado: any;
  grupoProduto: GrupoProduto = {
    atualizaPreco: false,
    situacaoCadastro: SituacaoCadastro.ATIVO
  }
tipoGrupoProduto = Object.keys(TipoGrupoProduto)
.filter((key) => isNaN(Number(key)))
.map((status, index) => ({
  label: status.replace(/_/g, ' '),
  value: index,
}));



}
