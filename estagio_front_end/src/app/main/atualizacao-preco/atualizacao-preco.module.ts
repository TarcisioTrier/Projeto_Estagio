import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AtualizacaoPrecoRoutingModule } from './atualizacao-preco-routing.module';
import { AtualizacaoPrecoComponent } from './atualizacao-preco.component';
import { PrimengModule } from '../../primeng/primeng.module';
import { FormModule } from '../forms/form.module';
import { AtualizacaoPrecoProdutoComponent } from './atualizacao-preco-produto/atualizacao-preco-produto.component';
import { AtualizacaoPrecoGrupoProdutoComponent } from './atualizacao-preco-grupo-produto/atualizacao-preco-grupo-produto.component';



@NgModule({
  declarations: [
    AtualizacaoPrecoComponent,
    AtualizacaoPrecoProdutoComponent,
    AtualizacaoPrecoGrupoProdutoComponent
  ],
  imports: [
    CommonModule,
    AtualizacaoPrecoRoutingModule,
    PrimengModule,
    FormModule
  ]
})
export class AtualizacaoPrecoModule { }
