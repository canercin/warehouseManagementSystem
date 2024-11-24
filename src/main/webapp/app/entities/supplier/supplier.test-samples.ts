import { ISupplier, NewSupplier } from './supplier.model';

export const sampleWithRequiredData: ISupplier = {
  id: 'e629a9f1-1854-4e5c-b476-0ecda3feb2b6',
};

export const sampleWithPartialData: ISupplier = {
  id: '97fa144a-a3a0-4853-8c1d-a490dc8c64b5',
  name: 'married frankly',
  contactInfo: 'scrape big',
};

export const sampleWithFullData: ISupplier = {
  id: '49d4cb87-05ef-4342-956c-87593c90d097',
  name: 'detain imaginative',
  contactInfo: 'authorized handle',
  address: 'pish whereas enormously',
};

export const sampleWithNewData: NewSupplier = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
