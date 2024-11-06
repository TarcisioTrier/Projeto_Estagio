import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FilialRoutingModule } from './filial-routing.module';
import { FilialComponent } from './filial.component';
import { FormModule } from '../forms/form.module';
import { MessageService } from 'primeng/api';
import { HttpService } from '../../services/http/http.service';


@NgModule({
  declarations: [
    FilialComponent
  ],
  imports: [
    CommonModule,
    FilialRoutingModule,
    FormModule
  ],
  providers: [HttpService, MessageService],
})
export class FilialModule { }
