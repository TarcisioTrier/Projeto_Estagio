import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GrupoProdutoCadastroComponent } from './grupo-produto-cadastro.component';
import { PrimengModule } from '../../../primeng/primeng.module';
import { HttpService } from '../../../services/http.service';
import { MessageService } from 'primeng/api';



@NgModule({
  declarations: [
    GrupoProdutoCadastroComponent
  ],
  imports: [
    CommonModule,
    PrimengModule
  ],
  providers: [HttpService, MessageService],
  exports:[
    GrupoProdutoCadastroComponent
  ]

})
export class GrupoProdutoCadastroModule { }
