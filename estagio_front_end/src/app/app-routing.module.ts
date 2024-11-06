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

  {
    path: 'grupo-de-produto',
    loadChildren: () =>
      import('./main/grupo-produto/grupo-produto.module').then(
        (m) => m.GrupoProdutoModule
      ),
    canActivateChild: [AuthGuard],
  },

  {
    path: 'fornecedor',
    loadChildren: () =>
      import('./main/fornecedor/fornecedor.module').then(
        (m) => m.FornecedorModule
      ),
    canActivateChild: [AuthGuard],
  },

  {
    path: 'produto',
    loadChildren: () =>
      import('./main/produto/produto.module').then((m) => m.ProdutoModule),
    canActivateChild: [AuthGuard],
  },

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
