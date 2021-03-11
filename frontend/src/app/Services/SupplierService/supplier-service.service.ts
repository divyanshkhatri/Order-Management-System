import { Supplier } from './../../interfaces/Supplier';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class SupplierServiceService {
  constructor(private http: HttpClient) {}

  addSupplier(supplier: Supplier) {
    let body = supplier;
    let url = 'api/createsupplier';
    this.http.post<Supplier>(url, body).subscribe((data) => {
      console.log(data);
    });
  }
}
