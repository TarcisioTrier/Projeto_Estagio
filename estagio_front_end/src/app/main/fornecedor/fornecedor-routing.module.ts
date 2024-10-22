import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FornecedorComponent } from './fornecedor.component';
import { FornecedorCadastroComponent } from './fornecedor-cadastro/fornecedor-cadastro.component';
import { FornecedorListagemComponent } from './fornecedor-listagem/fornecedor-listagem.component';

const routes: Routes = [
  {path:'', component:FornecedorComponent},
  {path:'cadastro', component:FornecedorCadastroComponent},
  {path:'listagem', component:FornecedorListagemComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FornecedorRoutingModule { }
