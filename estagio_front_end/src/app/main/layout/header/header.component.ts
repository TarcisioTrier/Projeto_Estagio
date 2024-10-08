import { MenuItem, PrimeNGConfig } from 'primeng/api';
import { Component } from '@angular/core';
import { ToggleServiceService } from '../../../services/toggle-service.service';



@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
items: MenuItem[] = [
  {label:"Home", routerLink:"inicial"},{
    label:"Telas", items:[
      {label:"Filiais", routerLink:"filial", items:[
        {label:"Cadastro de Filiais", routerLink:'filial/cadastro'},
        {label:"Listagem de Filiais", routerLink:'filial/listagem'}
      ]},
      {label:"Grupo de Produto", routerLink:"grupo-de-produto", items:[
        {label:"Cadastro de Grupo de Produto", routerLink:'grupo-de-produto/cadastro'},
        {label:"Listagem de Grupo de Produto", routerLink:'grupo-de-produto/listagem'}
      ]},
      {label:"Fornecedor", routerLink:"fornecedor", items:[
        {label:"Cadastro de Fornecedor", routerLink:'fornecedor/cadastro'},
        {label:"Listagem de Fornecedor", routerLink:'fornecedor/listagem'}
      ]},
      {label:"Produto", routerLink:"produto", items:[
        {label:"Cadastro de Produto", routerLink:'produto/cadastro'},
        {label:"Listagem de Produto", routerLink:'produto/listagem'}
      ]}
    ]
}

];

  constructor(private toggleService: ToggleServiceService, private primengconfig: PrimeNGConfig) {}
  toggleMenu() {
    this.toggleService.menuHidden = !this.toggleService.menuHidden;
  }
  imagem = 'https://www.triersistemas.com.br/imagens/logo_topo.png';
}
