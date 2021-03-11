import { SupplierServiceService } from '../../Services/SupplierService/supplier-service.service';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-addsuppliers',
  templateUrl: './addsuppliers.component.html',
  styleUrls: ['./addsuppliers.component.css'],
})
export class AddsuppliersComponent implements OnInit {
  constructor(private supplierService: SupplierServiceService) {}

  ngOnInit(): void {}

  supplierForm = new FormGroup({
    supplierName: new FormControl('', [Validators.required]),
    supplierEmail: new FormControl('', [Validators.required, Validators.pattern("[^\s@]+@[^\s@]+")]),
    supplierPhone: new FormControl('', [Validators.required, Validators.pattern('[2-9]{2}[0-9]{8}')]),
    supplierAddress: new FormControl('', [Validators.required]),
    supplierPincode: new FormControl('', [Validators.required, Validators.pattern('[1-9]{1}[0-9]{5}')]),
  });

  addSupplier() {
    this.supplierService.addSupplier(this.supplierForm.value);
  }
}
