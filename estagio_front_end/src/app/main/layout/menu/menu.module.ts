import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuComponent } from './menu.component';
import { AppRoutingModule } from '../../../app-routing.module';
import { MenubarModule } from 'primeng/menubar';



@NgModule({
  declarations: [
    MenuComponent
  ],
  imports: [
    CommonModule,
    AppRoutingModule,
    MenubarModule,

  ],
  exports:[
    MenuComponent
  ]
})
export class MenuModule { }
