import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root',
})
export class MessageHandleService {
  constructor(private messageService: MessageService) {}

  showSuccessMessage(string: any) {
    this.messageService.add({
      severity: 'success',
      summary: 'Successo',
      detail: string,
    });
  }

  showCadastroMessage(retorno: any) {
    console.log(retorno);
    this.messageService.add({
      severity: 'success',
      summary: 'Successo',
      detail: 'Cadastrado com Sucesso',
    });
  }

  showErrorMessage(erro: any) {
    if (typeof erro === 'string') {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: erro,
      });
      return;
    }

    const error = erro?.error;

    if (typeof error === 'string') {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: error,
      });
    } else if (error && typeof error === 'object') {
      const erros = Object.values(error);
      for (let erro of erros) {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: String(erro),
        });
      }
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: 'Ocorreu um erro desconhecido.',
      });
    }
  }
}
