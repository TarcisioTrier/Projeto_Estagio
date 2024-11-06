import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LayoutComponent } from './layout.component';
import { AppRoutingModule } from '../../app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PrimengModule } from '../../primeng/primeng.module';
import { ConfirmationService } from 'primeng/api';

@NgModule({
  declarations: [
    LayoutComponent
  ],
  imports: [
    CommonModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    PrimengModule
],
providers:[ConfirmationService]
,
exports:[
  LayoutComponent
]
})
export class LayoutModule { }
