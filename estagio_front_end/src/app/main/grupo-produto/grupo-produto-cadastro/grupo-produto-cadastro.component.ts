import { Component } from '@angular/core';
import { GrupoProduto } from '../../../models/grupo-produto';
import { SituacaoCadastro, TipoGrupoProduto } from '../../../models/app-enums';

@Component({
  selector: 'app-grupo-produto-cadastro',
  templateUrl: './grupo-produto-cadastro.component.html',
  styleUrl: './grupo-produto-cadastro.component.scss'
})
export class GrupoProdutoCadastroComponent {
cadastroTipoGrupo(event: any) {
  this.grupoProduto.tipoGrupo = event.value;
}
  tipoGrupoProdutoSelecionado: any;
  grupoProduto: GrupoProduto = {
    atualizaPreco: false,
    situacaoCadastro: SituacaoCadastro.ATIVO
  }
tipoGrupoProduto = Object.keys(TipoGrupoProduto)
.filter((key) => isNaN(Number(key)))
.map((status, index) => ({
  label: status.replace(/_/g, ' '),
  value: index,
}));
}
