import { Filter, Order, SituacaoCadastro, TipoGrupo } from './app-enums';
import { Produto } from './produto';

export interface GrupoProduto {
  id?: number;
  nomeGrupo?: string;
  filialId?: number;
  tipoGrupo?: TipoGrupo;
  margemLucro?: number;
  atualizaPreco?: boolean;
  situacaoCadastro?: SituacaoCadastro;
  edit?: boolean;
  produtos?: Array<Produto>;
  orderer?: Order[];
  filter?: any;
}
