import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISupplierContact } from '../supplier-contact.model';
import { SupplierContactService } from '../service/supplier-contact.service';

@Component({
  standalone: true,
  templateUrl: './supplier-contact-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SupplierContactDeleteDialogComponent {
  supplierContact?: ISupplierContact;

  protected supplierContactService = inject(SupplierContactService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.supplierContactService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
