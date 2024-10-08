import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GrupoProdutoRoutingModule } from './grupo-produto-routing.module';
import { GrupoProdutoComponent } from './grupo-produto.component';
import { GrupoProdutoCadastroModule } from './grupo-produto-cadastro/grupo-produto-cadastro.module';
import { GrupoProdutoListagemModule } from './grupo-produto-listagem/grupo-produto-listagem.module';


@NgModule({
  declarations: [
    GrupoProdutoComponent
  ],
  imports: [
    CommonModule,
    GrupoProdutoRoutingModule,
    GrupoProdutoCadastroModule,
    GrupoProdutoListagemModule
  ]
})
export class GrupoProdutoModule { }
