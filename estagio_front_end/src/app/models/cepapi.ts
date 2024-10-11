export interface Cep{
  cep: string,
  logradouro: string,
  complemento: string,
  unidade: string,
  bairro: string,
  localidade: string,
  uf: string,
  estado: string,
  regiao: string,
  ibge: string,
  gia: string,
  ddd: string,
  siafi: string,
  erro?: string
}
export interface Cnpj{
  "NOME FANTASIA": string,
  "RAZAO SOCIAL": string,
  CNPJ: number,
  "STATUS": string,
  "CNAE PRINCIPAL DESCRICAO": string,
  "CNAE PRINCIPAL CODIGO": string,
  CEP: string,
  "DATA ABERTURA": string,
  "DDD": string,
  "TELEFONE": string,
  "EMAIL": string,
  "TIPO LOGRADOURO": string,
  LOGRADOURO: string,
  NUMERO: number,
  COMPLEMENTO: string,
  BAIRRO: string,
  MUNICIPIO: string,
  UF: string
  error?: string
}
