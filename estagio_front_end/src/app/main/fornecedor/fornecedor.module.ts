import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FornecedorRoutingModule } from './fornecedor-routing.module';
import { FornecedorComponent } from './fornecedor.component';
import { FormModule } from '../forms/form.module';


@NgModule({
  declarations: [
    FornecedorComponent
  ],
  imports: [
    CommonModule,
    FornecedorRoutingModule,
    FormModule
  ]
})
export class FornecedorModule { }
