import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FornecedorListagemComponent } from './fornecedor-listagem.component';



@NgModule({
  declarations: [
    FornecedorListagemComponent
  ],
  imports: [
    CommonModule
  ],
  exports:[
    FornecedorListagemComponent
  ]
})
export class FornecedorListagemModule { }
