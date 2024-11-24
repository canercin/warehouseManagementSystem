import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IBatch } from '../batch.model';
import { BatchService } from '../service/batch.service';
import { BatchFormGroup, BatchFormService } from './batch-form.service';

@Component({
  standalone: true,
  selector: 'jhi-batch-update',
  templateUrl: './batch-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BatchUpdateComponent implements OnInit {
  isSaving = false;
  batch: IBatch | null = null;

  productsSharedCollection: IProduct[] = [];

  protected batchService = inject(BatchService);
  protected batchFormService = inject(BatchFormService);
  protected productService = inject(ProductService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BatchFormGroup = this.batchFormService.createBatchFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ batch }) => {
      this.batch = batch;
      if (batch) {
        this.updateForm(batch);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const batch = this.batchFormService.getBatch(this.editForm);
    if (batch.id !== null) {
      this.subscribeToSaveResponse(this.batchService.update(batch));
    } else {
      this.subscribeToSaveResponse(this.batchService.create(batch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBatch>>): void {
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

  protected updateForm(batch: IBatch): void {
    this.batch = batch;
    this.batchFormService.resetForm(this.editForm, batch);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      batch.product,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.batch?.product)))
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
