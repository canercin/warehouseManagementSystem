import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';
import { SupplierContactService } from '../service/supplier-contact.service';
import { ISupplierContact } from '../supplier-contact.model';
import { SupplierContactFormService } from './supplier-contact-form.service';

import { SupplierContactUpdateComponent } from './supplier-contact-update.component';

describe('SupplierContact Management Update Component', () => {
  let comp: SupplierContactUpdateComponent;
  let fixture: ComponentFixture<SupplierContactUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let supplierContactFormService: SupplierContactFormService;
  let supplierContactService: SupplierContactService;
  let supplierService: SupplierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SupplierContactUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SupplierContactUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SupplierContactUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    supplierContactFormService = TestBed.inject(SupplierContactFormService);
    supplierContactService = TestBed.inject(SupplierContactService);
    supplierService = TestBed.inject(SupplierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Supplier query and add missing value', () => {
      const supplierContact: ISupplierContact = { id: 456 };
      const supplier: ISupplier = { id: '0715db1f-6696-4afd-8d47-955731ae6270' };
      supplierContact.supplier = supplier;

      const supplierCollection: ISupplier[] = [{ id: 'f98be534-20cd-4e63-9bed-e95cd0a23212' }];
      jest.spyOn(supplierService, 'query').mockReturnValue(of(new HttpResponse({ body: supplierCollection })));
      const additionalSuppliers = [supplier];
      const expectedCollection: ISupplier[] = [...additionalSuppliers, ...supplierCollection];
      jest.spyOn(supplierService, 'addSupplierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ supplierContact });
      comp.ngOnInit();

      expect(supplierService.query).toHaveBeenCalled();
      expect(supplierService.addSupplierToCollectionIfMissing).toHaveBeenCalledWith(
        supplierCollection,
        ...additionalSuppliers.map(expect.objectContaining),
      );
      expect(comp.suppliersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const supplierContact: ISupplierContact = { id: 456 };
      const supplier: ISupplier = { id: '3750d205-ffa9-4a37-a1f5-c13b9db05c1c' };
      supplierContact.supplier = supplier;

      activatedRoute.data = of({ supplierContact });
      comp.ngOnInit();

      expect(comp.suppliersSharedCollection).toContain(supplier);
      expect(comp.supplierContact).toEqual(supplierContact);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISupplierContact>>();
      const supplierContact = { id: 123 };
      jest.spyOn(supplierContactFormService, 'getSupplierContact').mockReturnValue(supplierContact);
      jest.spyOn(supplierContactService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplierContact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: supplierContact }));
      saveSubject.complete();

      // THEN
      expect(supplierContactFormService.getSupplierContact).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(supplierContactService.update).toHaveBeenCalledWith(expect.objectContaining(supplierContact));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISupplierContact>>();
      const supplierContact = { id: 123 };
      jest.spyOn(supplierContactFormService, 'getSupplierContact').mockReturnValue({ id: null });
      jest.spyOn(supplierContactService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplierContact: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: supplierContact }));
      saveSubject.complete();

      // THEN
      expect(supplierContactFormService.getSupplierContact).toHaveBeenCalled();
      expect(supplierContactService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISupplierContact>>();
      const supplierContact = { id: 123 };
      jest.spyOn(supplierContactService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ supplierContact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(supplierContactService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSupplier', () => {
      it('Should forward to supplierService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(supplierService, 'compareSupplier');
        comp.compareSupplier(entity, entity2);
        expect(supplierService.compareSupplier).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
