import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FornecedorCadastroComponent } from './fornecedor-cadastro.component';
import { PrimengModule } from '../../../primeng/primeng.module';



@NgModule({
  declarations: [
    FornecedorCadastroComponent
  ],
  imports: [
    CommonModule,
    PrimengModule
  ],
  exports:[
    FornecedorCadastroComponent
  ]
})
export class FornecedorCadastroModule { }
