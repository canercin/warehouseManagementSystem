import { IWarehouse, NewWarehouse } from './warehouse.model';

export const sampleWithRequiredData: IWarehouse = {
  id: 'cc9d7329-baa9-4bdb-b215-682c4c27805e',
};

export const sampleWithPartialData: IWarehouse = {
  id: 'ef7ceb8b-4793-431c-a7b2-8b25eacdb295',
  name: 'carefree who toward',
};

export const sampleWithFullData: IWarehouse = {
  id: '59605230-1409-42ae-b9bd-69a2638859b0',
  name: 'disconnection anti voluminous',
};

export const sampleWithNewData: NewWarehouse = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
