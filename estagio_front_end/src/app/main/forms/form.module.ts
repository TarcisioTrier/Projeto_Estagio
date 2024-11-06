import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProdutoFormComponent } from './produto-form/produto-form.component';
import { PrimengModule } from '../../primeng/primeng.module';
import { GrupoProdutoFormComponent } from './grupo-produto-form/grupo-produto-form.component';
import { FornecedorFormComponent } from './fornecedor-form/fornecedor-form.component';
import { FilialFormComponent } from './filial-form/filial-form.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    ProdutoFormComponent,
    GrupoProdutoFormComponent,
    FornecedorFormComponent,
    FilialFormComponent,
  ],
  imports: [CommonModule, FormsModule, PrimengModule],
  exports: [
    ProdutoFormComponent,
    GrupoProdutoFormComponent,
    FornecedorFormComponent,
    FilialFormComponent,
  ],
})
export class FormModule {}
