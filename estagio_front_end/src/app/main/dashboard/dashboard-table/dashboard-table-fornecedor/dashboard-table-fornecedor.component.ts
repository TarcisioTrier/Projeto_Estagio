import { Component } from '@angular/core';
import { objectFix } from '../../../../models/externalapi';
import { Filial } from '../../../../models/filial';
import { HttpService } from '../../../../services/http/http.service';
import { MessageHandleService } from '../../../../services/message-handle.service';
import { Fornecedor } from '../../../../models/fornecedor';

@Component({
  selector: 'app-dashboard-table-fornecedor',
  templateUrl: './dashboard-table-fornecedor.component.html',
  styleUrl: './dashboard-table-fornecedor.component.scss',
})
export class DashboardTableFornecedorComponent {
  editFornecedor(item: any) {
    item.edit = true;
  }
  change(fornecedor: Fornecedor) {
    this.http.putForncedor(fornecedor).subscribe({
      next: (retorno) => {
        this.messageHandler.showCadastroMessage(retorno);
      },
      error: (erro) => {
        this.messageHandler.showErrorMessage(erro);
      },
    });
    this.fornecedores.forEach((fornecedor) => {
      fornecedor.edit = false;
    });
  }

  totalFornecedores!: number;
  cols = [
    { field: 'nomeFantasia', header: 'Nome Fantasia' },
    { field: 'razaoSocial', header: 'Razão Social' },
    { field: 'email', header: 'Email' },
    { field: 'telefone', header: 'Telefone' },
    { field: 'cnpj', header: 'CNPJ' },
  ];

  selectedColumns = [
    { field: 'nomeFantasia', header: 'Nome Fantasia' },
    { field: 'razaoSocial', header: 'Razão Social' },
    { field: 'email', header: 'Email' },
    { field: 'telefone', header: 'Telefone' },
    { field: 'cnpj', header: 'CNPJ' },
  ];
  load(event: any) {
    this.fornecedorFilter = objectFix(this.fornecedorFilter, event);
    this.fornecedorFilter.orderer = event.multiSortMeta;
    const page = event.first! / event.rows!;
    this.rows = event.rows!;
    const pager = { page: page, size: this.rows };
    this.http.getFornecedorPaged(pager, this.fornecedorFilter).subscribe({
      next: (data) => {
        this.fornecedores = data.content;
        this.fornecedores.forEach((fornecedor) => {
          fornecedor.edit = false;
        });
        this.totalFornecedores = data.totalElements;
      },
      error: (erro) => {
        this.messageHandler.showErrorMessage(erro);
      },
    });
  }
  fornecedores!: Fornecedor[];
  fornecedorFilter: Fornecedor = {
    filter: new Map<string, string>(),
    disabled: {
      nomeFantasia: false,
      razaoSocial: false,
      email: false,
    },
  };
  rows: number = 10;
  itemField(item: any, field: string): any {
    if (field.includes('.')) {
      let fields = field.split('.');
      let value = item;
      for (let f of fields) {
        value = value[f];
      }
      return value;
    } else {
      return item[field];
    }
  }
  constructor(
    private http: HttpService,
    private messageHandler: MessageHandleService
  ) {}
}
