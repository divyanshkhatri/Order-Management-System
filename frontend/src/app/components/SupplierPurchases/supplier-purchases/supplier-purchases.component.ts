import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import {MatDialog, MatDialogConfig, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { PurchaseProductComponent } from '../../PurchaseProductComponent/purchase-product/purchase-product.component';

@Component({
  selector: 'app-supplier-purchases',
  templateUrl: './supplier-purchases.component.html',
  styleUrls: ['./supplier-purchases.component.css']
})

export class SupplierPurchasesComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialog: MatDialog, private http: HttpClient) { }

  displayedColumns: string[] = ['customerId', 'purchaseId', 'orderStatus', 'products'];

  status: Status[] = [
    {value: 'Draft', viewValue: 'Draft'},
    {value: 'Raised', viewValue: 'Raised'},
    {value: 'Received', viewValue: 'Received' },
    {value: 'Closed', viewValue: 'Closed' },
    {value: 'Cancelled', viewValue: 'Cancelled'},
  ]

  @ViewChild(MatPaginator, { static: true })
  paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true })
  sort!: MatSort;

  dataSource = new MatTableDataSource<Order>();

  tableLength = -1;


  supplier = {
    "supplierId": this.data.id
  }

  ngOnInit(): void {
    // console.log(this.data.id);
    this.http.get<Order[]>(`/api/getpurchases/${this.data.id}`).subscribe((data) => {
      this.tableLength = 1;
      this.dataSource.data = data;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      console.log(data);

    })

  }

  openDialog(){

    console.log(this.supplier);

    let createOrderUrl = `api/createpurchase/`
    this.http.post<Order[]>(createOrderUrl, this.supplier).subscribe((data) => {
      console.log(data);
      this.http.get<Order[]>(`/api/getpurchases/${this.data.id}`).subscribe((data) => {
        this.tableLength = 1;
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
    dialogConfig.height = '70vh';
    dialogConfig.width = '60vw';
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      id: element.purchaseId
    }
    this.dialog.open(PurchaseProductComponent, dialogConfig);
  }

  updated = -1;

  accepted = "";
  rejected = "";

  updatePurchase = {}

  update(element: any) {

    this.updatePurchase = {
      "purchaseId": element.purchaseId,
      "purchaseStatus": element.purchaseStatus
    }

    this.http.post<any>(`/api/updateproductstatus`, this.updatePurchase).subscribe((data) => {
      this.updated = data;
      if(this.updated >= 1) {
        this.accepted = "Order Status Updated!";
      } else if(this.updated == 0) {
        this.rejected = "Order Stauts not Updated!";
      } else {
        this.accepted = "";
        this.rejected = "";
      }
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

