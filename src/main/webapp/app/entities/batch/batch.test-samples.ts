import dayjs from 'dayjs/esm';

import { IBatch, NewBatch } from './batch.model';

export const sampleWithRequiredData: IBatch = {
  id: 'b863e956-20a3-4f49-9f3b-65ff60d3d2c7',
};

export const sampleWithPartialData: IBatch = {
  id: '07ab0da6-c986-4e52-9e63-7e50386ff45f',
  purchasePrice: 24581.93,
};

export const sampleWithFullData: IBatch = {
  id: '8d764683-25fd-4272-865b-65eef96a6c6b',
  batchNumber: 9121,
  purchaseQuantity: 19400,
  purchasePrice: 18581.33,
  salePrice: 14191.25,
  purchaseDate: dayjs('2024-11-20'),
};

export const sampleWithNewData: NewBatch = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
