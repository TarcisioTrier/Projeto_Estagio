import { DEFAULT_CURRENCY_CODE, LOCALE_ID, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LayoutModule } from "./main/layout/layout.module";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { PrimengModule } from './primeng/primeng.module';
import { ApiInterceptor } from './services/http/api-interceptor.service';
import { MessageService } from 'primeng/api';
import { registerLocaleData } from '@angular/common';
import ptBr from '@angular/common/locales/pt';
registerLocaleData(ptBr)

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    LayoutModule,
    BrowserAnimationsModule,
    HttpClientModule,
    PrimengModule
],
providers: [
  {
    provide: HTTP_INTERCEPTORS,
    useClass: ApiInterceptor,
    multi: true
  },
  { provide: DEFAULT_CURRENCY_CODE, useValue: 'BRL' },
  { provide: LOCALE_ID, useValue: 'pt-BR' },
  MessageService
],
  bootstrap: [AppComponent]
})
export class AppModule { }
