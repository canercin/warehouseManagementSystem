import { ISupplier } from 'app/entities/supplier/supplier.model';

export interface ISupplierContact {
  id: number;
  name?: string | null;
  email?: string | null;
  phone?: string | null;
  supplier?: Pick<ISupplier, 'id' | 'name'> | null;
}

export type NewSupplierContact = Omit<ISupplierContact, 'id'> & { id: null };
