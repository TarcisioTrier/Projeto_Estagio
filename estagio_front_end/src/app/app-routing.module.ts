import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { NotFoundComponent } from './main/not-found/not-found.component';
import { AuthGuard } from './services/guard/auth-guard.guard';

const routes: Routes = [
  {
    path: 'inicial',
    redirectTo: '',
    pathMatch: 'full',
  },

  {
    path: 'filial',
    loadChildren: () =>
      import('./main/filial/filial.module').then((m) => m.FilialModule),
  },

  // Cadastro de Grupo de Produto
  {
    path: 'grupo-de-produto',
    loadChildren: () =>
      import('./main/grupo-produto/grupo-produto.module').then(
        (m) => m.GrupoProdutoModule
      ),
    canActivateChild: [AuthGuard],
  },

  // Cadastro de Fornecedor
  {
    path: 'fornecedor',
    loadChildren: () =>
      import('./main/fornecedor/fornecedor.module').then(
        (m) => m.FornecedorModule
      ),
    canActivateChild: [AuthGuard],
  },

  // Cadastro de Produto
  {
    path: 'produto',
    loadChildren: () =>
      import('./main/produto/produto.module').then((m) => m.ProdutoModule),
    canActivateChild: [AuthGuard],
  },

  // Cadastro de Especialidade Médica (BÔNUS)
  {
    path: 'especialidade',
    loadChildren: () =>
      import('./main/especialidade/especialidade.module').then(
        (m) => m.EspecialidadeModule
      ),
    canActivateChild: [AuthGuard],
  },

  // Cadastro de Médico (BÔNUS)
  {
    path: 'medico',
    loadChildren: () =>
      import('./main/medico/medico.module').then((m) => m.MedicoModule),
    canActivateChild: [AuthGuard],
  },

  // Atualização de Preço
  {
    path: 'atualizacao-de-preco',
    loadChildren: () =>
      import('./main/atualizacao-preco/atualizacao-preco.module').then(
        (m) => m.AtualizacaoPrecoModule
      ),
    canActivateChild: [AuthGuard],
  },

  {
    path: '',
    loadChildren: () =>
      import('./main/dashboard/dashboard.module').then(
        (m) => m.DashboardModule
      ),
  },

  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
