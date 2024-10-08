import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GrupoProdutoComponent } from './grupo-produto.component';
import { GrupoProdutoCadastroComponent } from './grupo-produto-cadastro/grupo-produto-cadastro.component';
import { GrupoProdutoListagemComponent } from './grupo-produto-listagem/grupo-produto-listagem.component';

const routes: Routes = [
  {path:'', component:GrupoProdutoComponent},
  {path:'cadastro', component:GrupoProdutoCadastroComponent},
  {path:'listagem', component:GrupoProdutoListagemComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GrupoProdutoRoutingModule { }
