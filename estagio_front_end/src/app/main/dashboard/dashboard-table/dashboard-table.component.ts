import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard-table',
  templateUrl: './dashboard-table.component.html',
  styleUrl: './dashboard-table.component.scss'
})
export class DashboardTableComponent implements OnInit {

  filialId?: number;
  constructor() { }

  ngOnInit(): void {
    const data = sessionStorage.getItem('filial');
    const localFilial = data ? JSON.parse(data) : undefined;
    this.filialId = localFilial.id;
  }

}
