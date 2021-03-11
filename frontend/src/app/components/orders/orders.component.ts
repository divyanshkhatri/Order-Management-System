import { CustomerOrder } from '../../interfaces/CustomerOrder';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';

import { HttpClient } from '@angular/common/http';
import { take } from 'rxjs/operators';
import { MatSort } from '@angular/material/sort';
import { ProductsComponent } from '../ProductsComponent/products/products.component';
@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css'],
})
export class OrdersComponent implements OnInit {
  displayedColumns: string[] = ['customerId', 'orderId', 'orderStatus', 'details'];
  dataSource = new MatTableDataSource<CustomerOrder>();
  constructor(private dialog: MatDialog, private http: HttpClient) {}

  getData(offset: number, limit: number) {
    const requestUrl = `api/getorders`;

    return this.http.get<CustomerOrder[]>(requestUrl);
  }

  data: CustomerOrder[] = [];

  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;

  @ViewChild(MatPaginator, { static: true })
  paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true })
  sort!: MatSort;

  fetchData() {
    console.log('next page', this.paginator.pageIndex, this.paginator.pageSize);

    this.getData(
      this.paginator.pageSize * this.paginator.pageIndex,
      this.paginator.pageSize
    ).subscribe((data) => {
      this.dataSource.data = data;
    });
  }

  updateStatus = {}

  updated = "";

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

  ngOnInit(): void {
    this.paginator.pageSize = 5;
    this.paginator.length = 100;
    this.getData(0, 5)
    .pipe(take(1))
    .subscribe((data) => {
      console.log(data);
      this.dataSource.data = data;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
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

}
