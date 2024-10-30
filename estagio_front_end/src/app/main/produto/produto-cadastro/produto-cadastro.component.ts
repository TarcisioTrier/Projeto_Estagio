import { Component, OnInit } from '@angular/core';
import { Produto } from '../../../models/produto';
import { HttpService } from '../../../services/http/http.service';
import { MessageHandleService } from '../../../services/message-handle.service';


@Component({
  selector: 'app-produto-cadastro',
  templateUrl: './produto-cadastro.component.html',
  styleUrl: './produto-cadastro.component.scss',
})
export class ProdutoCadastroComponent {

  load(produto: Produto) {
      produto.grupoProdutoId = produto.grupoProduto!.id;
    this.http.postProduto(produto).subscribe({
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
