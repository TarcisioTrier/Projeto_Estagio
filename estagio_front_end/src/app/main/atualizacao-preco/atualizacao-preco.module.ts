import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AtualizacaoPrecoRoutingModule } from './atualizacao-preco-routing.module';
import { AtualizacaoPrecoComponent } from './atualizacao-preco.component';
import { PrimengModule } from '../../primeng/primeng.module';


@NgModule({
  declarations: [
    AtualizacaoPrecoComponent
  ],
  imports: [
    CommonModule,
    AtualizacaoPrecoRoutingModule,
    PrimengModule
  ]
})
export class AtualizacaoPrecoModule { }
