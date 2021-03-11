import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Chart } from 'chart.js';
import { Order } from '../../interfaces/Order';

@Component({
  selector: 'app-ordersgraph',
  templateUrl: './ordersgraph.component.html',
  styleUrls: ['./ordersgraph.component.css'],
})
export class OrdersgraphComponent implements OnInit {
  constructor(private http: HttpClient) {}

  customerId: number[] = [];
  orderId: number[] = [];
  options: Order[] = [];
  notConfirmed = 0;
  confirmed = 0;
  shipped = 0;
  completed = 0;
  cancelled = 0;

  ngOnInit(): void {

    // Pie chart to show the number of orders per status

    this.http
      .get<Order[]>('api/getorders')

      .subscribe((data) => {
        console.log(data);
        this.options = [...data];
        console.log(this.options);
        this.options.forEach((option: Order) => {
          if(option.orderStatus == "NotConfirmed") this.notConfirmed += 1;
          else if(option.orderStatus == "Confirmed") this.confirmed += 1;
          else if(option.orderStatus == "Shipped") this.shipped += 1;
          else if(option.orderStatus == "Completed") this.completed += 1;
          else if(option.orderStatus == "Cancelled") this.cancelled += 1;
        });
        console.log(this.notConfirmed);
        console.log(this.confirmed);
        console.log(this.shipped);
        console.log(this.completed);
        console.log(this.cancelled);
        var myChart = new Chart('ctx4', {
          type: 'pie',
          data: {
            datasets: [
              {
                label: 'Purchase Graph',
                data: [this.notConfirmed, this.confirmed, this.shipped, this.completed, this.cancelled], //diff states of purchase
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
            labels: ['Not Confirmed', 'Confirmed', 'Shipped', 'Completed', 'Cancelled'],
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
    console.log(this.customerId);

  }
}
