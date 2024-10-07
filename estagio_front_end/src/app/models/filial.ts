import { SituacaoContrato } from "./app-enums"
import { Fornecedor } from "./fornecedor";
import { GrupoProduto } from "./grupo-produto";

export interface Filial{
  id?: number,
  nomeFantasia: string,
  razaoSocial: string,
  cnpj: string,
  telefone: string,
  email: string,
  situacaoContrato: SituacaoContrato,
  endereco?: Endereco,
  fornecedores?: Array<Fornecedor>,
  grupoProdutos?: Array<GrupoProduto>
  }
  export interface Endereco{
    id?: number,
    cep: string,
    logradouro: string;
    numero: number;
    complemento?: string,
    bairro: string,
    cidade: string,
    estado: string,
    rua: string

    }
