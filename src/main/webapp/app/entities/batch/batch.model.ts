import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';

export interface IBatch {
  id: string;
  batchNumber?: number | null;
  purchaseQuantity?: number | null;
  purchasePrice?: number | null;
  salePrice?: number | null;
  purchaseDate?: dayjs.Dayjs | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
}

export type NewBatch = Omit<IBatch, 'id'> & { id: null };
