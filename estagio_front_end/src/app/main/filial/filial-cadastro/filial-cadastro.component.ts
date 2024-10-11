import { NotFoundComponent } from './../../not-found/not-found.component';
import { SituacaoCadastro } from './../../../models/app-enums';
import { Endereco } from './../../../models/filial';
import { Component, OnInit } from '@angular/core';
import { Filial } from '../../../models/filial';
import { SituacaoContrato } from '../../../models/app-enums';
import { HttpService } from '../../../services/http.service';
import { Cep, Cnpj } from '../../../models/cepapi';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-filial-cadastro',
  templateUrl: './filial-cadastro.component.html',
  styleUrl: './filial-cadastro.component.scss',
  providers: [MessageService]
})
export class FilialCadastroComponent{
loading = false;
load() {
  this.loading = true;
  console.log(this.cadastroFilial)
  let filial: Filial = this.cadastroFilial;
  if(this.endereco.cep !== ''){
    filial.endereco = this.endereco;
    console.log(filial.endereco)
  }
  console.log(filial)
  this.http.postFilial(filial).subscribe(retorno => {
    console.log(retorno);
    this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Cadastrado com Sucesso' });
  })
  this.loading = false;
}
print(arg0: string) {
  console.log(arg0)
}
cnpj() {
  const cnpj = this.cadastroFilial.cnpj.replace(/[_./-]/g, '');
  console.log(cnpj)
  if(cnpj.length == 14){
    this.http.buscaCNPJ(cnpj).subscribe({
      next: (obj: Cnpj) => {
        this.cadastroFilial.nomeFantasia = obj['NOME FANTASIA'].toLowerCase();;
        this.disabled.nomeFantasia = (obj['NOME FANTASIA'] !== '');
        this.cadastroFilial.razaoSocial = obj['RAZAO SOCIAL'].toLowerCase();;
        this.disabled.razaoSocial = (obj['RAZAO SOCIAL'] !== '');
        this.cadastroFilial.telefone = obj.DDD + obj.TELEFONE;
        this.disabled.telefone = ((obj.DDD + obj.TELEFONE) !== '');
        this.cadastroFilial.email = obj.EMAIL.toLowerCase();;
        this.disabled.email = (obj.EMAIL !== '');
        this.endereco.cep = obj.CEP;
        this.endereco.numero = obj.NUMERO;
        this.endereco.complemento = obj.COMPLEMENTO.toLowerCase();;
        this.endereco.localidade = obj.MUNICIPIO;
        this.endereco.bairro = obj.BAIRRO.toLowerCase();;
        this.endereco.logradouro = obj.LOGRADOURO.toLowerCase();;
        if(obj.CEP !== ''){
          this.cep()
        }
    }
    });
  }
}
  cadastroSituacao(event: any) {
    this.cadastroFilial.situacaoContrato = event.value;
  }
  constructor(private http: HttpService, private messageService: MessageService) {}
  cep() {
    const cep = this.endereco.cep.replace(/[_-]/g, '');
    console.log(cep);
    if (cep.length == 8) {
      this.http.viaCep(cep).subscribe({
        next: (obj: Cep) => {
          if (obj.bairro !== ''){
            this.endereco.bairro = obj.bairro;
          }
          this.disabled.bairro = (obj.bairro !== '')
          this.endereco.localidade = obj.localidade;
          this.disabled.cidade = (obj.localidade !== '')
          this.disabled.logradouro = (obj.logradouro !== '')
          if(obj.logradouro !== ''){
            this.endereco.logradouro = obj.logradouro;
          }


          this.endereco.estado = obj.estado;
          this.disabled.estado = (obj.estado !== '')
          console.log(this.endereco)
        },
      });
    }
  }

  situacaoContrato = Object.keys(SituacaoContrato)
    .filter((key) => isNaN(Number(key)))
    .map((status, index) => ({ label: status, value: index }));
  itemSelecionado: any;
  cadastroFilial = {
    nomeFantasia: '',
    razaoSocial: '',
    cnpj: '',
    telefone: '',
    email: '',
    situacaoContrato: SituacaoContrato.ATIVO,
  };
  endereco = {
    cep: '',
    logradouro: '',
    numero: 0,
    complemento: '',
    bairro: '',
    localidade: '',
    estado: '',
  };
  disabled = {
    nomeFantasia: false,
    razaoSocial: false,
    telefone: false,
    email: false,
    logradouro: false,
    bairro: false,
    cidade: false,
    estado: false
  };
}
