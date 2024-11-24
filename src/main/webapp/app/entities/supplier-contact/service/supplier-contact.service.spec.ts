import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISupplierContact } from '../supplier-contact.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../supplier-contact.test-samples';

import { SupplierContactService } from './supplier-contact.service';

const requireRestSample: ISupplierContact = {
  ...sampleWithRequiredData,
};

describe('SupplierContact Service', () => {
  let service: SupplierContactService;
  let httpMock: HttpTestingController;
  let expectedResult: ISupplierContact | ISupplierContact[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SupplierContactService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SupplierContact', () => {
      const supplierContact = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(supplierContact).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SupplierContact', () => {
      const supplierContact = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(supplierContact).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SupplierContact', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SupplierContact', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SupplierContact', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSupplierContactToCollectionIfMissing', () => {
      it('should add a SupplierContact to an empty array', () => {
        const supplierContact: ISupplierContact = sampleWithRequiredData;
        expectedResult = service.addSupplierContactToCollectionIfMissing([], supplierContact);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(supplierContact);
      });

      it('should not add a SupplierContact to an array that contains it', () => {
        const supplierContact: ISupplierContact = sampleWithRequiredData;
        const supplierContactCollection: ISupplierContact[] = [
          {
            ...supplierContact,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSupplierContactToCollectionIfMissing(supplierContactCollection, supplierContact);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SupplierContact to an array that doesn't contain it", () => {
        const supplierContact: ISupplierContact = sampleWithRequiredData;
        const supplierContactCollection: ISupplierContact[] = [sampleWithPartialData];
        expectedResult = service.addSupplierContactToCollectionIfMissing(supplierContactCollection, supplierContact);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(supplierContact);
      });

      it('should add only unique SupplierContact to an array', () => {
        const supplierContactArray: ISupplierContact[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const supplierContactCollection: ISupplierContact[] = [sampleWithRequiredData];
        expectedResult = service.addSupplierContactToCollectionIfMissing(supplierContactCollection, ...supplierContactArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const supplierContact: ISupplierContact = sampleWithRequiredData;
        const supplierContact2: ISupplierContact = sampleWithPartialData;
        expectedResult = service.addSupplierContactToCollectionIfMissing([], supplierContact, supplierContact2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(supplierContact);
        expect(expectedResult).toContain(supplierContact2);
      });

      it('should accept null and undefined values', () => {
        const supplierContact: ISupplierContact = sampleWithRequiredData;
        expectedResult = service.addSupplierContactToCollectionIfMissing([], null, supplierContact, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(supplierContact);
      });

      it('should return initial array if no SupplierContact is added', () => {
        const supplierContactCollection: ISupplierContact[] = [sampleWithRequiredData];
        expectedResult = service.addSupplierContactToCollectionIfMissing(supplierContactCollection, undefined, null);
        expect(expectedResult).toEqual(supplierContactCollection);
      });
    });

    describe('compareSupplierContact', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSupplierContact(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSupplierContact(entity1, entity2);
        const compareResult2 = service.compareSupplierContact(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSupplierContact(entity1, entity2);
        const compareResult2 = service.compareSupplierContact(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSupplierContact(entity1, entity2);
        const compareResult2 = service.compareSupplierContact(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
