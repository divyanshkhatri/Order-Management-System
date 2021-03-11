import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { AddSupplierProductComponent } from '../../SupplierProductComponent/add-supplier-product/add-supplier-product.component';

@Component({
  selector: 'app-purchase-product',
  templateUrl: './purchase-product.component.html',
  styleUrls: ['./purchase-product.component.css']
})

export class PurchaseProductComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialog: MatDialog, private http: HttpClient) { }

  displayedColumns: string[] = ['purchaseId', 'productId', 'productQuantity'];

  @ViewChild(MatPaginator, { static: true })
  paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true })
  sort!: MatSort;

  dataSource = new MatTableDataSource<PurchaseProduct>();

  ngOnInit(): void {

    this.http.get<PurchaseProduct[]>(`api/getpurchaseproducts/${this.data.id}`).subscribe((data) => {
        this.dataSource.data = data;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
    })

  }

  openDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;
    dialogConfig.height = '380px';
    dialogConfig.width = '350px';
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      id: this.data.id
    }

    this.dialog.open(AddSupplierProductComponent, dialogConfig).afterClosed().subscribe(() => {
      console.log("fdklfkd");
      const requestUrl =
        `/api/getSuppliers?limit=5&offset=0`;

      this.http.get<PurchaseProduct[]>(`api/getpurchaseproducts/${this.data.id}`).subscribe((data) => {
        this.dataSource.data = data;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      })
    });

  }

}

export interface PurchaseProduct{

  productId: String,
  purchaseId: String,
  productQuantity: number,

}
