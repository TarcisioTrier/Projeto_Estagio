import { Component } from '@angular/core';
import { HttpService } from '../../services/http/http.service';
import { AtualizaPreco } from '../../models/atualizapreco';
import { MessageHandleService } from '../../services/message-handle.service';
@Component({
  selector: 'app-atualizacao-preco',
  templateUrl: './atualizacao-preco.component.html',
  styleUrl: './atualizacao-preco.component.scss',
})
export class AtualizacaoPrecoComponent {
  produtosOptions: any[] = [
    { label: 'Produto', value: true },
    { label: 'Grupo de Produto', value: false },
  ];
  atualizaRequest(atualizacao: AtualizaPreco) {
    console.log(atualizacao)
    this.http.putAtualizacaoPreco(atualizacao).subscribe({
      next: (data) => {
        this.messageHandle.showSuccessMessage(
          'Atualização realizada com sucesso'
        );
        console.log(data);
      },
      error: (erro) => {
        let error = erro.error;
        if (typeof error === 'string') {
          this.messageHandle.showErrorMessage(error);
        } else {
          this.messageHandle.showErrorMessage('Erro ao atualizar');
          console.log(erro);
        }
      },
    });
  }

  constructor(
    private http: HttpService,
    private messageHandle: MessageHandleService
  ) {}
}
