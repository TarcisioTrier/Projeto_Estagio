import { HttpService } from '../../services/http/http.service';
import { style } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { StylesService } from '../../services/styles.service';
import { Filial } from '../../models/filial';
import { AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import { Router } from '@angular/router';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
})
export class LayoutComponent implements OnInit {
  localFilial?: Filial;
  filiaisFilter: Filial[] = [];
  selectedItem: any;
  darkMode!: boolean;
  expanded = false;
  visible = false;
  imagem = 'https://www.triersistemas.com.br/imagens/logo_topo.png';
  saveFilial(filial: any) {
    this.localFilial = filial;
    sessionStorage.setItem('filial', JSON.stringify(this.localFilial));
    this.menuItem();
    this.selectedItem = undefined;
    this.visible = false;
    this.router.navigate(['']);
  }
  cancel() {
    this.visible = false;
  }
  filterItems(event: AutoCompleteCompleteEvent) {
    let query = event.query;
    this.http.getFilialFiltered(query).subscribe((filial) => {
      this.filiaisFilter = Object.values(filial);
      console.log(this.filiaisFilter);
    });
  }
  quit() {
    sessionStorage.removeItem('filial');
    this.localFilial = undefined;
    this.menuItem();
    this.router.navigate(['/filial/cadastro']);
  }

  showDialog() {
    this.visible = true;
  }
  constructor(private styleService: StylesService, private http: HttpService, private router:Router) {}
  ngOnInit(): void {
    this.darkMode = this.styleService.isDarkMode;
    const data = sessionStorage.getItem('filial');
    this.localFilial = data ? JSON.parse(data) : undefined;
    this.menuItem();

  }
  hoverTest(event: Event, hover: boolean) {
    var local = event.target as HTMLElement;

    if (hover) {
      local.style.color = 'var(--text-color)';
    } else {
      local.style.color = 'var(--text-color-secundary)';
    }
  }
  toggleMenu() {
    this.expanded = !this.expanded;
  }
  toggleMode() {
    this.styleService.toggleLightDark();
    this.darkMode = this.styleService.isDarkMode;
    if (this.darkMode) {
      document.body.classList.add('dark');
    } else {
      document.body.classList.remove('dark');
    }
  }

  menuItem() {
    this.items = [
      {
        label: 'Home',
        routerLink: 'inicial',
        command: () => {
          this.toggleMenu();
        },
      },
    ];
    if (!this.localFilial) {
      this.items.push({
        label: 'Filiais',
        routerLink: 'filial',
        items: [
          {
            label: 'Cadastro de Filiais',
            routerLink: 'filial/cadastro',
            command: () => {
              this.toggleMenu();
            },
          },
          {
            label: 'Listagem de Filiais',
            routerLink: 'filial/listagem',
            command: () => {
              this.toggleMenu();
            },
          },
        ],
      });
    } else {
      this.items.push({
        label: 'Grupo de Produto',
        routerLink: 'grupo-de-produto',
        items: [
          {
            label: 'Cadastro de Grupo de Produto',
            routerLink: 'grupo-de-produto/cadastro',
            command: () => {
              this.toggleMenu();
            },
          },
          {
            label: 'Listagem de Grupo de Produto',
            routerLink: 'grupo-de-produto/listagem',
            command: () => {
              this.toggleMenu();
            },
          },
        ],
      });
      this.items.push({
        label: 'Fornecedor',
        routerLink: 'fornecedor',
        items: [
          {
            label: 'Cadastro de Fornecedor',
            routerLink: 'fornecedor/cadastro',
            command: () => {
              this.toggleMenu();
            },
          },
          {
            label: 'Listagem de Fornecedor',
            routerLink: 'fornecedor/listagem',
            command: () => {
              this.toggleMenu();
            },
          },
        ],
      });
      this.items.push({
        label: 'Produto',
        routerLink: 'produto',
        items: [
          {
            label: 'Cadastro de Produto',
            routerLink: 'produto/cadastro',
            command: () => {
              this.toggleMenu();
            },
          },
          {
            label: 'Listagem de Produto',
            routerLink: 'produto/listagem',
            command: () => {
              this.toggleMenu();
            },
          },
        ],
      });
    }
  }

  items: MenuItem[] = [];
}
