import { CustomerOrder } from '../../interfaces/CustomerOrder';
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Chart } from 'chart.js';
import { Purchase } from '../../interfaces/Purchase';
import { Order } from '../../interfaces/Order';
import { throwIfEmpty } from 'rxjs/operators';

@Component({
  selector: 'app-customergraph',
  templateUrl: './customergraph.component.html',
  styleUrls: ['./customergraph.component.css'],
})
export class CustomergraphComponent implements OnInit {
  constructor(private http: HttpClient) {}

  orderId: number[] = [];
  totalProfit: number = 0;
  options1: CustomerOrder[] = [];
  purchaseId: number[] = [];
  purchaseStatus: string[] = [];
  options2: Order[] = [];
  customerOrders: number[] = new Array(300).fill(0);
  totalOrders : number[] = [];
  unique: number[] = [];
  unique1: number[] = [];
  storeProductId: number[] = [];
  productsId: number[] = new Array(200).fill(0);
  options3: Product[] = [];
  storeProductAvailability: number[]  = new Array(3).fill(0);;

  ngOnInit(): void {

    // For orders and profits on that order

    this.http
      .get<CustomerOrder[]>(
        'api/getorders'
      )

      .subscribe((data) => {
        console.log(data);
        this.options1 = [...data];
        console.log(this.options1);
        this.options1.forEach((option: CustomerOrder) => {
          this.orderId.push(option.orderId);

          this.totalProfit+=option.orderProfit;
        });

      });

    // For Customers and their number of orders per customer

    this.http
      .get<Order[]>('api/getorders')

      .subscribe((data) => {
        console.log(data);
        this.options2 = [...data];
        this.options2.forEach((option: Order) => {
          this.purchaseId.push(option.customerId);
          this.customerOrders[option.customerId]++;
        });
        this.unique = this.purchaseId.filter((item, i, ar) => ar.indexOf(item) === i);
        console.log(this.unique);

      for(let i = 0; i<this.unique.length; i++) {
        this.totalOrders.push(this.customerOrders[this.unique[i]]);
      }
      console.log(this.totalOrders);
      console.log(this.purchaseId);
      var myChart = new Chart('ctx2', {
        type: 'pie',
        data: {
          labels: this.unique,
          datasets: [
            {
              label: 'Purchase Graph',
              data: this.totalOrders, //diff states of purchase
              backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 159, 64, 0.2)',
              ],
              borderColor: [
                'rgba(255, 99, 132, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)',
              ],
              borderWidth: 1,
            },
          ],
        },
        options: {
          scales: {
            yAxes: [
              {
                ticks: {
                  beginAtZero: true,
                },
              },
            ],
          },
        },
      });
    });
    this.http
      .get<Product[]>(
        'api/getproducts?offset=0&limit=10000'
      )

      .subscribe((data) => {
        console.log(data);
        this.options3 = [...data];
        console.log(this.options3);
        this.options3.forEach((option: Product) => {
          if(parseInt(option.productAvailable) > 700) {
            this.storeProductAvailability[2]++;
          } else if(parseInt(option.productAvailable) <= 700 && parseInt(option.productAvailable) > 0) {
            this.storeProductAvailability[1]++;
          } else this.storeProductAvailability[0]++;
        });

        console.log(this.storeProductAvailability);

        console.log(this.orderId);
        var myChart = new Chart('ctx5', {
          type: 'pie',
          data: {
            labels: ["Out of Stock", "Low Stock", "High Stock"],
            datasets: [
              {
                label: '# products thresholds',
                data: this.storeProductAvailability,
                backgroundColor: [
                  'rgba(255, 99, 132, 0.2)',
                  'rgba(54, 162, 235, 0.2)',
                  'rgba(255, 206, 86, 0.2)',
                  'rgba(75, 192, 192, 0.2)',
                  'rgba(153, 102, 255, 0.2)',
                  'rgba(255, 159, 64, 0.2)',
                ],
                borderColor: [
                  'rgba(255, 99, 132, 1)',
                  'rgba(54, 162, 235, 1)',
                  'rgba(255, 206, 86, 1)',
                  'rgba(75, 192, 192, 1)',
                  'rgba(153, 102, 255, 1)',
                  'rgba(255, 159, 64, 1)',
                ],
                borderWidth: 1,
              },
            ],
          },
          options: {
            scales: {
              yAxes: [
                {
                  ticks: {
                    beginAtZero: true,
                  },
                },
              ],
            },
          },
        });
    });
  }

}

export interface ProductDetails {
  productId: number,
  productQuantity: number
}

export interface Product {
  productId: number;
  productName: string;
  productOnHand: string;
  productAvailable: string;
  productOutgoing: string;
  productIncoming: string;
  productcostPrice: string;
  productSellingPrice: string;
}
