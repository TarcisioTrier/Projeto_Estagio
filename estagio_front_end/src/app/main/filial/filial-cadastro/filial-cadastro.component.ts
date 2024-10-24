import { Component } from '@angular/core';
import { debounce } from 'lodash';
import { MessageService } from 'primeng/api';
import { SituacaoContrato } from '../../../models/app-enums';
import {
  Cep,
  cepToEndereco,
  Cnpj,
  cnpjEnderecoDisabler,
  cnpjToEndereco,
  cnpjToFilial,
  validateCnpj,
  validateCnpjField,
} from '../../../models/externalapi';
import { Filial } from '../../../models/filial';
import { HttpService } from '../../../services/http/http.service';
import { MessageHandleService } from '../../../services/message-handle.service';
import { Endereco } from './../../../models/filial';

@Component({
  selector: 'app-filial-cadastro',
  templateUrl: './filial-cadastro.component.html',
  styleUrl: './filial-cadastro.component.scss',
  providers: [MessageService],
})
export class FilialCadastroComponent {
  itemSelecionado: any;
  cadastroFilial: Filial = {
    nomeFantasia: '',
    razaoSocial: '',
    cnpj: '',
    telefone: '',
    email: '',
    situacaoContrato: SituacaoContrato.ATIVO,
    disabled: {
      nomeFantasia: false,
      razaoSocial: false,
      email: false,
    },
  };
  endereco: Endereco = {
    cep: '',
    logradouro: '',
    numero: undefined,
    complemento: '',
    bairro: '',
    localidade: '',
    estado: '',
    disabled: {
      logradouro: false,
      numero: false,
      complemento: false,
      bairro: false,
      localidade: false,
      estado: false,
    },
  };

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

  loading = false;

  load() {
    this.loading = true;

    this.cadastroFilial.telefone = this.cadastroFilial.telefone.replace(
      /[_]/g,
      ''
    );

    if (this.endereco.cep !== '') {
      this.cadastroFilial.endereco = this.endereco;
    } else {
      this.cadastroFilial.endereco = undefined;
    }

    this.http.postFilial(this.cadastroFilial).subscribe({
      next: (retorno) => {
        this.messageHandler.showSuccessMessage(retorno);
      },
      error: (erro) => {
        this.messageHandler.showErrorMessage(erro);
      },
    });
    this.loading = false;
  }

  cnpj = debounce(() => {
    const cnpj = this.cadastroFilial.cnpj.replace(/[_./-]/g, '');
    console.log(cnpj);

    if (cnpj.length == 0) {
      this.enableFieldsCnpj();
    }

    if (validateCnpj(cnpj)) {
      this.http.buscaCNPJ(cnpj).subscribe({
        next: (obj: Cnpj) => {
          console.log(obj);
          if (obj.error) {
            this.messageHandler.showErrorMessage(obj.error);
          } else {
            this.cadastroFilial = cnpjToFilial(obj);
            if (obj.CEP !== '') {
              this.cep(obj);
            }
          }
        },
        error: (error) => {
          console.log(error);
        },
      });
    } else {
      validateCnpjField(cnpj);
    }
  }, 1000);

  cadastroSituacao(event: any) {
    this.cadastroFilial.situacaoContrato = event.value;
  }

  cep = debounce((cnpj?: Cnpj) => {
    const cep = this.endereco.cep.replace(/[_-]/g, '');
    console.log(cep.length);

    if (cep.length == 0) {
      this.enableFieldsAndCleanCep();
    }

    if (cnpj) {
      this.endereco = cnpjToEndereco(cnpj);
      this.http.viaCep(cep).subscribe({
        next: (obj: Cep) => {
          console.log(obj);
          cepToEndereco(obj, this.endereco);
        },
        error: (error) => {
          this.messageHandler.showErrorMessage(error);
        },
      });
      cnpjEnderecoDisabler(cnpj, this.endereco);
    } else if (cep.length == 8) {
      this.buscarCep(cep);
    }
  }, 300);

  private buscarCep(cep: string) {
    this.http.viaCep(cep).subscribe({
      next: (obj: Cep) => {
        console.log(obj);
        cepToEndereco(obj, this.endereco);
      },
      error: (error) => this.messageHandler.showErrorMessage(error),
    });
  }

  isFoneValid(): boolean {
    return this.cadastroFilial.telefone.replace(/[_]/g, '').length < 15;
  }

  enableFieldsAndCleanCep() {
    this.endereco.disabled.bairro = false;
    this.endereco.disabled.complemento = false;
    this.endereco.disabled.estado = false;
    this.endereco.disabled.localidade = false;
    this.endereco.disabled.logradouro = false;
    this.endereco.disabled.numero = false;

    this.endereco.bairro = '';
    this.endereco.complemento = '';
    this.endereco.estado = '';
    this.endereco.localidade = '';
    this.endereco.logradouro = '';
    this.endereco.numero = undefined;
  }

  enableFieldsCnpj() {
    this.cadastroFilial.disabled.email = false;
    this.cadastroFilial.disabled.nomeFantasia = false;
    this.cadastroFilial.disabled.razaoSocial = false;
  }
}
