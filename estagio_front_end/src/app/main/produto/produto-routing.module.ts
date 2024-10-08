import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProdutoComponent } from './produto.component';
import { ProdutoCadastroComponent } from './produto-cadastro/produto-cadastro.component';
import { ProdutoListagemComponent } from './produto-listagem/produto-listagem.component';

const routes: Routes = [
  {path:'', component:ProdutoComponent},
  {path:'cadastro', component:ProdutoCadastroComponent},
  {path:'listagem', component:ProdutoListagemComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProdutoRoutingModule { }
