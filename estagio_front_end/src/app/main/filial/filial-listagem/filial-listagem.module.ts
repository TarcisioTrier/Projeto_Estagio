import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilialListagemComponent } from './filial-listagem.component';



@NgModule({
  declarations: [
    FilialListagemComponent
  ],
  imports: [
    CommonModule
  ],
  exports:[
    FilialListagemComponent
  ]
})
export class FilialListagemModule { }
