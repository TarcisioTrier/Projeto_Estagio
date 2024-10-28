export enum SituacaoContrato {
  ATIVO,
  INATIVO,
  RESCINDIDO,
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
export enum TipoGrupo {
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
  MARGEM,
  VALOR_PRODUTO,
  VALOR_VENDA,
}

export interface Order {
  field: string;
  order: number;
}

export interface Filter {
  field: string;
  matchMode: string;
}
export function enumToArray(enumme: any): any[] {
  return Object.keys(enumme)
    .filter((key) => isNaN(Number(key)))
    .map((status, index) => ({
      label: status.replace(/_/g, ' '),
      value: index,
    }));
}
