import { Component } from '@angular/core';
import { GrupoProduto } from '../../models/grupo-produto';
import { HttpService } from '../../services/http/http.service';
import { MessageHandleService } from '../../services/message-handle.service';

@Component({
  selector: 'app-grupo-produto',
  templateUrl: './grupo-produto.component.html',
  styleUrl: './grupo-produto.component.scss'
})
export class GrupoProdutoComponent {
  load(grupoProduto: GrupoProduto) {
    this.http.postGrupoProduto(grupoProduto).subscribe({
      next: (retorno) => {
        this.messageHandle.showCadastroMessage(retorno);
      },
      error: (erro) => {
        this.messageHandle.showErrorMessage(erro);
      },
    });
  }
  constructor(
    private http: HttpService,
    private messageHandle: MessageHandleService
  ) {}
}
