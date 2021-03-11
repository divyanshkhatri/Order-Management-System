import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Purchase } from '../../interfaces/Purchase';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-purchasegraph',
  templateUrl: './purchasegraph.component.html',
  styleUrls: ['./purchasegraph.component.css'],
})
export class PurchasegraphComponent implements OnInit {
  constructor(private http: HttpClient) {}

  purchaseId: number[] = [];
  purchaseStatus: string[] = [];
  options: Purchase[] = [];
  draft = 0;
  raised = 0;
  received = 0;
  closed = 0;
  cancelled = 0;

  ngOnInit(): void {
    this.http
      .get<Purchase[]>('api/getallpurchases')

      .subscribe((data) => {
        console.log(data);
        this.options = [...data];
        console.log(this.options);
        this.options.forEach((option: Purchase) => {
          this.purchaseId.push(option.purchaseId);
          this.purchaseStatus.push(option.purchaseStatus);
          if(option.purchaseStatus == "Draft") this.draft += 1;
          else if(option.purchaseStatus == "Raised") this.raised += 1;
          else if(option.purchaseStatus == "Received") this.received += 1;
          else if(option.purchaseStatus == "Closed") this.closed += 1;
          else if(option.purchaseStatus == "Cancelled") this.cancelled += 1;
        });
        console.log(this.draft);
        console.log(this.raised);
        console.log(this.received);
        console.log(this.closed);
        console.log(this.cancelled);
        console.log(this.purchaseId);
        var myChart = new Chart('ctx3', {
          type: 'pie',
          data: {
            labels: ["Draft", "Raised", "Received", "Closed", "Cancelled"],
            datasets: [
              {
                label: 'Purchase Graph',
                data: [this.draft, this.raised, this.received, this.closed, this.cancelled], //different states of purchase
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
