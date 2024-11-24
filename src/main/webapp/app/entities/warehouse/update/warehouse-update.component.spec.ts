import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { WarehouseService } from '../service/warehouse.service';
import { IWarehouse } from '../warehouse.model';
import { WarehouseFormService } from './warehouse-form.service';

import { WarehouseUpdateComponent } from './warehouse-update.component';

describe('Warehouse Management Update Component', () => {
  let comp: WarehouseUpdateComponent;
  let fixture: ComponentFixture<WarehouseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let warehouseFormService: WarehouseFormService;
  let warehouseService: WarehouseService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WarehouseUpdateComponent],
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
      .overrideTemplate(WarehouseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WarehouseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    warehouseFormService = TestBed.inject(WarehouseFormService);
    warehouseService = TestBed.inject(WarehouseService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const warehouse: IWarehouse = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const products: IProduct[] = [{ id: '945c0c26-f748-4835-a196-b13b7ee383ca' }];
      warehouse.products = products;

      const productCollection: IProduct[] = [{ id: 'e57732be-6207-4d9e-adf4-3ba3f8e032d7' }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [...products];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ warehouse });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const warehouse: IWarehouse = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const products: IProduct = { id: '95088d35-df5f-491d-82a7-ee7ae612d126' };
      warehouse.products = [products];

      activatedRoute.data = of({ warehouse });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(products);
      expect(comp.warehouse).toEqual(warehouse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWarehouse>>();
      const warehouse = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(warehouseFormService, 'getWarehouse').mockReturnValue(warehouse);
      jest.spyOn(warehouseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warehouse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: warehouse }));
      saveSubject.complete();

      // THEN
      expect(warehouseFormService.getWarehouse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(warehouseService.update).toHaveBeenCalledWith(expect.objectContaining(warehouse));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWarehouse>>();
      const warehouse = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(warehouseFormService, 'getWarehouse').mockReturnValue({ id: null });
      jest.spyOn(warehouseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warehouse: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: warehouse }));
      saveSubject.complete();

      // THEN
      expect(warehouseFormService.getWarehouse).toHaveBeenCalled();
      expect(warehouseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWarehouse>>();
      const warehouse = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(warehouseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ warehouse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(warehouseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
