import { Component } from '@angular/core';
import { Produto } from '../../../models/produto';

@Component({
  selector: 'app-produto-cadastro',
  templateUrl: './produto-cadastro.component.html',
  styleUrl: './produto-cadastro.component.scss'
})
export class ProdutoCadastroComponent {
  produto: Produto ={
    codigoBarras: '',
    nome: '',
    descricao: '',
    aceitaAtualizacaoPreco: false,
  }
}
