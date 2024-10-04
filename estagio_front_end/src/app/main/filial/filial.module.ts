import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FilialRoutingModule } from './filial-routing.module';
import { FilialComponent } from './filial.component';


@NgModule({
  declarations: [
    FilialComponent
  ],
  imports: [
    CommonModule,
    FilialRoutingModule
  ]
})
export class FilialModule { }
