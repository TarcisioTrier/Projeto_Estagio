import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GrupoProdutoCadastroComponent } from './grupo-produto-cadastro.component';
import { PrimengModule } from '../../../primeng/primeng.module';
import { HttpService } from '../../../services/http/http.service';
import { MessageService } from 'primeng/api';
import { FormModule } from '../../forms/form.module';



@NgModule({
  declarations: [
    GrupoProdutoCadastroComponent
  ],
  imports: [
    CommonModule,
    PrimengModule,
    FormModule
  ],
  providers: [HttpService, MessageService],
  exports:[
    GrupoProdutoCadastroComponent
  ]

})
export class GrupoProdutoCadastroModule { }
