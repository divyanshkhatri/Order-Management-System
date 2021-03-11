import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CustomerData, CustomersComponent } from '../customers/customers.component';
import { CustomerServiceService } from '../../Services/CustomerService/customer-service.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-addcustomers',
  templateUrl: './addcustomers.component.html',
  styleUrls: ['./addcustomers.component.css']
})
export class AddcustomersComponent implements OnInit {

  constructor(private customerService : CustomerServiceService, private http : HttpClient) {
  }

  ngOnInit(): void {
  }

  custForm = new FormGroup({

    customerName: new FormControl('',[Validators.required]),
    customerEmail : new FormControl('',[Validators.required, Validators.pattern("[^\s@]+@[^\s@]+")]),
    customerPhone : new FormControl('',[Validators.required, Validators.pattern('[2-9]{2}[0-9]{8}')]),
    customerAddress : new FormControl('',[Validators.required]),
    customerPincode: new FormControl('', [Validators.required, Validators.pattern('[1-9]{1}[0-9]{5}')])
  });

  get customerName() {
    return this.custForm.get('customerName');
  }
  get customerEmail() {
    return this.custForm.get('customerEmail');
  }
  get customerPhone() {
    return this.custForm.get('customerPhone');
  }
  get customerAddress() {
    return this.custForm.get('customerAddress');
  }
  get customerPincode() {
    return this.custForm.get('customerPincode');
  }

  addCustomer() {
    this.customerService.addCustomer(this.custForm.value);
  }
}
