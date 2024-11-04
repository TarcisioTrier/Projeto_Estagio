import { StylesService } from './services/styles.service';
import { Component, inject, OnInit } from '@angular/core';
import { PrimeNGConfig } from 'primeng/api';
import { HttpService } from './services/http/http.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  constructor(
    private styleService: StylesService,
    private primengConfig: PrimeNGConfig,
    private http: HttpService
  ) {}
  ngOnInit() {
    this.primengConfig.ripple = true;
    this.styleService.toggleLightDark();
  }

  title = 'estagio_front_end';
}
