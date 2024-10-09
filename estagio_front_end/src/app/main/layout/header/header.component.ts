import { style } from '@angular/animations';
import { StylesService } from './../../../services/styles.service';
import { MenuItem, PrimeNGConfig } from 'primeng/api';
import { Component, OnInit } from '@angular/core';



@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements OnInit {
  darkMode!: boolean;

toggleMode() {
  this.styleService.toggleLightDark();
  this.darkMode = this.styleService.isDarkMode
  if(this.darkMode){
    document.body.classList.add('dark')
  }else{
    document.body.classList.remove('dark')
  }
}
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

  constructor(private styleService: StylesService) {}
  ngOnInit(): void {
    this.darkMode = this.styleService.isDarkMode
  }


  imagem = 'https://www.triersistemas.com.br/imagens/logo_topo.png';
}
