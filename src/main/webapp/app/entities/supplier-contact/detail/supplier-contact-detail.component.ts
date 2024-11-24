import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISupplierContact } from '../supplier-contact.model';

@Component({
  standalone: true,
  selector: 'jhi-supplier-contact-detail',
  templateUrl: './supplier-contact-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SupplierContactDetailComponent {
  supplierContact = input<ISupplierContact | null>(null);

  previousState(): void {
    window.history.back();
  }
}
