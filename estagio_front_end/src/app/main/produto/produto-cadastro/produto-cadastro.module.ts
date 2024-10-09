import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProdutoCadastroComponent } from './produto-cadastro.component';



@NgModule({
  declarations: [
    ProdutoCadastroComponent
  ],
  imports: [
    CommonModule
  ],
  exports:[
    ProdutoCadastroComponent
  ]
})
export class ProdutoCadastroModule { }
