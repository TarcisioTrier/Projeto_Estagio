import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
} from '@angular/core';
import {
  enumToArray,
  SituacaoCadastro,
  SituacaoContrato,
} from '../../../models/app-enums';
import {
  validateCnpj,
  Cnpj,
  cnpjtoFornecedor,
  validateCnpjField,
} from '../../../models/externalapi';
import { Fornecedor } from '../../../models/fornecedor';
import { HttpService } from '../../../services/http/http.service';
import { MessageHandleService } from '../../../services/message-handle.service';
import { debounce } from 'lodash';

@Component({
  selector: 'app-fornecedor-form',
  templateUrl: './fornecedor-form.component.html',
  styleUrl: './fornecedor-form.component.scss',
})
export class FornecedorFormComponent implements OnChanges {
  @Input() fornecedorPut!: Fornecedor;
  @Output() fornecedorPutChange = new EventEmitter<Fornecedor>();
  loading = false;

  fornecedor: Fornecedor = {
    cnpj: '',
    telefone: '',
    disabled: {
      nomeFantasia: false,
      razaoSocial: false,
      email: false,
    },
  };

  situacaoCadastro = enumToArray(SituacaoCadastro);

  constructor(
    private http: HttpService,
    private messageHandler: MessageHandleService
  ) {}
  ngOnChanges(changes: SimpleChanges): void {
    if (this.fornecedorPut !== undefined) {
      const situacaoCadastro = Object.values(SituacaoCadastro);
      this.fornecedor = {
        cnpj: this.fornecedorPut.cnpj,
        telefone: this.fornecedorPut.telefone,
        nomeFantasia: this.fornecedorPut.nomeFantasia,
        razaoSocial: this.fornecedorPut.razaoSocial,
        email: this.fornecedorPut.email,
        situacaoCadastro: situacaoCadastro.indexOf(
          this.fornecedorPut.situacaoCadastro!
        ),
        disabled: {
          nomeFantasia: false,
          razaoSocial: false,
          email: false,
        },
      };
    }
  }

  load() {
    this.loading = true;
    this.fornecedorPutChange.emit(this.fornecedor);
    this.loading = false;
  }

  cnpj = debounce(() => {
    const cnpj = this.fornecedor.cnpj!.replace(/[_./-]/g, '');

    if (cnpj.length == 0) {
      this.enableFieldsCnpj();
    }

    if (validateCnpj(cnpj)) {
      this.http.buscaCNPJ(cnpj).subscribe({
        next: (obj: Cnpj) => {
          if (obj && obj.error) {
            this.messageHandler.showErrorMessage(obj.error);
          } else {
            this.fornecedor = cnpjtoFornecedor(obj);
          }
        },
        error: (error) => {
          console.log(error);
        },
      });
    } else {
      validateCnpjField(cnpj);
    }
  }, 1000);

  enableFieldsCnpj() {
    this.fornecedor.disabled.nomeFantasia = false;
    this.fornecedor.disabled.razaoSocial = false;
    this.fornecedor.disabled.email = false;
  }

  isFoneValid(): boolean {
    return this.fornecedor.telefone!.replace(/[_]/g, '').length < 15;
  }
}
