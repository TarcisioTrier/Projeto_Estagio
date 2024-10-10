import { Filial } from './../models/filial';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { take } from 'rxjs';
import { Fornecedor } from '../models/fornecedor';
import { FilialPage } from '../models/filial-page';

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  // buscarPorQueryParam(id: Number){
  //   return this.http.get(`comments`, {params : {postId: id.toString()}}).pipe(take(1));
  // }
  constructor(private http: HttpClient) {}

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
