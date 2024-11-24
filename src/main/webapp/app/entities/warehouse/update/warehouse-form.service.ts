import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IWarehouse, NewWarehouse } from '../warehouse.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWarehouse for edit and NewWarehouseFormGroupInput for create.
 */
type WarehouseFormGroupInput = IWarehouse | PartialWithRequiredKeyOf<NewWarehouse>;

type WarehouseFormDefaults = Pick<NewWarehouse, 'id' | 'products'>;

type WarehouseFormGroupContent = {
  id: FormControl<IWarehouse['id'] | NewWarehouse['id']>;
  name: FormControl<IWarehouse['name']>;
  products: FormControl<IWarehouse['products']>;
};

export type WarehouseFormGroup = FormGroup<WarehouseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WarehouseFormService {
  createWarehouseFormGroup(warehouse: WarehouseFormGroupInput = { id: null }): WarehouseFormGroup {
    const warehouseRawValue = {
      ...this.getFormDefaults(),
      ...warehouse,
    };
    return new FormGroup<WarehouseFormGroupContent>({
      id: new FormControl(
        { value: warehouseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(warehouseRawValue.name),
      products: new FormControl(warehouseRawValue.products ?? []),
    });
  }

  getWarehouse(form: WarehouseFormGroup): IWarehouse | NewWarehouse {
    return form.getRawValue() as IWarehouse | NewWarehouse;
  }

  resetForm(form: WarehouseFormGroup, warehouse: WarehouseFormGroupInput): void {
    const warehouseRawValue = { ...this.getFormDefaults(), ...warehouse };
    form.reset(
      {
        ...warehouseRawValue,
        id: { value: warehouseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WarehouseFormDefaults {
    return {
      id: null,
      products: [],
    };
  }
}
