import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../../services/http.service';

@Component({
  selector: 'app-grupo-produto-listagem',
  templateUrl: './grupo-produto-listagem.component.html',
  styleUrl: './grupo-produto-listagem.component.scss'
})
export class GrupoProdutoListagemComponent  implements OnInit {
  constructor(private http:HttpService) { }
  ngOnInit(): void {
    const data = sessionStorage.getItem('filial');
    const localFilial = data ? JSON.parse(data) : undefined;
    this.http.getGrupoProdutoAllFilter(localFilial.id, 'Teste').subscribe({next: (retorno) => {
      console.log(retorno);
    }
  })
  }

}
