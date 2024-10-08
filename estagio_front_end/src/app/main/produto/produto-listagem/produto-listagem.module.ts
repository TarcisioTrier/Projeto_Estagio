import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProdutoListagemComponent } from './produto-listagem.component';



@NgModule({
  declarations: [
    ProdutoListagemComponent
  ],
  imports: [
    CommonModule
  ],
  exports:[
    ProdutoListagemComponent
  ]
})
export class ProdutoListagemModule { }
