import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EspecialidadeRoutingModule } from './especialidade-routing.module';
import { EspecialidadeComponent } from './especialidade.component';


@NgModule({
  declarations: [
    EspecialidadeComponent
  ],
  imports: [
    CommonModule,
    EspecialidadeRoutingModule
  ]
})
export class EspecialidadeModule { }
