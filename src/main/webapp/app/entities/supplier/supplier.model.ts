import { IProduct } from 'app/entities/product/product.model';

export interface ISupplier {
  id: string;
  name?: string | null;
  contactInfo?: string | null;
  address?: string | null;
  products?: Pick<IProduct, 'id' | 'name'>[] | null;
}

export type NewSupplier = Omit<ISupplier, 'id'> & { id: null };
