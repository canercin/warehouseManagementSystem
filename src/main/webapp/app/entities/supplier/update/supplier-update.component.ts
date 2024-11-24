import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ISupplier } from '../supplier.model';
import { SupplierService } from '../service/supplier.service';
import { SupplierFormGroup, SupplierFormService } from './supplier-form.service';

@Component({
  standalone: true,
  selector: 'jhi-supplier-update',
  templateUrl: './supplier-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SupplierUpdateComponent implements OnInit {
  isSaving = false;
  supplier: ISupplier | null = null;

  productsSharedCollection: IProduct[] = [];

  protected supplierService = inject(SupplierService);
  protected supplierFormService = inject(SupplierFormService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SupplierFormGroup = this.supplierFormService.createSupplierFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplier }) => {
      this.supplier = supplier;
      if (supplier) {
        this.updateForm(supplier);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const supplier = this.supplierFormService.getSupplier(this.editForm);
    if (supplier.id !== null) {
      this.subscribeToSaveResponse(this.supplierService.update(supplier));
    } else {
      this.subscribeToSaveResponse(this.supplierService.create(supplier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISupplier>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(supplier: ISupplier): void {
    this.supplier = supplier;
    this.supplierFormService.resetForm(this.editForm, supplier);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      ...(supplier.products ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, ...(this.supplier?.products ?? [])),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
