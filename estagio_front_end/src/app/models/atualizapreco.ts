import { AtualizaPrecoEnum } from "./app-enums";
import { GrupoProduto } from "./grupo-produto";
import { Produto } from "./produto";

export interface AtualizaPreco{
  produtoId: number[],
  grupoProdutoId: number[],
  filialId?: number,
  all: boolean,
  isProduto: boolean,
  isRelativo: boolean,
  valor?: number,
  isPercentual: boolean,
  atualizaPreco?: AtualizaPrecoEnum
  produtoFilter?: Produto,
  grupoProdutoFilter?: GrupoProduto
}
