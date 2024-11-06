import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FilialComponent } from './filial.component';

const routes: Routes = [
  { path: '', component: FilialComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FilialRoutingModule {}
