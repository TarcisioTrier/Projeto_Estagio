import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GrupoProdutoRoutingModule } from './grupo-produto-routing.module';
import { GrupoProdutoComponent } from './grupo-produto.component';


@NgModule({
  declarations: [
    GrupoProdutoComponent
  ],
  imports: [
    CommonModule,
    GrupoProdutoRoutingModule
  ]
})
export class GrupoProdutoModule { }
