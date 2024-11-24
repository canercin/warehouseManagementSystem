import { ISupplierContact, NewSupplierContact } from './supplier-contact.model';

export const sampleWithRequiredData: ISupplierContact = {
  id: 32678,
};

export const sampleWithPartialData: ISupplierContact = {
  id: 11074,
  email: 'Rick_Harvey@yahoo.com',
  phone: '927-657-6200 x30057',
};

export const sampleWithFullData: ISupplierContact = {
  id: 9007,
  name: 'failing down appropriate',
  email: 'Lurline_Greenfelder@gmail.com',
  phone: '399-423-7706 x0971',
};

export const sampleWithNewData: NewSupplierContact = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
