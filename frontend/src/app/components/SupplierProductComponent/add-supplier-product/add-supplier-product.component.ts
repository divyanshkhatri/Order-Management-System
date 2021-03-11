import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith,} from 'rxjs/operators';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';

import { ProductServiceService } from 'src/app/Services/ProductService/product-service.service';
import { Product } from 'src/app/interfaces/Product';

@Component({
  selector: 'app-add-supplier-product',
  templateUrl: './add-supplier-product.component.html',
  styleUrls: ['./add-supplier-product.component.css']
})

export class AddSupplierProductComponent implements OnInit {
  constructor(
    private productService: ProductServiceService,
    private dialog: MatDialog,
    private http: HttpClient,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {}

  productId: number = 0

  options: Product[] = [];
  products: String[] = [];

  getProductName(productId:number) {
    return this.options.find(option => option.productId === productId)?.productName || '';
  }

  ngOnInit() {

    this.getData().subscribe((data) => {
      console.log(data);
      this.options = [...data];
      console.log(this.options);
      this.options.forEach((option: Product) => {
        this.products.push(option.productName);
      });
    });


    console.log(this.products);
  }
  getData() {

      const requestUrl = `api/getproducts?limit=100&offset=0`;

      return this.http.get<Product[]>(requestUrl);

  }

  productForm = new FormGroup({
    productId: new FormControl('', [Validators.required]),
    productQuantity: new FormControl('', [Validators.required]),
  });

  product = {

  }

  addProduct() {

    this.product = {
      "productId": this.productForm.get('productId')?.value,
      "productQuantity": this.productForm.get('productQuantity')?.value,
      "purchaseId": this.data.id,
    }

    const requestUrl = `api/createpurchaseproduct`;
    return this.http.post<any>(requestUrl, this.product).subscribe((data) => {
      console.log(data);
    });
  }
}
