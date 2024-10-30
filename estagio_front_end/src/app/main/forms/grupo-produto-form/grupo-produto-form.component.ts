import { enumToArray, SituacaoCadastro, TipoGrupo } from './../../../models/app-enums';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { GrupoProduto } from '../../../models/grupo-produto';

@Component({
  selector: 'app-grupo-produto-form',
  templateUrl: './grupo-produto-form.component.html',
  styleUrl: './grupo-produto-form.component.scss'
})
export class GrupoProdutoFormComponent implements OnInit {

  @Input() grupoProdutoPut?: GrupoProduto;
  @Output() grupoProdutoChange = new EventEmitter<GrupoProduto>();
  loading = false;
  situacaoCadastro = enumToArray(SituacaoCadastro);
  ngOnInit(): void {
    if(this.grupoProdutoPut !== undefined){
      const tipoGrupo = Object.values(TipoGrupo);
      const situacaoCadastro = Object.values(SituacaoCadastro);
      this.grupoProduto = {
        id: this.grupoProdutoPut.id,
        nomeGrupo: this.grupoProdutoPut.nomeGrupo,
        tipoGrupo: tipoGrupo.indexOf(this.grupoProdutoPut.tipoGrupo!),
        filialId: this.grupoProdutoPut.filialId,
        situacaoCadastro: situacaoCadastro.indexOf(this.grupoProdutoPut.situacaoCadastro!),
        atualizaPreco: this.grupoProdutoPut.atualizaPreco,
      }
    }else{
      this.grupoProduto = {
        situacaoCadastro: SituacaoCadastro.ATIVO,
      }
    }
  }
load() {
  this.loading = true;
  const data = sessionStorage.getItem('filial');
  const localFilial = data ? JSON.parse(data) : undefined;
  this.grupoProduto.filialId = localFilial.id;
  this.grupoProdutoChange.emit(this.grupoProduto);
  this.loading = false;
}


cadastroTipoGrupo(event: any) {
  this.grupoProduto.tipoGrupo = event.value;
}
  tipoGrupoProdutoSelecionado: any;
  grupoProduto: GrupoProduto = {
    atualizaPreco: false,
    situacaoCadastro: SituacaoCadastro.ATIVO
  }
tipoGrupoProduto = Object.keys(TipoGrupo)
.filter((key) => isNaN(Number(key)))
.map((status, index) => ({
  label: status.replace(/_/g, ' '),
  value: index,
}));


}
