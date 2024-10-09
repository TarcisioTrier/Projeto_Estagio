import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FornecedorRoutingModule } from './fornecedor-routing.module';
import { FornecedorComponent } from './fornecedor.component';
import { FornecedorCadastroModule } from './fornecedor-cadastro/fornecedor-cadastro.module';
import { FornecedorListagemModule } from './fornecedor-listagem/fornecedor-listagem.module';


@NgModule({
  declarations: [
    FornecedorComponent
  ],
  imports: [
    CommonModule,
    FornecedorRoutingModule,
    FornecedorCadastroModule,
    FornecedorListagemModule
  ]
})
export class FornecedorModule { }
