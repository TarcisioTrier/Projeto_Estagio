import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import { debounce } from 'lodash';
import { SituacaoCadastro, Apresentacao, TipoProduto, enumToArray } from '../../../models/app-enums';
import { verificaBarcode } from '../../../models/externalapi';
import { GrupoProduto } from '../../../models/grupo-produto';
import { Produto } from '../../../models/produto';
import { HttpService } from '../../../services/http/http.service';
import { MessageHandleService } from '../../../services/message-handle.service';
import { DropdownChangeEvent } from 'primeng/dropdown';

@Component({
  selector: 'app-produto-form',
  templateUrl: './produto-form.component.html',
  styleUrl: './produto-form.component.scss'
})
export class ProdutoFormComponent implements OnInit {

  @Input() produtoPut?: Produto;
  loading = false;
  @Output() produtoChange = new EventEmitter<Produto>();
  grupoProdutoFilter: GrupoProduto[] = [];
  produto!: Produto;

  constructor(
    private http: HttpService,
    private messageHandle: MessageHandleService
  ) {}
  ngOnInit(): void {
    if(this.produtoPut !== undefined){
      const tipoProduto = Object.values(TipoProduto);
      const apresentacao = Object.values(Apresentacao);
      const situacaoCadastro = Object.values(SituacaoCadastro);
          this.produto = {
            id: this.produtoPut.id,
            nome: this.produtoPut.nome,
            descricao: this.produtoPut.descricao,
            tipoProduto:tipoProduto.indexOf(this.produtoPut.tipoProduto!) ,
            grupoProdutoId: this.produtoPut.grupoProdutoId,
            codigoBarras: this.produtoPut.codigoBarras,
            margemLucro: this.produtoPut.margemLucro,
            atualizaPreco: this.produtoPut.atualizaPreco,
            valorProduto: this.produtoPut.valorProduto,
            apresentacao: apresentacao.indexOf(this.produtoPut.apresentacao!),
            situacaoCadastro: situacaoCadastro.indexOf(this.produtoPut.situacaoCadastro!),
            disabled: {
              nome: false,
              descricao: false,
            },
          }
          this.http.getGrupoProduto(this.produtoPut.grupoProdutoId!).subscribe({
            next: (data) => {
              this.produto.grupoProduto = data;
            }
          });
    }else{
      this.produto = {
        codigoBarras: '',
        descricao: '',
        valorVenda: 0,
        disabled: {
          nome: false,
          descricao: false,
        },
        atualizaPreco: false,
        situacaoCadastro: SituacaoCadastro.ATIVO,
      };
    }
  }

  load() {
    this.loading = true;
    this.produtoChange.emit(this.produto);
    this.loading = false;
  }

  apiBarcode = debounce(() => {
    const barcode = this.produto.codigoBarras!;

    if (verificaBarcode(barcode)) {
      this.http.barcode(barcode).subscribe({
        next: (retorno) => {
          this.produto.nome = retorno.description;
          this.produto.descricao = retorno.ncm.full_description;
          this.produto.disabled.nome = true;
          this.produto.disabled.descricao = true;
        },
        error: (erro) => {
          this.messageHandle.showErrorMessage('codigo de barra não encontrado');
        },
      });
    } else {
      this.messageHandle.showErrorMessage('código de barra inválido');
    }
  }, 300);

  filterItems(event: AutoCompleteCompleteEvent) {
    const data = sessionStorage.getItem('filial');
    const localFilial = data ? JSON.parse(data) : undefined;
    this.http.getGrupoProdutoAllFilter(localFilial.id, event.query).subscribe({
      next: (list) => {
        this.grupoProdutoFilter = Object.values(list);
      },
      error: (erro) => {
        this.messageHandle.showErrorMessage(erro);
      },
    });
  }

  apresentacao = enumToArray(Apresentacao);

  tipoProduto = enumToArray(TipoProduto);

  situacaoCadastro = enumToArray(SituacaoCadastro);

  verificaCodigoBarra(barcode: string) {
    return verificaBarcode(barcode);
  }
}
