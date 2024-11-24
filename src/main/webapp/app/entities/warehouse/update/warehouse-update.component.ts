import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IWarehouse } from '../warehouse.model';
import { WarehouseService } from '../service/warehouse.service';
import { WarehouseFormGroup, WarehouseFormService } from './warehouse-form.service';

@Component({
  standalone: true,
  selector: 'jhi-warehouse-update',
  templateUrl: './warehouse-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WarehouseUpdateComponent implements OnInit {
  isSaving = false;
  warehouse: IWarehouse | null = null;

  productsSharedCollection: IProduct[] = [];

  protected warehouseService = inject(WarehouseService);
  protected warehouseFormService = inject(WarehouseFormService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WarehouseFormGroup = this.warehouseFormService.createWarehouseFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ warehouse }) => {
      this.warehouse = warehouse;
      if (warehouse) {
        this.updateForm(warehouse);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const warehouse = this.warehouseFormService.getWarehouse(this.editForm);
    if (warehouse.id !== null) {
      this.subscribeToSaveResponse(this.warehouseService.update(warehouse));
    } else {
      this.subscribeToSaveResponse(this.warehouseService.create(warehouse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWarehouse>>): void {
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

  protected updateForm(warehouse: IWarehouse): void {
    this.warehouse = warehouse;
    this.warehouseFormService.resetForm(this.editForm, warehouse);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      ...(warehouse.products ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, ...(this.warehouse?.products ?? [])),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
