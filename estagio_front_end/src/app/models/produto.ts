import { Apresentacao, SituacaoCadastro, TipoProduto } from './app-enums';
import { GrupoProduto } from './grupo-produto';

export interface Produto {
  id?: number;
  codigoBarras: string;
  nome: string;
  descricao: string;
  grupoProdutoId?: number;
  grupoProduto?: GrupoProduto;
  tipoProduto?: TipoProduto;
  apresentacao?: Apresentacao;
  margemLucro?: number;
  atualizaPreco: boolean;
  valorProduto?: number;
  valorVenda?: number;
  dataUltimaAtualizacao?: Date;
  situacaoCadastro: SituacaoCadastro;
  disabled: {
    nome: boolean;
    descricao: boolean;
  };
}
