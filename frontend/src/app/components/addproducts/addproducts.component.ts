import { ProductServiceService } from '../../Services/ProductService/product-service.service';
import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { catchError, map, startWith,} from 'rxjs/operators';
import { Product } from '../../interfaces/Product';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';
@Component({
  selector: 'app-addproducts',
  templateUrl: './addproducts.component.html',
  styleUrls: ['./addproducts.component.css'],
})
export class AddproductsComponent implements OnInit {
  constructor(
    private productService: ProductServiceService,
    private dialog: MatDialog,
    private http: HttpClient,
  ) {}

  options: Product[] = [];
  products: String[] = [];
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
    productName: new FormControl('', [Validators.required]),
    productAvailable: new FormControl('', [Validators.required]),
    productSellingPrice: new FormControl('', [Validators.required]),
    productcostPrice: new FormControl('', [Validators.required])
  });

  displayError = 0;


  addProduct() {
    let body = this.productForm.value;
    console.log(body);
    let url = 'api/createproduct';
    this.http.post<Product>(url, body).subscribe((data) => {
      console.log(data);
      this.displayError = 0;
      console.log(this.displayError);
    },
    (error) => {
      this.displayError = 1;
      console.error(error);
      console.log(this.displayError);
    }
  )}

}

