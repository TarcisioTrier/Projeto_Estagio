import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import { Filial } from '../../models/filial';
import { HttpService } from '../../services/http/http.service';
import { StylesService } from '../../services/styles.service';
import { set } from 'lodash';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
})
export class LayoutComponent implements OnInit {
  click() {
    const target = document.getElementsByClassName('click')[0] as HTMLElement;
    target.classList.add('click-1');
    setTimeout(() => {
      target.classList.remove('click-1');
      target.classList.add('click-2');
      setTimeout(() => {
        target.classList.remove('click-2');
        target.classList.add('click-3');
        setTimeout(() => {
          target.classList.remove('click-3');
        }, 50);
      }, 50);
    }, 50);
  }

  localFilial?: any;
  filiaisFilter: Filial[] = [];
  selectedItem: any;
  darkMode!: boolean;
  expanded = false;
  visible = false;
  imagem = 'src/assets/logo.png';
  items: MenuItem[] = [];
  activeIndex: number | null = null;

  constructor(
    private styleService: StylesService,
    private http: HttpService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.localFilial = this.http.filial()
    this.menuItem();
    const data = sessionStorage.getItem('darkTheme');
    if(data == undefined){
      sessionStorage.setItem('darkTheme', this.styleService.systemIsDark());
      window.location.reload();
    }
    this.darkMode = this.styleService.isDarkModeEnabled();
    if (this.darkMode) {
      document.body.classList.add('dark');
    } else {
      document.body.classList.remove('dark');
    }
  }

  saveFilial(filial: Filial) {
    this.localFilial = {id: filial.id, nomeFantasia: filial.nomeFantasia};
    sessionStorage.setItem('filial', JSON.stringify(this.localFilial));
    this.menuItem();
    this.visible = false;
    this.selectedItem = undefined;
    if (this.router.url == '/') {
      window.location.reload();
    } else {
      this.router.navigate(['/inicial']);
    }
  }

  filterItems(event: AutoCompleteCompleteEvent) {
    let query = event.query;
    this.http.getFilialFiltered(query).subscribe((filial) => {
      this.filiaisFilter = Object.values(filial);
    });
  }

  quit() {
    sessionStorage.removeItem('filial');
    this.localFilial = undefined;
    this.menuItem();
    this.router.navigate(['/filial']);
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
    this.darkMode = this.styleService.toggleLightDark();
    if (this.darkMode) {
      document.body.classList.add('dark');
    } else {
      document.body.classList.remove('dark');
    }
    window.location.reload();
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
      });
    } else {
      this.items.push({
        label: 'Grupo de Produto',
        routerLink: 'grupo-de-produto',
      });

      this.items.push({
        label: 'Fornecedor',
        routerLink: 'fornecedor',
      });

      this.items.push({
        label: 'Produto',
        routerLink: 'produto',
      });
      this.items.push({
        label: 'Atualização de Preço',
        routerLink: 'atualizacao-de-preco',
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
