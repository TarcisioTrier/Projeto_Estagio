import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilialCadastroComponent } from './filial-cadastro.component';
import { FormsModule } from '@angular/forms';
import { PrimengModule } from '../../../primeng/primeng.module';
@NgModule({
  declarations: [
    FilialCadastroComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    PrimengModule
  ],
  exports:[
    FilialCadastroComponent
  ]
})
export class FilialCadastroModule { }
