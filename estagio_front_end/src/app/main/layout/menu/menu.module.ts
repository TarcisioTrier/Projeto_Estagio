import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuComponent } from './menu.component';
import { AppRoutingModule } from '../../../app-routing.module';



@NgModule({
  declarations: [
    MenuComponent
  ],
  imports: [
    CommonModule,
    AppRoutingModule
  ],
  exports:[
    MenuComponent
  ]
})
export class MenuModule { }
