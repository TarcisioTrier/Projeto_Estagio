import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputMaskModule } from 'primeng/inputmask';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { MenuModule } from 'primeng/menu';
import { MenubarModule } from 'primeng/menubar';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { PanelMenuModule } from 'primeng/panelmenu';
import { SidebarModule } from 'primeng/sidebar';
import { TableModule } from 'primeng/table';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';

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
    TableModule,
  ],
  exports: [
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
    TableModule,
  ],
})
export class PrimengModule {}
