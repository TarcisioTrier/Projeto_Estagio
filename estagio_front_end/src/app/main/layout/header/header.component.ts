import { Component } from '@angular/core';
import { ToggleServiceService } from '../../../services/toggle-service.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  constructor(private toggleService: ToggleServiceService) {}
  toggleMenu() {
    this.toggleService.menuHidden = !this.toggleService.menuHidden;
  }
  imagem = 'https://www.triersistemas.com.br/imagens/logo_topo.png';
}
