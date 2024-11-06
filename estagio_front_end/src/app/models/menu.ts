export interface Menu {
  id: string;
  nome: string;
  paginas?: Array<Paginas>;
}
export interface Paginas {
  id: string;
  url: string;
  info: string;
}
