import { Product } from './../../interfaces/Product';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ProductServiceService {
  constructor(private http: HttpClient) {}

  addProduct(product: Product) {
    // console.log(customer);
    let body = product;
    let url = 'api/createproduct';
    this.http.post<Product>(url, body).subscribe((data) => {
      console.log(data);
    });
  }

  displayError = 0;

  createOrderProduct(customerorderproduct: any, id: number) {

    customerorderproduct.orderId = id;
    let body = customerorderproduct;
    let url = 'api/createorderproduct';
    this.http.post<Product>(url, body).subscribe((data) => {
      console.log(data);
      this.displayError = 0;

    },
    (error) => {
      this.displayError = 1;
      console.error(error);

    });

  }
}

export interface Customerorderproduct {

  orderId: number,
  productQuantity: number,
  productId: number

}
