import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MessageService } from 'primeng/api';
import { enumToArray, SituacaoContrato } from '../../../models/app-enums';
import {
  validateCnpj,
  Cnpj,
  cnpjToFilial,
  cnpjToEndereco,
  Cep,
  cepToEndereco,
  cnpjEnderecoDisabler,
} from '../../../models/externalapi';
import { Filial, Endereco } from '../../../models/filial';
import { HttpService } from '../../../services/http/http.service';
import { debounce, first, isNull } from 'lodash';
import { MessageHandleService } from '../../../services/message-handle.service';

@Component({
  selector: 'app-filial-form',
  templateUrl: './filial-form.component.html',
  styleUrl: './filial-form.component.scss',
})
export class FilialFormComponent implements OnInit {
  ngOnInit(): void {
    if (this.filialPut !== undefined) {
      const situacaoContrato = Object.values(SituacaoContrato);
      console.log(situacaoContrato.indexOf(this.filialPut.situacaoContrato!));
      this.cadastroFilial = {
        id: this.filialPut.id,
        nomeFantasia: this.filialPut.nomeFantasia,
        razaoSocial: this.filialPut.razaoSocial,
        email: this.filialPut.email,
        cnpj: this.filialPut.cnpj,
        telefone: this.filialPut.telefone,
        situacaoContrato: situacaoContrato.indexOf(
          this.filialPut.situacaoContrato!
        ),
        disabled: {
          nomeFantasia: false,
          razaoSocial: false,
          email: false,
        }
      }
      if(this.filialPut.endereco !== null){
        this.cadastroFilial.endereco = {
          cep: this.filialPut.endereco!.cep,
          logradouro: this.filialPut.endereco!.logradouro,
          numero: this.filialPut.endereco!.numero,
          complemento: this.filialPut.endereco!.complemento,
          bairro: this.filialPut.endereco!.bairro,
          localidade: this.filialPut.endereco!.localidade,
          estado: this.filialPut.endereco!.estado,
          disabled: {
            logradouro: false,
            numero: false,
            complemento: false,
            bairro: false,
            localidade: false,
            estado: false,
          },
        }
      }else{
        this.cadastroFilial.endereco = {
          cep: '',
          disabled: {
            logradouro: false,
            numero: false,
            complemento: false,
            bairro: false,
            localidade: false,
            estado: false,
          },
        }
      }

    }
  }
  isValidFone(): boolean {
    if (this.cadastroFilial.telefone!.replace(/[_]/g, '').length < 15)
      return true;
    return false;
  }

  @Input() filialPut?: Filial;
  @Output() filialChange = new EventEmitter<Filial>();
  loading = false;
  load() {
    this.loading = true;

    let filial: Filial = {
      id: this.cadastroFilial.id,
      nomeFantasia: this.cadastroFilial.nomeFantasia,
      razaoSocial: this.cadastroFilial.razaoSocial,
      cnpj: this.cadastroFilial.cnpj,
      telefone: this.cadastroFilial.telefone,
      email: this.cadastroFilial.email,
      situacaoContrato: this.cadastroFilial.situacaoContrato,
      disabled: {
        nomeFantasia: false,
        razaoSocial: false,
        email: false
      }
    }
    if(this.cadastroFilial.endereco?.cep !== ''){
      filial.endereco = this.cadastroFilial.endereco!;
      }

    filial.telefone = this.cadastroFilial.telefone!.replace(/[_]/g, '');
    this.filialChange.emit(filial);
    this.loading = false;
  }
  cnpj = debounce(() => {
    if (this.filialPut === undefined) {
      const cnpj = this.cadastroFilial.cnpj!.replace(/[_./-]/g, '');
      console.log(cnpj);
      if (validateCnpj(cnpj)) {
        this.http.buscaCNPJ(cnpj).subscribe({
          next: (retorno) => {
            this.cadastroFilial = cnpjToFilial(retorno);
          },
          error: (erro) => {
            let error = erro.error;
            if (typeof error === 'string') {
              this.messageHandler.showErrorMessage(error);
            } else {
              this.messageHandler.showErrorMessage('Erro ao Cadastrar');
              console.log(erro);
            }
          },
        });
      } else {
        const cnpjElement = document.getElementById('cnpj');
        if (cnpjElement) {
          if (cnpj.length > 0) {
            cnpjElement.classList.add('ng-invalid');
            cnpjElement.classList.add('ng-dirty');
          } else {
            cnpjElement.classList.remove('ng-invalid');
            cnpjElement.classList.remove('ng-dirty');
          }
        }
      }
    }
  }, 1000);
  cadastroSituacao(event: any) {
    this.cadastroFilial.situacaoContrato = event.value;
  }
  constructor(
    private http: HttpService,
    private messageHandler: MessageHandleService
  ) {}

  cep = debounce((cnpj?: Cnpj) => {
    if (this.filialPut === undefined) {
      const cep = this.cadastroFilial.endereco!.cep!.replace(/[_-]/g, '');
      console.log(cep);
      if (cnpj) {
        this.cadastroFilial.endereco = cnpjToEndereco(cnpj);
        this.http.viaCep(cep).subscribe((obj: Cep) => {
          cepToEndereco(obj, this.cadastroFilial.endereco!);
        });
        cnpjEnderecoDisabler(cnpj, this.cadastroFilial.endereco);
      } else if (cep.length == 8) {
        this.http.viaCep(cep).subscribe({
          next: (obj: Cep) => {
            cepToEndereco(obj, this.cadastroFilial.endereco!);
          },
        });
      }
    }
  }, 300);
  situacaoContrato = enumToArray(SituacaoContrato);
  itemSelecionado: any;
  cadastroFilial: Filial = {
    cnpj: '',
    telefone: '',
    situacaoContrato: SituacaoContrato.ATIVO,
    disabled: {
      nomeFantasia: false,
      razaoSocial: false,
      email: false,
    },
    endereco: {
      cep: '',
      disabled: {
        logradouro: false,
        numero: false,
        complemento: false,
        bairro: false,
        localidade: false,
        estado: false,
      },
      logradouro: '',
      bairro: '',
      localidade: '',
      estado: '',
    },
  };
}
