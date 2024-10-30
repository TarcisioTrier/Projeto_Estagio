import { MessageHandleService } from './../../../services/message-handle.service';
import { Endereco } from './../../../models/filial';
import { Component, OnInit } from '@angular/core';
import { Filial } from '../../../models/filial';
import { SituacaoContrato } from '../../../models/app-enums';
import { HttpService } from '../../../services/http/http.service';
import {
  Cep,
  cepToEndereco,
  Cnpj,
  cnpjToEndereco,
  cnpjToFilial,
  cnpjEnderecoDisabler,
  validateCnpj,
} from '../../../models/externalapi';
import { MessageService } from 'primeng/api';
import { debounce } from 'lodash';

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
