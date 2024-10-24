import { Component } from '@angular/core';
import { SituacaoContrato } from '../../../models/app-enums';
import {
  Cnpj,
  cnpjtoFornecedor,
  validateCnpj,
  validateCnpjField,
} from '../../../models/externalapi';
import { Fornecedor } from '../../../models/fornecedor';
import { HttpService } from '../../../services/http/http.service';
import { MessageHandleService } from '../../../services/message-handle.service';

@Component({
  selector: 'app-fornecedor-cadastro',
  templateUrl: './fornecedor-cadastro.component.html',
  styleUrl: './fornecedor-cadastro.component.scss',
})
export class FornecedorCadastroComponent {
  loading = false;

  fornecedor: Fornecedor = {
    cnpj: '',
    telefone: '',
    disabled: {
      nomeFantasia: false,
      razaoSocial: false,
      email: false,
    },
  };

  situacaoCadastroSelecionado: any;
  situacaoContrato = Object.keys(SituacaoContrato)
    .filter((key) => isNaN(Number(key)))
    .map((status, index) => ({
      label: status.replace(/_/g, ' '),
      value: index,
    }));

  constructor(
    private http: HttpService,
    private messageHandler: MessageHandleService
  ) {}

  load() {
    this.loading = true;
    const data = sessionStorage.getItem('filial');
    const localFilial = data ? JSON.parse(data) : undefined;
    this.fornecedor.filialId = localFilial.id;

    this.http.postFornecedor(this.fornecedor).subscribe({
      next: (retorno) => {
        this.messageHandler.showSuccessMessage(retorno);
      },
      error: (erro) => {
        this.messageHandler.showErrorMessage(erro);
      },
    });
    this.loading = false;
  }

  cnpj() {
    const cnpj = this.fornecedor.cnpj.replace(/[_./-]/g, '');
    console.log(cnpj);

    if (cnpj.length == 0) {
      this.enableFieldsCnpj();
    }

    if (validateCnpj(cnpj)) {
      this.http.buscaCNPJ(cnpj).subscribe({
        next: (obj: Cnpj) => this.processCNPJResponse(obj),
        error: (error) => {
          console.log(error);
        },
      });
    } else {
      validateCnpjField(cnpj);
    }
  }

  processCNPJResponse(obj: Cnpj) {
    console.log(obj);
    if (obj.error) {
      this.messageHandler.showErrorMessage(obj.error);
      return;
    }

    this.fornecedor = cnpjtoFornecedor(obj);
  }

  enableFieldsCnpj() {
    this.fornecedor.disabled.nomeFantasia = false;
    this.fornecedor.disabled.razaoSocial = false;
    this.fornecedor.disabled.email = false;
  }

  isFoneValid(): boolean {
    return this.fornecedor.telefone.replace(/[_]/g, '').length < 15;
  }
  cadastroSituacao(event: any) {
    this.fornecedor.situacaoContrato = event.value;
  }
}
