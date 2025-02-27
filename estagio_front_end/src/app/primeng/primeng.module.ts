import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { MenubarModule } from 'primeng/menubar';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { PanelMenuModule } from 'primeng/panelmenu';
import { SidebarModule } from 'primeng/sidebar';
import { ToolbarModule } from 'primeng/toolbar';
import { MenuModule } from 'primeng/menu';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { FormsModule } from '@angular/forms';
import { InputMaskModule } from 'primeng/inputmask';
import { DropdownModule } from 'primeng/dropdown';
import { InputNumberModule } from 'primeng/inputnumber';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';
import { CheckboxModule } from 'primeng/checkbox';
import { TableModule } from 'primeng/table';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MenuModule,
    MenubarModule,
    TieredMenuModule,
    ButtonModule,
    PanelMenuModule,
    SidebarModule,
    ToolbarModule,
    AutoCompleteModule,
    FormsModule,
    OverlayPanelModule,
    FormsModule,
    InputTextModule,
    FloatLabelModule,
    InputMaskModule,
    DropdownModule,
    InputNumberModule,
    ToastModule,
    DialogModule,
    CheckboxModule,
    TableModule
  ],
  exports:[
    MenuModule,
    MenubarModule,
    TieredMenuModule,
    ButtonModule,
    PanelMenuModule,
    SidebarModule,
    ToolbarModule,
    AutoCompleteModule,
    FormsModule,
    OverlayPanelModule,
    FormsModule,
    InputTextModule,
    FloatLabelModule,
    InputMaskModule,
    DropdownModule,
    InputNumberModule,
    ToastModule,
    DialogModule,
    CheckboxModule,
    TableModule
  ]
})
export class PrimengModule { }
