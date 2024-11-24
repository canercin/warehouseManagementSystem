import dayjs from 'dayjs/esm';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { QuantityTypes } from 'app/entities/enumerations/quantity-types.model';

export interface IProduct {
  id: string;
  name?: string | null;
  description?: string | null;
  createdAt?: dayjs.Dayjs | null;
  quantityType?: keyof typeof QuantityTypes | null;
  updatedAt?: dayjs.Dayjs | null;
  warehouses?: Pick<IWarehouse, 'id'>[] | null;
  suppliers?: Pick<ISupplier, 'id'>[] | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
