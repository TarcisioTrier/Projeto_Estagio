import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GrupoProdutoListagemComponent } from './grupo-produto-listagem.component';



@NgModule({
  declarations: [
    GrupoProdutoListagemComponent
  ],
  imports: [
    CommonModule
  ],
  exports:[
    GrupoProdutoListagemComponent
  ]
})
export class GrupoProdutoListagemModule { }
