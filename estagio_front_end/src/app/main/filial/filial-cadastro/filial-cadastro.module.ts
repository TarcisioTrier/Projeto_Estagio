import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilialCadastroComponent } from './filial-cadastro.component';
import { FormsModule } from '@angular/forms';
import { PrimengModule } from '../../../primeng/primeng.module';
import { FormModule } from '../../forms/form.module';
@NgModule({
  declarations: [FilialCadastroComponent],
  imports: [CommonModule, FormsModule, PrimengModule, FormModule],
  exports: [FilialCadastroComponent],
})
export class FilialCadastroModule {}
