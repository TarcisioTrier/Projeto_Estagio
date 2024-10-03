import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from './layout.component';
import { HeaderModule } from './header/header.module';
import { AppRoutingModule } from '../../app-routing.module';
import { MenuModule } from './menu/menu.module';



@NgModule({
  declarations: [
    LayoutComponent
  ],
  imports: [
    CommonModule,
    HeaderModule,
    MenuModule,
    AppRoutingModule
],
exports:[
  LayoutComponent
]
})
export class LayoutModule { }
