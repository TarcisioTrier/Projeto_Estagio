import { Component } from '@angular/core';
import { Fornecedor } from '../../models/fornecedor';
import { HttpService } from '../../services/http/http.service';
import { MessageHandleService } from '../../services/message-handle.service';

@Component({
  selector: 'app-fornecedor',
  templateUrl: './fornecedor.component.html',
  styleUrl: './fornecedor.component.scss',
})
export class FornecedorComponent {
  fornecedor!: Fornecedor;
  load(fornecedor: Fornecedor) {
    this.http.postFornecedor(fornecedor).subscribe({
      next: (retorno) => {
        this.messageHandler.showCadastroMessage(retorno);
      },
      error: (erro) => {
        this.messageHandler.showErrorMessage(erro);
      },
    });
  }
  constructor(
    private http: HttpService,
    private messageHandler: MessageHandleService
  ) {}

}
