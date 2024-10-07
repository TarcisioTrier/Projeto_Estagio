import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToggleServiceService } from '../../../services/toggle-service.service';
import { Menu, Paginas } from '../../../models/menu';
import { TitleCasePipe } from '@angular/common';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss',
})
export class MenuComponent implements OnInit {
  menuHidden = false;
  menu: Array<Menu> = [];
  menuLocal = [
    {id:'inicial',paginas:[]},
    {id:'filiais', paginas:['cadastro','listagem']},
    {id:'grupo-de-produto', paginas:['cadastro','listagem']},
    {id:'fornecedor', paginas:['cadastro','listagem']},
    {id:'produto', paginas:['cadastro','listagem']},
    {id:'especialista', paginas:['cadastro','listagem','verificacao']},
    {id:'medico', paginas:['casdastro','listagem','vedacao']},
    {id:'atualizacao-de-produto',paginas:['cadastro']}
  ];
  getPaginas(i: any){
    var pages: Array<Paginas> = [];
    for(var item of this.menuLocal[i].paginas){
      pages.push({id: item,url:`${this.menuLocal[i].id}/${item}`,info:`${item} de ${this.createNome(this.menuLocal[i].id)}`});
    }
    return pages;
  }
pageConstructor(){
  for(var index in this.menuLocal){
    this.menu.push({id: this.menuLocal[index].id, nome: this.createNome(this.menuLocal[index].id), paginas: this.getPaginas(index)
  })
  }
}
createNome(id: string){
  var nome = id[0].toUpperCase() + id.substring(1).toLowerCase()
  if(nome.includes('-')){
    console.log(nome);
    nome = nome.replaceAll('-', ' ')
  }
  return nome
}


  constructor(
    private router: Router,
    private toggleService: ToggleServiceService
  ) {}

  ngOnInit(): void {
    this.pageConstructor();
    setInterval(() => {
      this.menuHidden = this.toggleService.menuHidden;
    }, 100);
  }
  hideMenu() {
    this.toggleService.menuHidden = true;
  }
  isCurrentRoute(route: string): boolean {
    return this.router.isActive(route, {
      paths: 'subset',
      queryParams: 'subset',
      fragment: 'ignored',
      matrixParams: 'ignored',
    });
  }
}

