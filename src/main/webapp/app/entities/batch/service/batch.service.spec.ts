import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBatch } from '../batch.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../batch.test-samples';

import { BatchService, RestBatch } from './batch.service';

const requireRestSample: RestBatch = {
  ...sampleWithRequiredData,
  purchaseDate: sampleWithRequiredData.purchaseDate?.format(DATE_FORMAT),
};

describe('Batch Service', () => {
  let service: BatchService;
  let httpMock: HttpTestingController;
  let expectedResult: IBatch | IBatch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BatchService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Batch', () => {
      const batch = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(batch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Batch', () => {
      const batch = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(batch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Batch', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Batch', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Batch', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBatchToCollectionIfMissing', () => {
      it('should add a Batch to an empty array', () => {
        const batch: IBatch = sampleWithRequiredData;
        expectedResult = service.addBatchToCollectionIfMissing([], batch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(batch);
      });

      it('should not add a Batch to an array that contains it', () => {
        const batch: IBatch = sampleWithRequiredData;
        const batchCollection: IBatch[] = [
          {
            ...batch,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBatchToCollectionIfMissing(batchCollection, batch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Batch to an array that doesn't contain it", () => {
        const batch: IBatch = sampleWithRequiredData;
        const batchCollection: IBatch[] = [sampleWithPartialData];
        expectedResult = service.addBatchToCollectionIfMissing(batchCollection, batch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(batch);
      });

      it('should add only unique Batch to an array', () => {
        const batchArray: IBatch[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const batchCollection: IBatch[] = [sampleWithRequiredData];
        expectedResult = service.addBatchToCollectionIfMissing(batchCollection, ...batchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const batch: IBatch = sampleWithRequiredData;
        const batch2: IBatch = sampleWithPartialData;
        expectedResult = service.addBatchToCollectionIfMissing([], batch, batch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(batch);
        expect(expectedResult).toContain(batch2);
      });

      it('should accept null and undefined values', () => {
        const batch: IBatch = sampleWithRequiredData;
        expectedResult = service.addBatchToCollectionIfMissing([], null, batch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(batch);
      });

      it('should return initial array if no Batch is added', () => {
        const batchCollection: IBatch[] = [sampleWithRequiredData];
        expectedResult = service.addBatchToCollectionIfMissing(batchCollection, undefined, null);
        expect(expectedResult).toEqual(batchCollection);
      });
    });

    describe('compareBatch', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBatch(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareBatch(entity1, entity2);
        const compareResult2 = service.compareBatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareBatch(entity1, entity2);
        const compareResult2 = service.compareBatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareBatch(entity1, entity2);
        const compareResult2 = service.compareBatch(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
