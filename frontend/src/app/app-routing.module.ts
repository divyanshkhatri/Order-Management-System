import { AllproductsComponent } from './components/allproducts/allproducts.component';
import { AddsuppliersComponent } from './components/addsuppliers/addsuppliers.component';
import { AddcustomersComponent } from './components/addcustomers/addcustomers.component';
import { SuppliersComponent } from './suppliers/suppliers.component';
import { CustomersComponent } from './components/customers/customers.component';

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { OrdersComponent } from './components/orders/orders.component';
import { HomeComponent } from './components/HomeComponent/home/home.component';
import { GraphsComponent } from './components/graphs/graphs.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'customers',
    component: CustomersComponent,
  },

  {
    path: 'orders',
    component: OrdersComponent,
  },

  {
    path: 'suppliers',
    component: SuppliersComponent,
  },

  {
    path: 'addcustomers',
    component: AddcustomersComponent,
  },
  {
    path: 'addsuppliers',
    component: AddsuppliersComponent,
  },
  {
    path: 'allproducts',
    component: AllproductsComponent,
  },
  {
    path: 'graphs',
    component: GraphsComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
