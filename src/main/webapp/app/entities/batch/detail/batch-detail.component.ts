import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IBatch } from '../batch.model';

@Component({
  standalone: true,
  selector: 'jhi-batch-detail',
  templateUrl: './batch-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BatchDetailComponent {
  batch = input<IBatch | null>(null);

  previousState(): void {
    window.history.back();
  }
}
