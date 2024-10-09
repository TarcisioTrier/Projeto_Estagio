import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FilialComponent } from './filial.component';
import { FilialCadastroComponent } from './filial-cadastro/filial-cadastro.component';
import { FilialListagemComponent } from './filial-listagem/filial-listagem.component';

const routes: Routes = [
  {path:'', component:FilialComponent},
  {path:'cadastro', component:FilialCadastroComponent},
  {path:'listagem', component:FilialListagemComponent}

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FilialRoutingModule { }
