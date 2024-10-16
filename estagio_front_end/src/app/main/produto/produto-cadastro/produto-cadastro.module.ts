import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProdutoCadastroComponent } from './produto-cadastro.component';
import { PrimengModule } from '../../../primeng/primeng.module';



@NgModule({
  declarations: [
    ProdutoCadastroComponent
  ],
  imports: [
    CommonModule,
    PrimengModule
  ],
  exports:[
    ProdutoCadastroComponent
  ]
})
export class ProdutoCadastroModule { }
