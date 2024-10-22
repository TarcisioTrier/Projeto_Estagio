import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FilialRoutingModule } from './filial-routing.module';
import { FilialComponent } from './filial.component';
import { FilialCadastroModule } from './filial-cadastro/filial-cadastro.module';
import { FilialListagemModule } from './filial-listagem/filial-listagem.module';


@NgModule({
  declarations: [
    FilialComponent
  ],
  imports: [
    CommonModule,
    FilialRoutingModule,
    FilialCadastroModule,
    FilialListagemModule
  ]
})
export class FilialModule { }
