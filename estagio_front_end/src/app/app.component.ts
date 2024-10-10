import { style } from '@angular/animations';
import { StylesService } from './services/styles.service';
import { Component, inject, OnInit } from '@angular/core';
import { PrimeNGConfig } from 'primeng/api';
import { DOCUMENT } from '@angular/common';
import { HttpService } from './services/http.service';
import { FilialPage } from './models/filial-page';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
    filialPage: FilialPage = {
      size: 3,
      page: 0,
      cnpj: '12.345.678/0002-'
    }
    constructor(private styleService: StylesService,private primengConfig: PrimeNGConfig, private http: HttpService) {}
    ngOnInit() {
        this.primengConfig.ripple = true;
        this.styleService.toggleLightDark();
        this.http.getFilialPaged(this.filialPage).subscribe(filiais => {
          console.log(filiais);
        })
    }

  title = 'estagio_front_end';
}
