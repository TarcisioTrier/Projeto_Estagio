import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../../services/http/http.service';

@Component({
  selector: 'app-dashboard-graph',
  templateUrl: './dashboard-graph.component.html',
  styleUrl: './dashboard-graph.component.scss',
})
export class DashboardGraphComponent implements OnInit {
  colors = [
    {
      backgroundColor: 'rgba(19, 111, 99, 0.9)',
      borderColor: 'rgb(19, 111, 99)',
    },
    {
      backgroundColor: 'rgba(241, 211, 2, 0.9)',
      borderColor: 'rgb(241, 211, 2)',
    },
    {
      backgroundColor: 'rgba(193, 41, 46, 0.9)',
      borderColor: 'rgb(193, 41, 46)',
    },
    {
      backgroundColor: 'rgba(253, 255, 252, 0.9)',
      borderColor: 'rgb(253, 255, 252)',
    },
    {
      backgroundColor: 'rgba(224, 202, 60, 0.9)',
      borderColor: 'rgb(224, 202, 60)',
    },
    {
      backgroundColor: 'rgba(0, 15, 8, 0.9)',
      borderColor: 'rgb(0, 15, 8)',
    },
    {
      backgroundColor: 'rgba(35, 87, 137, 0.9)',
      borderColor: 'rgb(35, 87, 137)',
    },
    {
      backgroundColor: 'rgba(243, 66, 19, 0.9)',
      borderColor: 'rgb(243, 66, 19)',
    },
    {
      backgroundColor: 'rgba(62, 47, 91, 0.9)',
      borderColor: 'rgb(62, 47, 91)',
    },
    {
      backgroundColor: 'rgba(186, 39, 74, 0.9)',
      borderColor: 'rgb(186, 39, 74)',
    },
  ];
  filialId?: number;
  produtosFilais: any;
  gruposProdutoFiliais: any;
  fornecedoresFiliais: any;
  produtosGrupos: any ;
  options: any;
  moneyOptions: any;
  maiorValorVendaOfFilial: any;
  maiorValorProdutoOfFilial: any;
  maiorValorVendaFilial: any;
  maiorValorProdutoFilial: any;
  constructor(private http: HttpService) {}
  ngOnInit(): void {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue(
      '--text-color-secondary'
    );
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');
    this.http.getFilialChart().subscribe({
      next: (data) => {
        data.sort((a: any, b: any) => b.produtos - a.produtos);
        const top10Produtos = data.slice(0, 10);
        this.produtosFilais = {
          labels: top10Produtos.map((element: any) => element.nomeFantasia),
          datasets: [
            {
              data: top10Produtos.map((element: any) => element.produtos),
              backgroundColor: top10Produtos.map((element: any, index: number) =>
                this.colors[index % this.colors.length].borderColor
              ),
              hoverBackgroundColor: top10Produtos.map((element: any, index: number) =>
                this.colors[index % this.colors.length].backgroundColor
              ),
              borderWidth: 1,
              label: 'Produtos por Filial',
            },
          ],
        };
        data.sort((a: any, b: any) => b.gruposProduto - a.gruposProduto);
        const top10Grupos = data.slice(0, 10);
        this.gruposProdutoFiliais = {
          labels: top10Grupos.map((element: any) => element.nomeFantasia),
          datasets: [
            {
              data: top10Grupos.map((element: any) => element.gruposProduto),
              backgroundColor: top10Produtos.map((element: any, index: number) =>
                this.colors[index % this.colors.length].borderColor
              ),
              hoverBackgroundColor: top10Produtos.map((element: any, index: number) =>
                this.colors[index % this.colors.length].backgroundColor
              ),
              borderWidth: 1,
              label: 'Grupos de Produto por Filial',
            },
          ],
        };
        data.sort((a: any, b: any) => b.fornecedores - a.fornecedores);
        const top10Fornecedores = data.slice(0, 10);
        this.fornecedoresFiliais = {
          labels: top10Fornecedores.map((element: any) => element.nomeFantasia),
          datasets: [
            {
              data: top10Fornecedores.map((element: any) => element.fornecedores),
              backgroundColor: top10Fornecedores.map((element: any, index: number) =>
                this.colors[index % this.colors.length].borderColor
              ),
              hoverBackgroundColor: top10Fornecedores.map((element: any, index: number) =>
                this.colors[index % this.colors.length].backgroundColor
              ),
              borderWidth: 1,
              label: 'Fornecedores por Filial',
            },
          ],
        };

        let top10MaisCaro: any = [];
        data.forEach((element: any) => {
          if(element.maiorValorVenda){
            top10MaisCaro.push(element.maiorValorVenda)
          }
        });
        top10MaisCaro.sort((a:any, b:any)=> b.valorVenda - a.valorVenda);
        top10MaisCaro = top10MaisCaro.slice(0,10)
        this.maiorValorVendaFilial = {
          labels: top10MaisCaro.map((element:any)=> element.nome),
          datasets: [
            {
              data: top10MaisCaro.map((element: any) => element.valorVenda),
              backgroundColor: top10MaisCaro.map((element: any, index: number) =>
                this.colors[index % this.colors.length].borderColor
              ),
              hoverBackgroundColor: top10MaisCaro.map((element: any, index: number) =>
                this.colors[index % this.colors.length].backgroundColor
              ),
              borderWidth: 1,
              label: 'Maior Valor de Venda por Filial',
            },
          ],
        }
        top10MaisCaro = [];
        data.forEach((element: any) => {
          if(element.maiorValorProduto){
            top10MaisCaro.push(element.maiorValorProduto)
          }
        });
        top10MaisCaro.sort((a:any, b:any)=> b.valorProduto - a.valorProduto);
        top10MaisCaro = top10MaisCaro.slice(0,10)
        this.maiorValorProdutoFilial = {
          labels: top10MaisCaro.map((element:any)=> element.nome),
          datasets: [
            {
              data: top10MaisCaro.map((element: any) => element.valorProduto),
              backgroundColor: top10MaisCaro.map((element: any, index: number) =>
                this.colors[index % this.colors.length].borderColor
              ),
              hoverBackgroundColor: top10MaisCaro.map((element: any, index: number) =>
                this.colors[index % this.colors.length].backgroundColor
              ),
              borderWidth: 1,
              label: 'Maior Valor de Produto por Filial',
            },
          ],
        }
      },


    });
    this.filialId = this.http.filialId();
    if (this.filialId !== undefined) {
    this.http.getProdutosOfGrupos(this.filialId).subscribe({
      next: (data) => {
        data.sort((a: any, b: any) => b.produtos - a.produtos);
        const top10Produtos = data.slice(0, 10);
        this.produtosGrupos = {
          labels: top10Produtos.map((element: any) => element.nomeGrupo),
          datasets: [
            {
              data: top10Produtos.map((element: any) => element.produtos),
              backgroundColor: top10Produtos.map((element: any, index: number) =>
                this.colors[index % this.colors.length].borderColor
              ),
              hoverBackgroundColor: top10Produtos.map((element: any, index: number) =>
                this.colors[index % this.colors.length].backgroundColor
              ),
              borderWidth: 1,
              label: 'Produtos por Grupo',
            },
          ],
        }
      },
    });
    this.http.maiorValorVendaProdutos(this.filialId).subscribe({
      next: (data)=> {
        const top10Produtos = data.content;
        this.maiorValorVendaOfFilial = {
          labels: top10Produtos.map((element: any) => element.nome),
          datasets: [
            {
              data: top10Produtos.map((element: any) => element.valorVenda),
              backgroundColor: top10Produtos.map((element: any, index: number) =>
                this.colors[index % this.colors.length].borderColor
              ),
              hoverBackgroundColor: top10Produtos.map((element: any, index: number) =>
                this.colors[index % this.colors.length].backgroundColor
              ),
              borderWidth: 1,
              label: 'Produtos com Maior Valor de Venda da Filial',
            },
          ],
        }
      }
    })
    this.http.maiorValorProdutoProdutos(this.filialId).subscribe({
      next: (data)=> {
        const top10Produtos = data.content;
        this.maiorValorProdutoOfFilial = {
          labels: top10Produtos.map((element: any) => element.nome),
          datasets: [
            {
              data: top10Produtos.map((element: any) => element.valorProduto),
              backgroundColor: top10Produtos.map((element: any, index: number) =>
                this.colors[index % this.colors.length].borderColor
              ),
              hoverBackgroundColor: top10Produtos.map((element: any, index: number) =>
                this.colors[index % this.colors.length].backgroundColor
              ),
              borderWidth: 1,
              label: 'Produtos com Maior Valor de Produto da Filial',
            },
          ],
        }
      }
    })
  }
    this.options = {
      plugins: {
        legend: {
          labels: {
            color: textColor,
          },
        },
      },
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
        },
        x: {
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
        },
      },
    };
    this.moneyOptions = {
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            color: textColorSecondary,
            callback: function(value: string, index: any, ticks: any) {
              return 'R$' + value;
          }
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
        },
        x: {
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
        },
      },
    };

  }
}

