import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AtualizacaoPrecoComponent } from './atualizacao-preco.component';

const routes: Routes = [
  {path:'', component:AtualizacaoPrecoComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AtualizacaoPrecoRoutingModule { }
