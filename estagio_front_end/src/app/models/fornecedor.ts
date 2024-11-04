import { Filter, Order, SituacaoCadastro, SituacaoContrato } from './app-enums';

export interface Fornecedor {
  id?: number;
  nomeFantasia?: string;
  razaoSocial?: string;
  cnpj?: string;
  telefone?: string;
  email?: string;
  situacaoCadastro?: SituacaoCadastro;
  filialId?: number;
  orderer?: Order[];
  edit?: boolean;
  filter?: any;
  disabled: {
    nomeFantasia: boolean;
    razaoSocial: boolean;
    email: boolean;
  };
}
