import { SituacaoCadastro } from './../../../models/app-enums';
import { Endereco } from './../../../models/filial';
import { Component, OnInit } from '@angular/core';
import { Filial } from '../../../models/filial';
import {SituacaoContrato } from '../../../models/app-enums';
import { HttpService } from '../../../services/http.service';

@Component({
  selector: 'app-filial-cadastro',
  templateUrl: './filial-cadastro.component.html',
  styleUrl: './filial-cadastro.component.scss'
})
export class FilialCadastroComponent implements OnInit{
test(event: any) {
this.cadastroFilial.situacaoContrato = event.value;
console.log(this.cadastroFilial.situacaoContrato)
}
  constructor(private http: HttpService) {}
cep(event: any) {
  const cep = String(event).replace(/[_-]/g, '');
  if (cep.length == 8){
    this.http.viaCep(cep).subscribe(obj=> {
      console.log(obj);
    })
  }
}
  ngOnInit(): void {
  }
  situacaoContrato =  Object.keys(SituacaoContrato).filter(key => isNaN(Number(key))).map((status, index) => ({ label: status, value: index }));;
  itemSelecionado: any;
  cadastroFilial: any = {
    nomeFantasia: null,
    razaoSocial: null,
    cnpj: null,
    telefone: null,
    email: null,
    situacaoContrato: null
  };
  endereco: any = {
    cep: null,
    logradouro: null,
    numero: null,
    bairro: null,
    cidade: null,
    estado: null,
    rua: null
  }
}
