import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GrupoProdutoCadastroComponent } from './grupo-produto-cadastro.component';



@NgModule({
  declarations: [
    GrupoProdutoCadastroComponent
  ],
  imports: [
    CommonModule
  ],
  exports:[
    GrupoProdutoCadastroComponent
  ]
})
export class GrupoProdutoCadastroModule { }
