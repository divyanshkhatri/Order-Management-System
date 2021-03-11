import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import {MatDialog, MatDialogConfig, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ProductsComponent } from '../../ProductsComponent/products/products.component';

@Component({
  selector: 'app-customer-order',
  templateUrl: './customer-order.component.html',
  styleUrls: ['./customer-order.component.css']
})
export class CustomerOrderComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialog: MatDialog, private http: HttpClient) { }

  displayedColumns: string[] = ['customerId', 'orderId', 'orderStatus', 'products'];

  status: Status[] = [
    {value: 'NotConfirmed', viewValue: 'Not Confirmed'},
    {value: 'Confirmed', viewValue: 'Confirmed'},
    {value: 'shipped', viewValue: 'Shipped' },
    {value: 'Completed', viewValue: 'Completed' },
    {value: 'Cancelled', viewValue: 'Cancelled'},
  ]

  statusNotConfirmed : Status[] = [
    {value: 'Confirmed', viewValue: 'Confirmed'},
  ];

  statusConfirmed : Status[] = [
    {value: 'Cancelled', viewValue: 'Cancelled'},
    {value: 'shipped', viewValue: 'Shipped' },
  ];

  statusShipped : Status[] = [
    {value: 'Completed', viewValue: 'Completed' },

  ];

  statusDelivered: Status[] = [

  ];

  statusCancelled : Status[] = [
    // {value: 'Delivered', viewValue: 'Delivered'},
  ];

  @ViewChild(MatPaginator, { static: true })
  paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true })
  sort!: MatSort;

  dataSource = new MatTableDataSource<Order>();

  customer = {
    "customerId": this.data.id
  }

  ngOnInit(): void {
    // console.log(this.data.id);
    this.http.get<Order[]>(`/api/getorders/${this.data.id}`).subscribe((data) => {
        this.dataSource.data = data;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        console.log(data);
    })
    // setTimeout(() => this.dataSource.paginator = this.paginator, 0);

  }

  orderStatus: String = "Status";

  openDialog(){

    console.log(this.customer);

    let createOrderUrl = `api/createorder/`
    this.http.post<Order[]>(createOrderUrl, this.customer).subscribe((data) => {
      console.log(data);
      this.http.get<Order[]>(`/api/getorders/${this.data.id}`).subscribe((data) => {
          this.dataSource.data = data;
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
          console.log(data);
      })
    })
  }

  onClick(element: any) {
    console.log(element.orderId);
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;
    dialogConfig.height = '85vh';
    dialogConfig.width = '70vw';
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      id: element.orderId
    }


    this.dialog.open(ProductsComponent, dialogConfig);



  }

  updated = "";

  accepted = "";
  rejected = "";

  updateStatus = {}


  update(element: any) {

    this.updateStatus = {
      "orderId": element.orderId,
      "orderStatus": element.orderStatus
    }
    console.log(element);

    const requestUrl = `api/updateorderstatus`;
    this.http.put<string>(requestUrl, this.updateStatus).subscribe((data) => {
      this.updated = data;

    })
  }

}

export interface Status {
  value: string;
  viewValue: string;
}

export interface Order {
  orderId: number,
  customerId: number,
  orderStatus: String,
}

export interface OrderStatus {
  orderId: String,
  orderStatus: String,
}
