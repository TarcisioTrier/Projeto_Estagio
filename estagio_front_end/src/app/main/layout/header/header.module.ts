import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header.component';
import { MenuModule } from '../menu/menu.module';
import { MenubarModule } from 'primeng/menubar';
import { AvatarModule } from 'primeng/avatar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';



@NgModule({
  declarations: [
    HeaderComponent
  ],
  imports: [
    CommonModule,
    MenuModule,
    MenubarModule,
    AvatarModule,
    BrowserAnimationsModule,
    ButtonModule,
    InputTextModule
  ],
  exports:[
    HeaderComponent
  ]
})
export class HeaderModule { }
