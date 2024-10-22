import { Component } from '@angular/core';
import { Fornecedor } from '../../../models/fornecedor';
import { MessageService } from 'primeng/api';
import { HttpService } from '../../../services/http/http.service';
import { SituacaoContrato } from '../../../models/app-enums';
import { Cnpj, cnpjtoFornecedor, validateCnpj } from '../../../models/externalapi';

@Component({
  selector: 'app-fornecedor-cadastro',
  templateUrl: './fornecedor-cadastro.component.html',
  styleUrl: './fornecedor-cadastro.component.scss'
})
export class FornecedorCadastroComponent {
  loading = false;
  load() {
    this.loading = true;
    const data = sessionStorage.getItem('filial');
    const localFilial = data ? JSON.parse(data) : undefined;
    this.fornecedor.filialId = localFilial.id;
    this.http.postFornecedor(this.fornecedor).subscribe({
      next: (retorno) => {
        console.log(retorno);
        this.messageService.add({
          severity: 'success',
          summary: 'Successo',
          detail: 'Cadastrado com Sucesso',
        });
      },
      error: (erro) => {
        let error = erro.error;
        if (typeof error === 'string') {
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: error,
          });
        } else {
          let erros = Object.values(error);
          for (let erro of erros) {
            this.messageService.add({
              severity: 'error',
              summary: 'Erro',
              detail: String(erro),
            });
          }
        }
      },
      complete: () => {
        this.loading = false;
      },
    });
  }
cnpj() {
  const cnpj = this.fornecedor.cnpj.replace(/[_./-]/g, '');
  console.log(cnpj);
  if (validateCnpj(cnpj)) {
    this.http.buscaCNPJ(cnpj).subscribe({
      next: (obj: Cnpj) => {
        console.log(obj);
        if (obj.error) {
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: obj.error,
          });
          return;
        }

        this.fornecedor = cnpjtoFornecedor(obj);
      },
      error: (error) => {
        console.log(error);
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: error.message,
          });

      }
    });
  }else{
    const cnpjElement = document.getElementById('cnpj');
    if(cnpjElement){
      if (cnpj.length > 0) {
        cnpjElement.classList.add('ng-invalid');
        cnpjElement.classList.add('ng-dirty');
      }else{
        cnpjElement.classList.remove('ng-invalid');
        cnpjElement.classList.remove('ng-dirty');
      }
    }

  }

}
telefone(): any {
  if(this.fornecedor.telefone.replace(/[_]/g, '').length < 15)
    return true;
    return false;
}
  fornecedor: Fornecedor = {
    cnpj: '',
    telefone: '',
    disabled: {
      nomeFantasia: false,
      razaoSocial: false,
      email: false
    }
  };
  situacaoCadastroSelecionado: any;
  situacaoContrato = Object.keys(SituacaoContrato).filter((key) => isNaN(Number(key)))
  .map((status, index) => ({
    label: status.replace(/_/g, ' '),
    value: index,
  }));
  cadastroSituacao(event: any) {
    this.fornecedor.situacaoContrato = event.value;
  }

  constructor(private http: HttpService,
    private messageService: MessageService) {}

}
