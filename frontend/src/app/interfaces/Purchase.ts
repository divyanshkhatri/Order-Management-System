import { Supplier } from './Supplier';

export interface Purchase {
  purchaseId: number;
  purchaseStatus: string;
  supplier: Supplier;
}
