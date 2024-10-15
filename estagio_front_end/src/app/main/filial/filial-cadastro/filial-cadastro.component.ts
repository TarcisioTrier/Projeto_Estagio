import { NotFoundComponent } from './../../not-found/not-found.component';
import { SituacaoCadastro } from './../../../models/app-enums';
import { Endereco } from './../../../models/filial';
import { Component, OnInit } from '@angular/core';
import { Filial } from '../../../models/filial';
import { SituacaoContrato } from '../../../models/app-enums';
import { HttpService } from '../../../services/http.service';
import {
  Cep,
  cepToEndereco,
  Cnpj,
  cnpjToEndereco,
  cnpjToFilial,
  cpnjEnderecoDisabler,
  validateCnpj,
} from '../../../models/externalapi';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-filial-cadastro',
  templateUrl: './filial-cadastro.component.html',
  styleUrl: './filial-cadastro.component.scss',
  providers: [MessageService],
})
export class FilialCadastroComponent {
telefone(): boolean {
  if(this.cadastroFilial.telefone.replace(/[_]/g, '').length < 15)
  return true;
  return false;
}
  loading = false;
  load() {
    this.loading = true;
    let filial = this.cadastroFilial;
    if (this.endereco.cep !== '') {
      filial.endereco = this.endereco;
    }
    this.http.postFilial(filial).subscribe({
      next: (retorno) => {
        console.log(retorno);
        this.messageService.add({
          severity: 'success',
          summary: 'Successo',
          detail: 'Cadastrado com Sucesso',
        });
      },
      error: (erro) => {
        let error = erro.error;
        if (typeof error === 'string') {
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: error,
          });
        } else {
          let erros = Object.values(error);
          for (let erro of erros) {
            this.messageService.add({
              severity: 'error',
              summary: 'Erro',
              detail: String(erro),
            });
          }
        }
      },
    });
    this.loading = false;
  }
  cnpj() {
    const cnpj = this.cadastroFilial.cnpj.replace(/[_./-]/g, '');
    console.log(cnpj);
    if (validateCnpj(cnpj)) {
      this.http.buscaCNPJ(cnpj).subscribe({
        next: (obj: Cnpj) => {
          console.log(obj);
          if (obj.error) {
            this.messageService.add({
              severity: 'error',
              summary: 'Erro',
              detail: obj.error,
            });
            return;
          }

          this.cadastroFilial = cnpjToFilial(obj);
          if (obj.CEP !== '') {
            this.cep(obj);
          }
        },
        error: (error) => {
          console.log(error);
            this.messageService.add({
              severity: 'error',
              summary: 'Erro',
              detail: error.message,
            });

        }
      });
    }else{
      const cnpjElement = document.getElementById('cnpj');
      if(cnpjElement){
        if (cnpj.length > 0) {
          cnpjElement.classList.add('ng-invalid');
          cnpjElement.classList.add('ng-dirty');
        }else{
          cnpjElement.classList.remove('ng-invalid');
          cnpjElement.classList.remove('ng-dirty');
        }
      }

    }
  }
  cadastroSituacao(event: any) {
    this.cadastroFilial.situacaoContrato = event.value;
  }
  constructor(
    private http: HttpService,
    private messageService: MessageService
  ) {}
  cep(cnpj?: Cnpj) {
    const cep = this.endereco.cep.replace(/[_-]/g, '');
    if (cnpj) {
      this.endereco = cnpjToEndereco(cnpj);
      this.http.viaCep(cep).subscribe({
        next: (obj: Cep) => {
          console.log(obj);
          cepToEndereco(obj, this.endereco);
        },
      });
      cpnjEnderecoDisabler(cnpj, this.endereco);
    } else if (cep.length == 8) {
      this.http.viaCep(cep).subscribe({
        next: (obj: Cep) => {
          console.log(obj);
          cepToEndereco(obj, this.endereco);
        },
      });
    }
  }
  situacaoContrato = Object.keys(SituacaoContrato)
    .filter((key) => isNaN(Number(key)))
    .map((status, index) => ({
      label: status.replace(/_/g, ' '),
      value: index,
    }));
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
}

