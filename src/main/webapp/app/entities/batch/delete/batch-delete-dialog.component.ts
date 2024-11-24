import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBatch } from '../batch.model';
import { BatchService } from '../service/batch.service';

@Component({
  standalone: true,
  templateUrl: './batch-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BatchDeleteDialogComponent {
  batch?: IBatch;

  protected batchService = inject(BatchService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.batchService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
