import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AtualizacaoPrecoRoutingModule } from './atualizacao-preco-routing.module';
import { AtualizacaoPrecoComponent } from './atualizacao-preco.component';


@NgModule({
  declarations: [
    AtualizacaoPrecoComponent
  ],
  imports: [
    CommonModule,
    AtualizacaoPrecoRoutingModule
  ]
})
export class AtualizacaoPrecoModule { }
