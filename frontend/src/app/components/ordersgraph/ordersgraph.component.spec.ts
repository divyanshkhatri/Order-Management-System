import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrdersgraphComponent } from './ordersgraph.component';

describe('OrdersgraphComponent', () => {
  let component: OrdersgraphComponent;
  let fixture: ComponentFixture<OrdersgraphComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrdersgraphComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrdersgraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
