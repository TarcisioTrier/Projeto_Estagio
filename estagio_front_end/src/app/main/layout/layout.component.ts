import { HttpService } from './../../services/http.service';
import { style } from '@angular/animations';
import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { StylesService } from '../../services/styles.service';
import { Filial } from '../../models/filial';
import { AutoCompleteCompleteEvent } from 'primeng/autocomplete';


@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {
filterItems(event: AutoCompleteCompleteEvent) {
  let query = event.query;
  this.http.getFilialFiltered(query).subscribe(filial => {
    this.filiaisFilter = Object.values(filial)
    console.log(this.filiaisFilter);
  })
}
  filiais!: Array<Filial>;
  filiaisFilter: any[] = [];
  selectedItem: any;
  darkMode!: boolean;
  expanded = false;

  constructor(private styleService: StylesService, private http: HttpService) {}
  ngOnInit(): void {
    this.darkMode = this.styleService.isDarkMode

  }
  hoverTest(event: Event, hover: boolean){
    var local =  (event.target as HTMLElement);

      if (hover){
        local.style.color = "var(--text-color)"
      }else{
        local.style.color = "var(--text-color-secundary)"
      }
  }
  toggleMenu() {
    this.expanded = !this.expanded;
    }
  toggleMode() {
    this.styleService.toggleLightDark();
    this.darkMode = this.styleService.isDarkMode
    if(this.darkMode){
      document.body.classList.add('dark')
    }else{
      document.body.classList.remove('dark')
    }
  }


  imagem = 'https://www.triersistemas.com.br/imagens/logo_topo.png';
  items: MenuItem[] = [
    {label:"Home", routerLink:"inicial", command: ()=> {this.toggleMenu()}},
        {label:"Filiais", routerLink:"filial", items:[
          {label:"Cadastro de Filiais", routerLink:'filial/cadastro', command: ()=> {this.toggleMenu()}},
          {label:"Listagem de Filiais", routerLink:'filial/listagem', command: ()=> {this.toggleMenu()}}
        ]},
        {label:"Grupo de Produto", routerLink:"grupo-de-produto", items:[
          {label:"Cadastro de Grupo de Produto", routerLink:'grupo-de-produto/cadastro', command: ()=> {this.toggleMenu()}},
          {label:"Listagem de Grupo de Produto", routerLink:'grupo-de-produto/listagem', command: ()=> {this.toggleMenu()}}
        ]},
        {label:"Fornecedor", routerLink:"fornecedor", items:[
          {label:"Cadastro de Fornecedor", routerLink:'fornecedor/cadastro', command: ()=> {this.toggleMenu()}},
          {label:"Listagem de Fornecedor", routerLink:'fornecedor/listagem', command: ()=> {this.toggleMenu()}}
        ]},
        {label:"Produto", routerLink:"produto", items:[
          {label:"Cadastro de Produto", routerLink:'produto/cadastro', command: ()=> {this.toggleMenu()}},
          {label:"Listagem de Produto", routerLink:'produto/listagem', command: ()=> {this.toggleMenu()}}
        ]}

  ];
}
