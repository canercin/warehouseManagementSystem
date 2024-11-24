import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';
import { ISupplierContact } from '../supplier-contact.model';
import { SupplierContactService } from '../service/supplier-contact.service';
import { SupplierContactFormGroup, SupplierContactFormService } from './supplier-contact-form.service';

@Component({
  standalone: true,
  selector: 'jhi-supplier-contact-update',
  templateUrl: './supplier-contact-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SupplierContactUpdateComponent implements OnInit {
  isSaving = false;
  supplierContact: ISupplierContact | null = null;

  suppliersSharedCollection: ISupplier[] = [];

  protected supplierContactService = inject(SupplierContactService);
  protected supplierContactFormService = inject(SupplierContactFormService);
  protected supplierService = inject(SupplierService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SupplierContactFormGroup = this.supplierContactFormService.createSupplierContactFormGroup();

  compareSupplier = (o1: ISupplier | null, o2: ISupplier | null): boolean => this.supplierService.compareSupplier(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplierContact }) => {
      this.supplierContact = supplierContact;
      if (supplierContact) {
        this.updateForm(supplierContact);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const supplierContact = this.supplierContactFormService.getSupplierContact(this.editForm);
    if (supplierContact.id !== null) {
      this.subscribeToSaveResponse(this.supplierContactService.update(supplierContact));
    } else {
      this.subscribeToSaveResponse(this.supplierContactService.create(supplierContact));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISupplierContact>>): void {
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

  protected updateForm(supplierContact: ISupplierContact): void {
    this.supplierContact = supplierContact;
    this.supplierContactFormService.resetForm(this.editForm, supplierContact);

    this.suppliersSharedCollection = this.supplierService.addSupplierToCollectionIfMissing<ISupplier>(
      this.suppliersSharedCollection,
      supplierContact.supplier,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.supplierService
      .query()
      .pipe(map((res: HttpResponse<ISupplier[]>) => res.body ?? []))
      .pipe(
        map((suppliers: ISupplier[]) =>
          this.supplierService.addSupplierToCollectionIfMissing<ISupplier>(suppliers, this.supplierContact?.supplier),
        ),
      )
      .subscribe((suppliers: ISupplier[]) => (this.suppliersSharedCollection = suppliers));
  }
}
