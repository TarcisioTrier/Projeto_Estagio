import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../../services/http/http.service';

@Component({
  selector: 'app-dashboard-table',
  templateUrl: './dashboard-table.component.html',
  styleUrl: './dashboard-table.component.scss',
})
export class DashboardTableComponent implements OnInit {
  filialId?: number;
  constructor(private http:HttpService) {}

  ngOnInit(): void {
    if(this.http.filialId() !== undefined)
    this.filialId = this.http.filialId();
  }
}
