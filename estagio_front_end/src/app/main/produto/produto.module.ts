import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProdutoRoutingModule } from './produto-routing.module';
import { ProdutoComponent } from './produto.component';
import { ProdutoCadastroModule } from './produto-cadastro/produto-cadastro.module';
import { ProdutoListagemModule } from './produto-listagem/produto-listagem.module';


@NgModule({
  declarations: [
    ProdutoComponent
  ],
  imports: [
    CommonModule,
    ProdutoRoutingModule,
    ProdutoCadastroModule,
    ProdutoListagemModule
  ]
})
export class ProdutoModule { }
