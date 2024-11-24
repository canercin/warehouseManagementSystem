import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBatch } from '../batch.model';
import { BatchService } from '../service/batch.service';

const batchResolve = (route: ActivatedRouteSnapshot): Observable<null | IBatch> => {
  const id = route.params.id;
  if (id) {
    return inject(BatchService)
      .find(id)
      .pipe(
        mergeMap((batch: HttpResponse<IBatch>) => {
          if (batch.body) {
            return of(batch.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default batchResolve;
