import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { DashboardTableComponent } from './dashboard-table/dashboard-table.component';
import { DashboardTableProdutoComponent } from './dashboard-table/dashboard-table-produto/dashboard-table-produto.component';
import { DashboardTableGrupoProdutoComponent } from './dashboard-table/dashboard-table-grupo-produto/dashboard-table-grupo-produto.component';
import { DashboardTableFilialComponent } from './dashboard-table/dashboard-table-filial/dashboard-table-filial.component';
import { DashboardTableFornecedorComponent } from './dashboard-table/dashboard-table-fornecedor/dashboard-table-fornecedor.component';
import { PrimengModule } from '../../primeng/primeng.module';


@NgModule({
  declarations: [
    DashboardComponent,
    DashboardTableComponent,
    DashboardTableProdutoComponent,
    DashboardTableGrupoProdutoComponent,
    DashboardTableFilialComponent,
    DashboardTableFornecedorComponent,
  ],
  imports: [
    CommonModule,
    DashboardRoutingModule,
    PrimengModule
  ]
})
export class DashboardModule { }
