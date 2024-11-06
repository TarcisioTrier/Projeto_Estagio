import { Component } from '@angular/core';
import { GrupoProduto } from '../../../../models/grupo-produto';
import { objectFix } from '../../../../models/externalapi';
import { HttpService } from '../../../../services/http/http.service';
import { MessageHandleService } from '../../../../services/message-handle.service';
import { enumToArray, TipoGrupo } from '../../../../models/app-enums';
import { ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-dashboard-table-grupo-produto',
  templateUrl: './dashboard-table-grupo-produto.component.html',
  styleUrl: './dashboard-table-grupo-produto.component.scss',
})
export class DashboardTableGrupoProdutoComponent {

  lastEvent:any;
  remove(event: Event, item: any) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Você tem certeza que deseja deletar ' + item.nomeGrupo + ' ?',
      header: 'Deletar',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass:"p-button-danger p-button-text",
              rejectButtonStyleClass:"p-button-text p-button-text",
              acceptIcon:"none",
              rejectIcon:"none",
              accept: () => {
                this.http.removerGrupoProduto(item.id).subscribe({
                  next: (data) =>{
                    this.messageHandler.showSuccessMessage( data.nomeGrupo + ' foi removido com sucesso.');
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
  enumOptions(): any[] {
    return enumToArray(TipoGrupo);
  }
  isEnum(item: any): any {
    return item.field === 'tipoGrupo';
  }
  isPercent(item: any): boolean {
    return item.field === 'margemLucro';
  }
  isBool(item: any): boolean {
    return item.field === 'atualizaPreco';
  }

  editGrupoProduto(item: any) {
    item.edit = true;
  }
  change(grupoProduto: GrupoProduto) {
    this.http.editarGrupoProduto(grupoProduto).subscribe({
      next: (retorno) => {
        this.messageHandler.showSuccessMessage("Grupo de Produto Atualizado com Sucesso");
      },
      error: (erro) => {
        this.messageHandler.showErrorMessage(erro);
      },
    });
    this.gruposProduto.forEach((grupoProduto) => {
      grupoProduto.edit = false;
    });
  }

  totalGruposProduto!: number;
  cols = [
    { field: 'nomeGrupo', header: 'Nome Do Grupo Produto' },
    { field: 'tipoGrupo', header: 'Tipo de Grupo Produto' },
    { field: 'margemLucro', header: 'Margem de Lucro' },
    { field: 'atualizaPreco', header: 'Atualização de Preço' },
  ];

  selectedColumns = this.cols;
  load(event: any) {
    this.lastEvent = { ...event };
    this.grupoProdutoFilter = objectFix(this.grupoProdutoFilter, event);
    this.grupoProdutoFilter.orderer = event.multiSortMeta;
    const page = event.first! / event.rows!;
    this.rows = event.rows!;
    const pager = { page: page, size: this.rows };
    this.http.getGrupoProdutoPaged(pager, this.grupoProdutoFilter).subscribe({
      next: (data) => {
        this.gruposProduto = data.content;
        this.gruposProduto.forEach((grupoProduto) => {
          grupoProduto.edit = false;
        });
        this.totalGruposProduto = data.totalElements;
      },
      error: (erro) => {
        this.messageHandler.showErrorMessage(erro);
      },
    });
  }
  gruposProduto!: GrupoProduto[];
  grupoProdutoFilter: GrupoProduto = {
    filter: new Map<string, string>(),
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
    private messageHandler: MessageHandleService,
    private confirmationService: ConfirmationService
  ) {}
}
