import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../batch.test-samples';

import { BatchFormService } from './batch-form.service';

describe('Batch Form Service', () => {
  let service: BatchFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BatchFormService);
  });

  describe('Service methods', () => {
    describe('createBatchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBatchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            batchNumber: expect.any(Object),
            purchaseQuantity: expect.any(Object),
            purchasePrice: expect.any(Object),
            salePrice: expect.any(Object),
            purchaseDate: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });

      it('passing IBatch should create a new form with FormGroup', () => {
        const formGroup = service.createBatchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            batchNumber: expect.any(Object),
            purchaseQuantity: expect.any(Object),
            purchasePrice: expect.any(Object),
            salePrice: expect.any(Object),
            purchaseDate: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });
    });

    describe('getBatch', () => {
      it('should return NewBatch for default Batch initial value', () => {
        const formGroup = service.createBatchFormGroup(sampleWithNewData);

        const batch = service.getBatch(formGroup) as any;

        expect(batch).toMatchObject(sampleWithNewData);
      });

      it('should return NewBatch for empty Batch initial value', () => {
        const formGroup = service.createBatchFormGroup();

        const batch = service.getBatch(formGroup) as any;

        expect(batch).toMatchObject({});
      });

      it('should return IBatch', () => {
        const formGroup = service.createBatchFormGroup(sampleWithRequiredData);

        const batch = service.getBatch(formGroup) as any;

        expect(batch).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBatch should not enable id FormControl', () => {
        const formGroup = service.createBatchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBatch should disable id FormControl', () => {
        const formGroup = service.createBatchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
