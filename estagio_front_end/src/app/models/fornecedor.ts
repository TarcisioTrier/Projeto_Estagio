import { SituacaoContrato } from "./app-enums";

export interface Fornecedor{
  id?: number,
  nomeFantasia: string,
  razaoSocial: string,
  cnpj: string,
  telefone: string,
  email: string,
  situacaoContrato: SituacaoContrato,
  filialId: number,
  }
