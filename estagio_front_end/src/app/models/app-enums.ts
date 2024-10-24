export enum SituacaoContrato {
  ATIVO ,
  INATIVO ,
  RESCINDIDO ,
  EM_IMPLEMENTACAO,
}
export enum Apresentacao {
  CAPS_COMP_DRAG,
  GOTAS,
  INJETAVEL,
  LIQUIDO,
  OUTROS,
  POMADA_CREME,
}
export enum SituacaoCadastro {
  ATIVO,
  INATIVO,
}
export enum TipoGrupoProduto {
  MEDICAMENTO,
  PERFUMARIA,
  BEBIDA,
  OUTROS,
}
export enum TipoProduto {
  ANTICONCEPCIONAIS,
  FITOTERAPICO,
  FRALDAS,
  GENERICO,
  HOMEOPATICO,
  LEITES,
  MANIPULADO,
  MEDICAMENTO,
  NAO_DEFINIDO,
  OFICINAL,
  PERFUMARIA,
  REFERENCIA,
  SIMILAR,
}

export enum AtualizaPrecoEnum {
  MARGEM_RELATIVA,
  MARGEM_ABSOLUTA,
  VALOR_PRODUTO_RELATIVO,
  VALOR_PRODUTO_ABSOLUTO,
  VALOR_VENDA_RELATIVO,
  VALOR_VENDA_ABSOLUTO,
}

export interface Order {
  field:string,
  order:number
}

export interface Filter{
  field:string,
  matchMode:string
}
