import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FornecedorCadastroComponent } from './fornecedor-cadastro.component';
import { PrimengModule } from '../../../primeng/primeng.module';
import { FormModule } from '../../forms/form.module';



@NgModule({
  declarations: [
    FornecedorCadastroComponent
  ],
  imports: [
    CommonModule,
    FormModule
  ],
  exports:[
    FornecedorCadastroComponent
  ]
})
export class FornecedorCadastroModule { }
