import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISupplierContact, NewSupplierContact } from '../supplier-contact.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISupplierContact for edit and NewSupplierContactFormGroupInput for create.
 */
type SupplierContactFormGroupInput = ISupplierContact | PartialWithRequiredKeyOf<NewSupplierContact>;

type SupplierContactFormDefaults = Pick<NewSupplierContact, 'id'>;

type SupplierContactFormGroupContent = {
  id: FormControl<ISupplierContact['id'] | NewSupplierContact['id']>;
  name: FormControl<ISupplierContact['name']>;
  email: FormControl<ISupplierContact['email']>;
  phone: FormControl<ISupplierContact['phone']>;
  supplier: FormControl<ISupplierContact['supplier']>;
};

export type SupplierContactFormGroup = FormGroup<SupplierContactFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SupplierContactFormService {
  createSupplierContactFormGroup(supplierContact: SupplierContactFormGroupInput = { id: null }): SupplierContactFormGroup {
    const supplierContactRawValue = {
      ...this.getFormDefaults(),
      ...supplierContact,
    };
    return new FormGroup<SupplierContactFormGroupContent>({
      id: new FormControl(
        { value: supplierContactRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(supplierContactRawValue.name),
      email: new FormControl(supplierContactRawValue.email),
      phone: new FormControl(supplierContactRawValue.phone),
      supplier: new FormControl(supplierContactRawValue.supplier),
    });
  }

  getSupplierContact(form: SupplierContactFormGroup): ISupplierContact | NewSupplierContact {
    return form.getRawValue() as ISupplierContact | NewSupplierContact;
  }

  resetForm(form: SupplierContactFormGroup, supplierContact: SupplierContactFormGroupInput): void {
    const supplierContactRawValue = { ...this.getFormDefaults(), ...supplierContact };
    form.reset(
      {
        ...supplierContactRawValue,
        id: { value: supplierContactRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SupplierContactFormDefaults {
    return {
      id: null,
    };
  }
}
