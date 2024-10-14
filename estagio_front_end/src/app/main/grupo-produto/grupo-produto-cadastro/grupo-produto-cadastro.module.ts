import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GrupoProdutoCadastroComponent } from './grupo-produto-cadastro.component';
import { PrimengModule } from '../../../primeng/primeng.module';



@NgModule({
  declarations: [
    GrupoProdutoCadastroComponent
  ],
  imports: [
    CommonModule,
    PrimengModule
  ],
  exports:[
    GrupoProdutoCadastroComponent
  ]
})
export class GrupoProdutoCadastroModule { }
