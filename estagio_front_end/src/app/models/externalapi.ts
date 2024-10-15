import { retry } from 'rxjs';
import { SituacaoContrato } from './app-enums';
import { Filial, Endereco } from './filial';
import { Fornecedor } from './fornecedor';

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
export function cnpjToFilial(cnpj:Cnpj){
  let filial: Filial = {
    nomeFantasia: toTitleCase(cnpj['NOME FANTASIA']),
    razaoSocial: toTitleCase(cnpj['RAZAO SOCIAL']),
    cnpj: cnpj.CNPJ.toString(),
    telefone: cnpj.DDD + cnpj.TELEFONE,
    email: cnpj.EMAIL,
    situacaoContrato: SituacaoContrato.ATIVO,
    disabled: {
      nomeFantasia: cnpj['NOME FANTASIA'] !== '',
      razaoSocial: cnpj['RAZAO SOCIAL'] !== '',
      email: cnpj.EMAIL !== ''
    }
  }
  return filial;
}
export function cnpjtoFornecedor(cnpj:Cnpj, id: number){
  let fornecedor:Fornecedor ={
    nomeFantasia: toTitleCase(cnpj['NOME FANTASIA']),
    razaoSocial: toTitleCase(cnpj['RAZAO SOCIAL']),
    cnpj: cnpj.CNPJ.toString(),
    telefone: cnpj.DDD + cnpj.TELEFONE,
    email: cnpj.EMAIL,
    situacaoContrato: SituacaoContrato.ATIVO,
    filialId: id
  }
}
export function cnpjToEndereco(cnpj:Cnpj){
  let endereco:Endereco = {
    cep: cnpj.CEP,
    bairro: toTitleCase(cnpj.BAIRRO),
    localidade: cnpj.MUNICIPIO,
    logradouro: toTitleCase(cnpj.LOGRADOURO),
    complemento: toTitleCase(cnpj.COMPLEMENTO),
    numero: cnpj.NUMERO,
    estado: cnpj.UF,
    disabled: {
      logradouro: cnpj.LOGRADOURO !== '',
      numero: cnpj.NUMERO !== 0,
      complemento: cnpj.COMPLEMENTO !== '',
      bairro: cnpj.BAIRRO !== '',
      localidade: cnpj.MUNICIPIO !== '',
      estado: false
    }
  }
  return endereco;
}

export function cpnjEnderecoDisabler(cnpj:Cnpj, endereco:Endereco){
  endereco.disabled = {
    logradouro: cnpj.LOGRADOURO !== '',
    numero: cnpj.NUMERO !== 0,
    complemento: cnpj.COMPLEMENTO !== '',
    bairro: cnpj.BAIRRO !== '',
    localidade: cnpj.MUNICIPIO !== '',
    estado: false
  }
}

export function cepToEndereco(cep:Cep, endereco:Endereco){
  if (cep.bairro !== ''){
    endereco.bairro = cep.bairro;
  }
  endereco.localidade = cep.localidade;
  if(cep.logradouro !== ''){
    endereco.logradouro = cep.logradouro;
  }
  if(cep.complemento !== ''){
    endereco.complemento = cep.complemento;
  }
  endereco.estado = cep.estado;
  endereco.disabled = {
    logradouro: !(cep.logradouro == '' || cep.logradouro == undefined),
    numero: false,
    complemento: !(cep.complemento == '' || cep.complemento == undefined),
    bairro: !(cep.bairro == '' || cep.bairro == undefined),
    localidade: !(cep.localidade == '' || cep.localidade == undefined),
    estado: !(cep.estado == '' || cep.estado == undefined)
  }
}

function toTitleCase(str: string): string {
  return str
    .toLowerCase() // Converte tudo para minúsculas primeiro
    .split(' ') // Divide a string em um array de palavras
    .map(word => word.charAt(0).toUpperCase() + word.slice(1)) // Converte a primeira letra de cada palavra para maiúscula
    .join(' '); // Junta as palavras novamente em uma string
}

export function validateCnpj(cnpj: string): boolean {
  cnpj = cnpj.replace(/[^\d]+/g, '');

  if (cnpj.length !== 14) return false;

  if (/^(\d)\1+$/.test(cnpj)) return false;

  let tamanho = cnpj.length - 2;
  let numeros = cnpj.substring(0, tamanho);
  let digitos = cnpj.substring(tamanho);
  let soma = 0;
  let pos = tamanho - 7;
  for (let i = tamanho; i >= 1; i--) {
    soma += parseInt(numeros.charAt(tamanho - i)) * pos--;
    if (pos < 2) pos = 9;
  }
  let resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11);
  if (resultado != parseInt(digitos.charAt(0))) return false;

  tamanho = tamanho + 1;
  numeros = cnpj.substring(0, tamanho);
  soma = 0;
  pos = tamanho - 7;
  for (let i = tamanho; i >= 1; i--) {
    soma += parseInt(numeros.charAt(tamanho - i)) * pos--;
    if (pos < 2) pos = 9;
  }
  resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11);
  if (resultado != parseInt(digitos.charAt(1))) return false;

  return true;
}
