import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Cep, Cnpj } from '../../models/externalapi';
import { Filial } from '../../models/filial';
import { FilialPage } from '../../models/filial-page';
import { Fornecedor } from '../../models/fornecedor';
import { GrupoProduto } from '../../models/grupo-produto';
import { Produto } from '../../models/produto';

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  constructor(private http: HttpClient) {}

  //Produtos
  postProduto(produto: Produto): Observable<Produto> {
    return this.http
      .post<Produto>('produto/post', produto)
      .pipe(catchError(this.handleError));
  }

  //Filiais
  postFilial(filial: Filial): Observable<Filial> {
    return this.http
      .post<Filial>('filiais/post', filial)
      .pipe(catchError(this.handleError));
  }

  putFilial(filial: Filial): Observable<Filial> {
    return this.http
      .put<Filial>(`filiais/update/${filial.id}`, filial)
      .pipe(catchError(this.handleError));
  }

  deleteFilial(id: number): Observable<void> {
    return this.http
      .delete<void>(`filiais/delete/${id}`)
      .pipe(catchError(this.handleError));
  }

  getFilialbyId(id: number): Observable<Filial> {
    return this.http
      .get<Filial>(`filiais/get/${id}`)
      .pipe(catchError(this.handleError));
  }

  getFilialPaged(filialPage?: FilialPage): Observable<Filial[]> {
    let params = this.createHttpParams(filialPage);
    return this.http
      .get<Filial[]>('filiais/getAllPaged', { params })
      .pipe(catchError(this.handleError));
  }

  getFilialFiltered(nome: string): Observable<Filial[]> {
    return this.http
      .get<Filial[]>(`filiais/getAllFilter`, { params: { nome } })
      .pipe(catchError(this.handleError));
  }

  //Fornecedores
  postFornecedor(fornecedor: Fornecedor): Observable<Fornecedor> {
    return this.http
      .post<Fornecedor>('fornecedores/post', fornecedor)
      .pipe(catchError(this.handleError));
  }

  //Grupo de Produtos
  postGrupoProduto(grupoProduto: GrupoProduto) {
    return this.http
      .post('grupos-produtos/post', grupoProduto)
      .pipe(take(1), catchError(this.handleError));
  }

  getGrupoProdutoAllFilter(
    filialId?: number,
    nomeGrupo?: string
  ): Observable<GrupoProduto[]> {
    let params = new HttpParams();
    if (filialId) {
      params = params.set('filialId', filialId.toString());
    }
    if (nomeGrupo) {
      params = params.set('nomeGrupo', nomeGrupo);
    }
    return this.http
      .get<GrupoProduto[]>('grupos-produtos/getAllFilter', { params })
      .pipe(catchError(this.handleError));
  }

  //APIs
  barcode(barcode: string): Observable<any> {
    return this.http
      .get(`/gtins/${barcode}.json`)
      .pipe(catchError(this.handleError));
  }

  viaCep(cep: string): Observable<Cep> {
    return this.http
      .get<Cep>(`https://viacep.com.br/ws/${cep}/json/`)
      .pipe(catchError(this.handleError));
  }

  buscaCNPJ(cnpj: string): Observable<Cnpj> {
    return this.http
      .get<Cnpj>(`https://api-publica.speedio.com.br/buscarcnpj?cnpj=${cnpj}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    return throwError(() => error);
  }

  private createHttpParams(filialPage?: FilialPage): HttpParams {
    let params = new HttpParams();
    if (filialPage) {
      if (filialPage.size) {
        params = params.set('size', filialPage.size.toString());
      }
      if (filialPage.page) {
        params = params.set('page', filialPage.page.toString());
      }
      if (filialPage.nome) {
        params = params.set('nome', filialPage.nome);
      }
      if (filialPage.cnpj) {
        params = params.set('cnpj', filialPage.cnpj);
      }
    }
    return params;
  }
}
