import { Filial } from './../models/filial';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { Fornecedor } from '../models/fornecedor';
import { FilialPage } from '../models/filial-page';
import { Cep, Cnpj } from '../models/cepapi';

@Injectable({
  providedIn: 'root',
})
export class HttpService {

  constructor(private http: HttpClient) {}
  viaCep(cep: string): Observable<Cep>{
    return this.http.get<Cep>(`https://viacep.com.br/ws/${cep}/json/`).pipe(take(1));
  }
  buscaCNPJ(cnpj: string): Observable<Cnpj>{
    return this.http.get<Cnpj>(`https://api-publica.speedio.com.br/buscarcnpj?cnpj=${cnpj}`).pipe(take(1));
  }
  postFilial(filial: Filial) {
    return this.http.post('filiais/post', filial).pipe(take(1));
  }
  getFilialbyId(id: number) {
    return this.http.get(`filiais/get/${id}`).pipe(take(1));
  }
  getFilialPaged(filialPage?: FilialPage) {
    if (filialPage) {
      let par = new HttpParams();
      if(filialPage.size){
        par = par.set('size', filialPage.size)
      }
      if(filialPage.page){
        par = par.set('page', filialPage.page)
      }
      if(filialPage.nome){
        par = par.set('nome', filialPage.nome)
      }
      if(filialPage.cnpj){
        par = par.set('cnpj', filialPage.cnpj)
      }
      return this.http.get('filiais/getAllPaged', {params: par}).pipe(take(1));
    } else {
      return this.http.get('filiais/getAllPaged').pipe(take(1));
    }
  }
  getFilialFiltered(nome:string){
    return this.http.get(`filiais/getAllFilter`, {params: {nome: nome}}).pipe(take(1));
  }

  putFilial(filial: Filial) {
    return this.http.put(`filiais/update/${filial.id}`, filial).pipe(take(1));
  }
  deleteFilial(id: number) {
    return this.http.delete(`filiais/delete/${id}`).pipe(take(1));
  }
  postFornecedores(fornecedor: Fornecedor) {
    return this.http.post('fornecedores/post', fornecedor).pipe(take(1));
  }
}
