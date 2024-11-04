import { GrupoProduto } from '../../models/grupo-produto';
import { Filial } from '../../models/filial';
import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take, throwError } from 'rxjs';
import { Fornecedor } from '../../models/fornecedor';
import { catchError } from 'rxjs/operators';
import { Cep, Cnpj } from '../../models/externalapi';
import { Produto } from '../../models/produto';
import { Pager } from '../../models/page';
import { AtualizaPreco } from '../../models/atualizapreco';

@Injectable({
  providedIn: 'root',
})
export class HttpService {

  filialId(){
    const data = sessionStorage.getItem('filial');
    const filial = data ? JSON.parse(data) : undefined;
    if(filial !== undefined)
    return filial.id;
    return undefined;
  }

  getTop10Produtos(filialId: number): Observable<any> {
    const pager: Pager = { page: 0, size: 10}
    const produto:Produto ={
      filialId: filialId,
      filter: new Map<string,string>(),
      disabled: {
        nome: false,
        descricao: false
      },
      orderer:[
        {
          field:"valorVenda",
          order:-1
        }
      ]
    }
    return this.getProdutoPaged(pager,produto);
  }

  getProdutosOfGrupos(filialId: number): Observable<any> {
    return this.http.get(`grupos-produtos/getProdutos/${filialId}`).pipe(take(1), catchError(this.handleError));
  }

  getFilialChart(): Observable<any> {
    return this.http.get('filiais/getChart').pipe(take(1), catchError(this.handleError));
  }

  putForncedor(fornecedor: Fornecedor) {
    return this.http
      .put(`fornecedores/put/${fornecedor.id}`, fornecedor)
      .pipe(take(1), catchError(this.handleError));
  }
  getFornecedorPaged(
    pager: { page: number; size: number },
    fornecedorFilter: Fornecedor
  ): Observable<any> {
    const params = new HttpParams()
      .set('page', pager.page || '')
      .set('size', pager.size || '')
      .set('filialId', this.filialId() || '');

    return this.http
      .put('fornecedores/getAllPaged', fornecedorFilter, { params })
      .pipe(take(1), catchError(this.handleError));
  }
  getFilialPaged(
    pager: { page: number; size: number },
    filialFilter: Filial
  ): Observable<any> {
    const params = new HttpParams()
      .set('page', pager.page || '')
      .set('size', pager.size || '');

    return this.http
      .put('filiais/getAllPaged', filialFilter, { params })
      .pipe(take(1), catchError(this.handleError));
  }
  getFornecedor(id: number): Observable<Fornecedor> {
    return this.http
      .get<Fornecedor>(`fornecedores/get/${id}`)
      .pipe(take(1), catchError(this.handleError));
  }

  editarProduto(produto: Produto) {
    return this.http
      .put(`produto/put/${produto.id}`, produto)
      .pipe(take(1), catchError(this.handleError));
  }

  editarGrupoProduto(grupoProduto: GrupoProduto) {
    return this.http
      .put(`grupos-produtos/put/${grupoProduto.id}`, grupoProduto)
      .pipe(take(1), catchError(this.handleError));
  }

  getProduto(produtoId: number): Observable<Produto> {
    return this.http
      .get<Produto>(`produto/get/${produtoId}`)
      .pipe(take(1), catchError(this.handleError));
  }

  getGrupoProduto(grupoProdutoId: number): Observable<GrupoProduto> {
    return this.http
      .get<GrupoProduto>(`grupos-produtos/get/${grupoProdutoId}`)
      .pipe(take(1), catchError(this.handleError));
  }

  putAtualizacaoPreco(atualizacao: AtualizaPreco) {
    return this.http
      .put('atualiza/put', atualizacao)
      .pipe(take(1), catchError(this.handleError));
  }

  getGrupoProdutoPaged(
    pager: Pager,
    grupoProduto: GrupoProduto
  ): Observable<any> {

    return this.http
      .put('grupos-produtos/getAllPaged', grupoProduto, {
        params: {
          page: pager.page || '',
          size: pager.size || '',
          filialId: this.filialId() || '',
        },
      })
      .pipe(take(1), catchError(this.handleError));
  }
  getProdutoPaged(pager: Pager, produto: Produto): Observable<any> {
    const params = new HttpParams()
      .set('page', pager.page || '')
      .set('size', pager.size || '')
      .set('filialId', this.filialId() || '');

    return this.http
      .put('produto/getAllPaged', produto, { params })
      .pipe(take(1), catchError(this.handleError));
  }

  postProduto(produto: Produto) {
    return this.http
      .post('produto/post', produto)
      .pipe(take(1), catchError(this.handleError));
  }
  constructor(private http: HttpClient) {}

  barcode(barcode: string): Observable<any> {
    return this.http
      .get(`/gtins/${barcode}.json`)
      .pipe(take(1), catchError(this.handleError));
  }
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
      .post('fornecedores/post', fornecedor)
      .pipe(take(1), catchError(this.handleError));
  }
  postGrupoProduto(grupoProduto: GrupoProduto) {
    return this.http
      .post('grupos-produtos/post', grupoProduto)
      .pipe(take(1), catchError(this.handleError));
  }
  getGrupoProdutoAllFilter(filialId?: number, nomeGrupo?: string) {
    let par = new HttpParams();
    if (filialId) {
      par = par.set('filialId', filialId);
    }
    if (nomeGrupo) {
      par = par.set('nomeGrupo', nomeGrupo);
    }
    return this.http
      .get('grupos-produtos/getAllFilter', { params: par })
      .pipe(take(1), catchError(this.handleError));
  }
  getFilialbyId(id: number): Observable<Filial> {
    return this.http.get<Filial>(`filiais/get/${id}`).pipe(take(1));
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
    return throwError(() => error);
  }
}
