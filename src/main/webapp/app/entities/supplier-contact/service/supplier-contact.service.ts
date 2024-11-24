import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISupplierContact, NewSupplierContact } from '../supplier-contact.model';

export type PartialUpdateSupplierContact = Partial<ISupplierContact> & Pick<ISupplierContact, 'id'>;

export type EntityResponseType = HttpResponse<ISupplierContact>;
export type EntityArrayResponseType = HttpResponse<ISupplierContact[]>;

@Injectable({ providedIn: 'root' })
export class SupplierContactService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/supplier-contacts');

  create(supplierContact: NewSupplierContact): Observable<EntityResponseType> {
    return this.http.post<ISupplierContact>(this.resourceUrl, supplierContact, { observe: 'response' });
  }

  update(supplierContact: ISupplierContact): Observable<EntityResponseType> {
    return this.http.put<ISupplierContact>(`${this.resourceUrl}/${this.getSupplierContactIdentifier(supplierContact)}`, supplierContact, {
      observe: 'response',
    });
  }

  partialUpdate(supplierContact: PartialUpdateSupplierContact): Observable<EntityResponseType> {
    return this.http.patch<ISupplierContact>(`${this.resourceUrl}/${this.getSupplierContactIdentifier(supplierContact)}`, supplierContact, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISupplierContact>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISupplierContact[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSupplierContactIdentifier(supplierContact: Pick<ISupplierContact, 'id'>): number {
    return supplierContact.id;
  }

  compareSupplierContact(o1: Pick<ISupplierContact, 'id'> | null, o2: Pick<ISupplierContact, 'id'> | null): boolean {
    return o1 && o2 ? this.getSupplierContactIdentifier(o1) === this.getSupplierContactIdentifier(o2) : o1 === o2;
  }

  addSupplierContactToCollectionIfMissing<Type extends Pick<ISupplierContact, 'id'>>(
    supplierContactCollection: Type[],
    ...supplierContactsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const supplierContacts: Type[] = supplierContactsToCheck.filter(isPresent);
    if (supplierContacts.length > 0) {
      const supplierContactCollectionIdentifiers = supplierContactCollection.map(supplierContactItem =>
        this.getSupplierContactIdentifier(supplierContactItem),
      );
      const supplierContactsToAdd = supplierContacts.filter(supplierContactItem => {
        const supplierContactIdentifier = this.getSupplierContactIdentifier(supplierContactItem);
        if (supplierContactCollectionIdentifiers.includes(supplierContactIdentifier)) {
          return false;
        }
        supplierContactCollectionIdentifiers.push(supplierContactIdentifier);
        return true;
      });
      return [...supplierContactsToAdd, ...supplierContactCollection];
    }
    return supplierContactCollection;
  }
}
