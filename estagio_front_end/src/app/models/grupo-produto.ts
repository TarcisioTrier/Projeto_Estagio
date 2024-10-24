import { GrupoProdutoOrderEnum, SituacaoCadastro, TipoGrupoProduto } from "./app-enums";
import { Produto } from "./produto";

export interface GrupoProduto{
  id?: number,
  nomeGrupo?: string,
  filialId?: number,
  tipoGrupo?: TipoGrupoProduto,
  margemLucro?: number,
  atualizaPreco?: boolean,
  situacaoCadastro?: SituacaoCadastro,
  produtos?: Array<Produto>,
  order?: GrupoProdutoOrderEnum
  }
