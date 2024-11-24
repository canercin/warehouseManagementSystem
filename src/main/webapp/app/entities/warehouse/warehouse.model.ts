import { IProduct } from 'app/entities/product/product.model';

export interface IWarehouse {
  id: string;
  name?: string | null;
  products?: Pick<IProduct, 'id' | 'name'>[] | null;
}

export type NewWarehouse = Omit<IWarehouse, 'id'> & { id: null };
