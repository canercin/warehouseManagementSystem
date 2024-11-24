import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '5d94c9f9-2eb8-4747-8b47-546570290976',
};

export const sampleWithPartialData: IAuthority = {
  name: 'd0a8443f-1f91-48e3-8ac3-1ece756d5cf4',
};

export const sampleWithFullData: IAuthority = {
  name: 'c5c4b611-5b55-41fb-9395-56fbfacbb651',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
