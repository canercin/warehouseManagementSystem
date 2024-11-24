import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { BatchService } from '../service/batch.service';
import { IBatch } from '../batch.model';
import { BatchFormService } from './batch-form.service';

import { BatchUpdateComponent } from './batch-update.component';

describe('Batch Management Update Component', () => {
  let comp: BatchUpdateComponent;
  let fixture: ComponentFixture<BatchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let batchFormService: BatchFormService;
  let batchService: BatchService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BatchUpdateComponent],
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
      .overrideTemplate(BatchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BatchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    batchFormService = TestBed.inject(BatchFormService);
    batchService = TestBed.inject(BatchService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const batch: IBatch = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const product: IProduct = { id: '327ce8b5-6783-4f57-80d0-9ab53bc299c8' };
      batch.product = product;

      const productCollection: IProduct[] = [{ id: '8217dfc4-0e52-4435-b674-5595533a645b' }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ batch });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const batch: IBatch = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const product: IProduct = { id: '099fb24c-7ff1-477d-98fe-0e00799e64f6' };
      batch.product = product;

      activatedRoute.data = of({ batch });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.batch).toEqual(batch);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBatch>>();
      const batch = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(batchFormService, 'getBatch').mockReturnValue(batch);
      jest.spyOn(batchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ batch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: batch }));
      saveSubject.complete();

      // THEN
      expect(batchFormService.getBatch).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(batchService.update).toHaveBeenCalledWith(expect.objectContaining(batch));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBatch>>();
      const batch = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(batchFormService, 'getBatch').mockReturnValue({ id: null });
      jest.spyOn(batchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ batch: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: batch }));
      saveSubject.complete();

      // THEN
      expect(batchFormService.getBatch).toHaveBeenCalled();
      expect(batchService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBatch>>();
      const batch = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(batchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ batch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(batchService.update).toHaveBeenCalled();
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
