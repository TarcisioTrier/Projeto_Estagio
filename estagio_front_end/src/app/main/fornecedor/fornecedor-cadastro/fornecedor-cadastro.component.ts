import { Component, OnInit } from '@angular/core';
import { Fornecedor } from '../../../models/fornecedor';
import { HttpService } from '../../../services/http/http.service';
import { MessageHandleService } from '../../../services/message-handle.service';

@Component({
  selector: 'app-fornecedor-cadastro',
  templateUrl: './fornecedor-cadastro.component.html',
  styleUrl: './fornecedor-cadastro.component.scss',
})
export class FornecedorCadastroComponent implements OnInit {
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
  ngOnInit(): void {
    this.http.getFornecedor(1).subscribe((data) => {
      this.fornecedor = data;
      console.log(this.fornecedor);
    });
  }
}
