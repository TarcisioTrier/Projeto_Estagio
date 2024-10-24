import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import { Filial } from '../../models/filial';
import { HttpService } from '../../services/http/http.service';
import { StylesService } from '../../services/styles.service';

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
  items: MenuItem[] = [];

  constructor(
    private styleService: StylesService,
    private http: HttpService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.darkMode = this.styleService.isDarkMode;
    const data = sessionStorage.getItem('filial');
    this.localFilial = data ? JSON.parse(data) : undefined;
    this.menuItem();
  }

  saveFilial(filial: Filial) {
    this.localFilial = filial;
    sessionStorage.setItem('filial', JSON.stringify(this.localFilial));
    this.menuItem();
    this.visible = false;
    this.selectedItem = undefined;
    this.router.navigate(['']);
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

  hoverTest(event: Event, hover: boolean) {
    var local = event.target as HTMLElement;

    if (hover) {
      local.style.color = 'var(--text-color)';
    } else {
      local.style.color = 'var(--text-color-secundary)';
    }
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

  toggleMenu() {
    this.expanded = !this.expanded;
  }

  showDialog() {
    this.visible = true;
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
          this.createMenuItem('Cadastro de Filiais', 'filial/cadastro'),
          this.createMenuItem('Listagem de Filiais', 'filial/listagem'),
        ],
      });
    } else {
      this.items.push({
        label: 'Grupo de Produto',
        routerLink: 'grupo-de-produto',
        items: [
          this.createMenuItem(
            'Cadastro de grupo de produtos',
            'grupo-de-produto/cadastro'
          ),
          this.createMenuItem(
            'Listagem de grupo de produtos',
            'grupo-de-produto/listagem'
          ),
        ],
      });

      this.items.push({
        label: 'Fornecedor',
        routerLink: 'fornecedor',
        items: [
          this.createMenuItem('Cadastro de fornecedor', 'fornecedor/cadastro'),
          this.createMenuItem('Listagem de fornecedor', 'fornecedor/listagem'),
        ],
      });

      this.items.push({
        label: 'Produto',
        routerLink: 'produto',
        items: [
          this.createMenuItem('Cadastro de Produto', 'produto/cadastro'),
          this.createMenuItem('Listagem de Produto', 'produto/listagem'),
        ],
      });
    }
  }

  private createMenuItem(label: string, routerLink: string): MenuItem {
    return {
      label,
      routerLink,
      command: () => this.toggleMenu(),
    };
  }
}
