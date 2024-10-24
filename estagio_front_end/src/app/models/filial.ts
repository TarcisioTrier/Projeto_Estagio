import { SituacaoContrato } from './app-enums';
import { Fornecedor } from './fornecedor';
import { GrupoProduto } from './grupo-produto';

export interface Filial {
  id?: number;
  nomeFantasia: string;
  razaoSocial: string;
  cnpj: string;
  telefone: string;
  email: string;
  situacaoContrato: SituacaoContrato;
  endereco?: Endereco;
  fornecedores?: Array<Fornecedor>;
  grupoProdutos?: Array<GrupoProduto>;
  disabled: {
    nomeFantasia: boolean;
    razaoSocial: boolean;
    email: boolean;
  };
}
export interface Endereco {
  id?: number;
  cep: string;
  logradouro: string;
  numero?: number;
  complemento?: string;
  bairro: string;
  localidade: string;
  estado: string;
  disabled: {
    logradouro: boolean;
    numero: boolean;
    complemento: boolean;
    bairro: boolean;
    localidade: boolean;
    estado: boolean;
  };
}
