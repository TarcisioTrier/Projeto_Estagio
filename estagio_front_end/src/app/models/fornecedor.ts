import { Filter, Order, SituacaoContrato } from "./app-enums";

export interface Fornecedor{
  id?: number,
  nomeFantasia?: string,
  razaoSocial?: string,
  cnpj: string,
  telefone: string,
  email?: string,
  situacaoContrato?: SituacaoContrato,
  filialId?: number,
  orderer?: Order[],
  filter?: Filter[],
  disabled:{
    nomeFantasia: boolean,
    razaoSocial: boolean,
    email: boolean
  }
  }
