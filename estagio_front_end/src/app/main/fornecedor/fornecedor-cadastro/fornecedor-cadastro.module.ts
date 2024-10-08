import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FornecedorCadastroComponent } from './fornecedor-cadastro.component';



@NgModule({
  declarations: [
    FornecedorCadastroComponent
  ],
  imports: [
    CommonModule
  ],
  exports:[
    FornecedorCadastroComponent
  ]
})
export class FornecedorCadastroModule { }
