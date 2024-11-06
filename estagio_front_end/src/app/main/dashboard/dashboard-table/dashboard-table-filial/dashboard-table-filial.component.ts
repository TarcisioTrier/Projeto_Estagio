import { ChangeDetectorRef, Component } from '@angular/core';
import { Filial } from '../../../../models/filial';
import { HttpService } from '../../../../services/http/http.service';
import { MessageHandleService } from '../../../../services/message-handle.service';
import { objectFix } from '../../../../models/externalapi';
import { ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-dashboard-table-filial',
  templateUrl: './dashboard-table-filial.component.html',
  styleUrl: './dashboard-table-filial.component.scss',
})
export class DashboardTableFilialComponent {
lastEvent:any;
remove(event: Event, item: any) {
  this.confirmationService.confirm({
    target: event.target as EventTarget,
    message: 'Você tem certeza que deseja deletar ' + item.nomeFantasia + ' ?',
    header: 'Deletar',
    icon: 'pi pi-info-circle',
    acceptButtonStyleClass:"p-button-danger p-button-text",
            rejectButtonStyleClass:"p-button-text p-button-text",
            acceptIcon:"none",
            rejectIcon:"none",
            accept: () => {
              this.http.removerFilial(item.id).subscribe({
                next: (data) =>{
                  this.messageHandler.showSuccessMessage( data.nomeFantasia + ' foi removido com sucesso.');
                  this.load(this.lastEvent)
                },
                error: (error)=>{
                  this.messageHandler.showErrorMessage(error.error);
                }
              })
          },
          reject: () => {
            this.messageHandler.showErrorMessage('Remoção cancelada');
          }
  })
}
  change(filial: Filial) {
    this.http.putFilial(filial).subscribe({
      next: (retorno) => {
        this.messageHandler.showSuccessMessage("Filial Atualizada com Sucesso");
      },
      error: (erro) => {
        this.messageHandler.showErrorMessage(erro);
      },
    });
    this.filiais.forEach((filial) => {
      filial.edit = false;
    });
  }
  editFilial(item: any) {
    item.edit = true;
  }
  itemField(item: any, field: string): any {
    if (field.includes('.')) {
      let fields = field.split('.');
      let value = item;
      for (let f of fields) {
        if(!value[f])
          return "";
        value = value[f];

      }

      return value;
    } else {
      return item[field];
    }
  }
  totalFiliais!: number;
  cols = [
    { field: 'nomeFantasia', header: 'Nome Fantasia' },
    { field: 'razaoSocial', header: 'Razão Social' },
    { field: 'email', header: 'Email' },
    { field: 'telefone', header: 'Telefone' },
    { field: 'cnpj', header: 'CNPJ' },
    { field: 'endereco.logradouro', header: 'Logradouro' },
    { field: 'endereco.numero', header: 'Número' },
    { field: 'endereco.complemento', header: 'Complemento' },
    { field: 'endereco.bairro', header: 'Bairro' },
    { field: 'endereco.cidade', header: 'Cidade' },
    { field: 'endereco.estado', header: 'Estado' },
    { field: 'endereco.cep', header: 'CEP' },
  ];

  selectedColumns = [
    { field: 'nomeFantasia', header: 'Nome Fantasia' },
    { field: 'razaoSocial', header: 'Razão Social' },
    { field: 'email', header: 'Email' },
    { field: 'telefone', header: 'Telefone' },
    { field: 'cnpj', header: 'CNPJ' },
    { field: 'endereco.cep', header: 'CEP' },
  ];
  load(event: any) {
    this.lastEvent = { ...event };
    this.filialFilter = objectFix(this.filialFilter, event);
    this.filialFilter.orderer = event.multiSortMeta;
    const page = event.first! / event.rows!;
    this.rows = event.rows!;
    const pager = { page: page, size: this.rows };
    this.http.getFilialPaged(pager, this.filialFilter).subscribe({
      next: (data) => {
        this.filiais = data.content;
        this.filiais.forEach((filial) => {
          filial.edit = false;
        });
        this.totalFiliais = data.totalElements;
      },
      error: (erro) => {
        this.messageHandler.showErrorMessage(erro);
      },
    });
  }
  filiais!: Filial[];
  filialFilter: Filial = {
    filter: new Map<string, string>(),
    disabled: {
      nomeFantasia: false,
      razaoSocial: false,
      email: false,
    },
    endereco: {
      disabled: {},
    },
  };
  rows: number = 10;
  constructor(
    private http: HttpService,
    private messageHandler: MessageHandleService,
    private confirmationService: ConfirmationService
  ) {}
}
