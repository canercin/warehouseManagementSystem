import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: '527f9722-2187-4965-ba63-878729fcfce9',
};

export const sampleWithPartialData: IProduct = {
  id: '956d3b61-cd33-4e38-a157-aadf3e0516ae',
  quantityType: 'ML',
  updatedAt: dayjs('2024-11-19'),
};

export const sampleWithFullData: IProduct = {
  id: 'be37b751-94ec-4095-bb4a-3a40a78b5f46',
  name: 'delightfully yahoo yowza',
  description: 'only',
  createdAt: dayjs('2024-11-19'),
  quantityType: 'L',
  updatedAt: dayjs('2024-11-19'),
};

export const sampleWithNewData: NewProduct = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
