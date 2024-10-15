import { GrupoProduto } from './../models/grupo-produto';
import { Filial } from './../models/filial';
import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take, throwError } from 'rxjs';
import { Fornecedor } from '../models/fornecedor';
import { FilialPage } from '../models/filial-page';
import { catchError } from 'rxjs/operators';
import { Cep, Cnpj } from '../models/externalapi';

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  constructor(private http: HttpClient) {}
  viaCep(cep: string): Observable<Cep> {
    return this.http
      .get<Cep>(`https://viacep.com.br/ws/${cep}/json/`)
      .pipe(take(1));
  }
  buscaCNPJ(cnpj: string): Observable<Cnpj> {
    return this.http
      .get<Cnpj>(`https://api-publica.speedio.com.br/buscarcnpj?cnpj=${cnpj}`)
      .pipe(take(1), catchError(this.handleError));
  }
  postFilial(filial: Filial) {
    return this.http
      .post('filiais/post', filial)
      .pipe(take(1), catchError(this.handleError));
  }
  postFornecedor(fornecedor: Fornecedor) {
    return this.http
      .post('fornecedor/post', fornecedor)
      .pipe(take(1), catchError(this.handleError));
  }
  postGrupoProduto(grupoProduto: GrupoProduto) {
    return this.http
      .post('grupos-produtos/post', grupoProduto)
      .pipe(take(1), catchError(this.handleError));
  }
  getFilialbyId(id: number) {
    return this.http.get(`filiais/get/${id}`).pipe(take(1));
  }
  getFilialPaged(filialPage?: FilialPage) {
    if (filialPage) {
      let par = new HttpParams();
      if (filialPage.size) {
        par = par.set('size', filialPage.size);
      }
      if (filialPage.page) {
        par = par.set('page', filialPage.page);
      }
      if (filialPage.nome) {
        par = par.set('nome', filialPage.nome);
      }
      if (filialPage.cnpj) {
        par = par.set('cnpj', filialPage.cnpj);
      }
      return this.http
        .get('filiais/getAllPaged', { params: par })
        .pipe(take(1));
    } else {
      return this.http.get('filiais/getAllPaged').pipe(take(1));
    }
  }
  getFilialFiltered(nome: string) {
    return this.http
      .get(`filiais/getAllFilter`, { params: { nome: nome } })
      .pipe(take(1));
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

  private handleError(error: HttpErrorResponse) {
    return throwError(
      () => error
    );
  }
}
