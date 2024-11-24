import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISupplierContact } from '../supplier-contact.model';
import { SupplierContactService } from '../service/supplier-contact.service';

const supplierContactResolve = (route: ActivatedRouteSnapshot): Observable<null | ISupplierContact> => {
  const id = route.params.id;
  if (id) {
    return inject(SupplierContactService)
      .find(id)
      .pipe(
        mergeMap((supplierContact: HttpResponse<ISupplierContact>) => {
          if (supplierContact.body) {
            return of(supplierContact.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default supplierContactResolve;
