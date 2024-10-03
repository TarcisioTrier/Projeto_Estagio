import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToggleServiceService } from '../../../services/toggle-service.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit{
  constructor(private router: Router, private toggleService: ToggleServiceService) {}
  menuHidden = false;
  ngOnInit(): void {
    setInterval(() => {
      this.menuHidden = this.toggleService.menuHidden;
    }, 100);
  }

  isCurrentRoute(route: string): boolean {
    return this.router.isActive(route, {
      paths: 'subset',
      queryParams: 'subset',
      fragment: 'ignored',
      matrixParams: 'ignored'
    });
  }
}
