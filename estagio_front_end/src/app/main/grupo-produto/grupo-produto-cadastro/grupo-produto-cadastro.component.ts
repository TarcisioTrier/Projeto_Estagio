import { Component } from '@angular/core';
import { SituacaoCadastro, TipoGrupoProduto } from '../../../models/app-enums';
import { GrupoProduto } from '../../../models/grupo-produto';
import { HttpService } from '../../../services/http/http.service';
import { MessageHandleService } from '../../../services/message-handle.service';

@Component({
  selector: 'app-grupo-produto-cadastro',
  templateUrl: './grupo-produto-cadastro.component.html',
  styleUrl: './grupo-produto-cadastro.component.scss',
})
export class GrupoProdutoCadastroComponent {
  loading = false;

  tipoGrupoProdutoSelecionado: any;
  grupoProduto: GrupoProduto = {
    atualizaPreco: false,
    situacaoCadastro: SituacaoCadastro.ATIVO,
  };

  constructor(
    private http: HttpService,
    private messageHandle: MessageHandleService
  ) {}

  load() {
    this.loading = true;

    const data = sessionStorage.getItem('filial');
    const localFilial = data ? JSON.parse(data) : undefined;

    this.grupoProduto.filialId = localFilial.id;
    this.http.postGrupoProduto(this.grupoProduto).subscribe({
      next: (retorno) => {
        this.messageHandle.showSuccessMessage(retorno);
      },
      error: (erro) => {
        this.messageHandle.showErrorMessage(erro);
      },
    });
    this.loading = false;
  }

  cadastroTipoGrupo(event: any) {
    this.grupoProduto.tipoGrupo = event.value;
  }

  tipoGrupoProduto = Object.keys(TipoGrupoProduto)
    .filter((key) => isNaN(Number(key)))
    .map((status, index) => ({
      label: status.replace(/_/g, ' '),
      value: index,
    }));
}
