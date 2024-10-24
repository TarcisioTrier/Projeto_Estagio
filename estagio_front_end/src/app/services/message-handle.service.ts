import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root',
})
export class MessageHandleService {
  constructor(private messageService: MessageService) {}

  showSuccessMessage(retorno: any) {
    console.log(retorno);
    this.messageService.add({
      severity: 'success',
      summary: 'Successo',
      detail: 'Cadastrado com Sucesso',
    });
  }

  showErrorMessage(erro: any) {
    const error = erro.error;
    console.log(erro);
    if (typeof error === 'string') {
      this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: error,
      });
    } else {
      const erros = Object.values(error);
      for (let erro of erros) {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: String(erro),
        });
      }
    }
  }
}
