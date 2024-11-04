import { MessageHandleService } from './../../../services/message-handle.service';
import { Component } from '@angular/core';
import { Filial } from '../../../models/filial';
import { HttpService } from '../../../services/http/http.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-filial-cadastro',
  templateUrl: './filial-cadastro.component.html',
  styleUrl: './filial-cadastro.component.scss',
  providers: [MessageService],
})
export class FilialCadastroComponent {
  filial!: Filial;

  loading = false;
  load(filial: Filial) {
    this.http.postFilial(filial).subscribe({
      next: (retorno) => {
        this.messageHandler.showCadastroMessage(retorno);
      },
      error: (erro) => {
        let error = erro.error;
        if (typeof error === 'string') {
          this.messageHandler.showErrorMessage(error);
        } else {
          let erros = Object.values(error);
          for (let erro of erros) {
            this.messageHandler.showErrorMessage(erro);
          }
        }
      },
    });
    this.loading = false;
  }
  constructor(
    private http: HttpService,
    private messageHandler: MessageHandleService
  ) {}
}
