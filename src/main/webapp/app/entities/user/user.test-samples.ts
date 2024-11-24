import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 24072,
  login: '4TAW@kGRwE2\\er',
};

export const sampleWithPartialData: IUser = {
  id: 24248,
  login: 'i',
};

export const sampleWithFullData: IUser = {
  id: 7113,
  login: 'n&IuL@5\\gH\\9i0\\:F6',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
