import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GrupoProdutoRoutingModule } from './grupo-produto-routing.module';
import { GrupoProdutoComponent } from './grupo-produto.component';
import { FormModule } from '../forms/form.module';
import { MessageService } from 'primeng/api';
import { HttpService } from '../../services/http/http.service';


@NgModule({
  declarations: [
    GrupoProdutoComponent
  ],
  imports: [
    CommonModule,
    GrupoProdutoRoutingModule,
    FormModule
  ],
  providers: [HttpService, MessageService],
})
export class GrupoProdutoModule { }
