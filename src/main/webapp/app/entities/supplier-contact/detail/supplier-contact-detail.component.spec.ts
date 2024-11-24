import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SupplierContactDetailComponent } from './supplier-contact-detail.component';

describe('SupplierContact Management Detail Component', () => {
  let comp: SupplierContactDetailComponent;
  let fixture: ComponentFixture<SupplierContactDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplierContactDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./supplier-contact-detail.component').then(m => m.SupplierContactDetailComponent),
              resolve: { supplierContact: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SupplierContactDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SupplierContactDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load supplierContact on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SupplierContactDetailComponent);

      // THEN
      expect(instance.supplierContact()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
