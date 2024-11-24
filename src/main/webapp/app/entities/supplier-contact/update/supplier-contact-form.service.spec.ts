import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../supplier-contact.test-samples';

import { SupplierContactFormService } from './supplier-contact-form.service';

describe('SupplierContact Form Service', () => {
  let service: SupplierContactFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SupplierContactFormService);
  });

  describe('Service methods', () => {
    describe('createSupplierContactFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSupplierContactFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            supplier: expect.any(Object),
          }),
        );
      });

      it('passing ISupplierContact should create a new form with FormGroup', () => {
        const formGroup = service.createSupplierContactFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            phone: expect.any(Object),
            supplier: expect.any(Object),
          }),
        );
      });
    });

    describe('getSupplierContact', () => {
      it('should return NewSupplierContact for default SupplierContact initial value', () => {
        const formGroup = service.createSupplierContactFormGroup(sampleWithNewData);

        const supplierContact = service.getSupplierContact(formGroup) as any;

        expect(supplierContact).toMatchObject(sampleWithNewData);
      });

      it('should return NewSupplierContact for empty SupplierContact initial value', () => {
        const formGroup = service.createSupplierContactFormGroup();

        const supplierContact = service.getSupplierContact(formGroup) as any;

        expect(supplierContact).toMatchObject({});
      });

      it('should return ISupplierContact', () => {
        const formGroup = service.createSupplierContactFormGroup(sampleWithRequiredData);

        const supplierContact = service.getSupplierContact(formGroup) as any;

        expect(supplierContact).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISupplierContact should not enable id FormControl', () => {
        const formGroup = service.createSupplierContactFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSupplierContact should disable id FormControl', () => {
        const formGroup = service.createSupplierContactFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
