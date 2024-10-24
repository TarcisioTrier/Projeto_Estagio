import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { LayoutModule } from './main/layout/layout.module';
import { PrimengModule } from './primeng/primeng.module';

import { MessageService } from 'primeng/api';
import { ApiInterceptor } from './services/http/api-interceptor.service';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,

    AppRoutingModule,

    LayoutModule,

    HttpClientModule,
    PrimengModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ApiInterceptor,
      multi: true,
    },

    MessageService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
