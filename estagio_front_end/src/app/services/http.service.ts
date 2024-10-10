import { Filial } from './../models/filial';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { take } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  // buscarPorQueryParam(id: Number){
  //   return this.http.get(`comments`, {params : {postId: id.toString()}}).pipe(take(1));
  // }
  constructor(private http: HttpClient) { }

  postFilial(filial: Filial){
    this.http.post('filiais/post', filial).pipe(take(1));
  }
  getFilialbyId(id: number){
    this.http.get(`filiais/get/${id}`).pipe(take(1));
  }
  getFilialFiltered(page = 0 , size = 10, nome = "", cnpj = ""){
    this.http.get('filiais/getAllFilter', {params: {page: page, size: size, nome: nome, cnpj: cnpj}})
  }
  putFilial(filial: Filial){
    this.http.put(`filiais/update/${filial.id}`, filial);
  }
  deleteFilial(id: number){
    this.http.delete(`filiais/delete/${id}`);
  }
}
