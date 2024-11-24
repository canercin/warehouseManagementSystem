import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBatch, NewBatch } from '../batch.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBatch for edit and NewBatchFormGroupInput for create.
 */
type BatchFormGroupInput = IBatch | PartialWithRequiredKeyOf<NewBatch>;

type BatchFormDefaults = Pick<NewBatch, 'id'>;

type BatchFormGroupContent = {
  id: FormControl<IBatch['id'] | NewBatch['id']>;
  batchNumber: FormControl<IBatch['batchNumber']>;
  purchaseQuantity: FormControl<IBatch['purchaseQuantity']>;
  purchasePrice: FormControl<IBatch['purchasePrice']>;
  salePrice: FormControl<IBatch['salePrice']>;
  purchaseDate: FormControl<IBatch['purchaseDate']>;
  product: FormControl<IBatch['product']>;
};

export type BatchFormGroup = FormGroup<BatchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BatchFormService {
  createBatchFormGroup(batch: BatchFormGroupInput = { id: null }): BatchFormGroup {
    const batchRawValue = {
      ...this.getFormDefaults(),
      ...batch,
    };
    return new FormGroup<BatchFormGroupContent>({
      id: new FormControl(
        { value: batchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      batchNumber: new FormControl(batchRawValue.batchNumber),
      purchaseQuantity: new FormControl(batchRawValue.purchaseQuantity),
      purchasePrice: new FormControl(batchRawValue.purchasePrice),
      salePrice: new FormControl(batchRawValue.salePrice),
      purchaseDate: new FormControl(batchRawValue.purchaseDate),
      product: new FormControl(batchRawValue.product),
    });
  }

  getBatch(form: BatchFormGroup): IBatch | NewBatch {
    return form.getRawValue() as IBatch | NewBatch;
  }

  resetForm(form: BatchFormGroup, batch: BatchFormGroupInput): void {
    const batchRawValue = { ...this.getFormDefaults(), ...batch };
    form.reset(
      {
        ...batchRawValue,
        id: { value: batchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BatchFormDefaults {
    return {
      id: null,
    };
  }
}
