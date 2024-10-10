import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilialCadastroComponent } from './filial-cadastro.component';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { FormsModule } from '@angular/forms';
import { InputMaskModule } from 'primeng/inputmask';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
@NgModule({
  declarations: [
    FilialCadastroComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    FloatLabelModule,
    InputMaskModule,
    DropdownModule,
    InputNumberModule
  ],
  exports:[
    FilialCadastroComponent
  ]
})
export class FilialCadastroModule { }
