import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBatch, NewBatch } from '../batch.model';

export type PartialUpdateBatch = Partial<IBatch> & Pick<IBatch, 'id'>;

type RestOf<T extends IBatch | NewBatch> = Omit<T, 'purchaseDate'> & {
  purchaseDate?: string | null;
};

export type RestBatch = RestOf<IBatch>;

export type NewRestBatch = RestOf<NewBatch>;

export type PartialUpdateRestBatch = RestOf<PartialUpdateBatch>;

export type EntityResponseType = HttpResponse<IBatch>;
export type EntityArrayResponseType = HttpResponse<IBatch[]>;

@Injectable({ providedIn: 'root' })
export class BatchService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/batches');

  create(batch: NewBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(batch);
    return this.http.post<RestBatch>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(batch: IBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(batch);
    return this.http
      .put<RestBatch>(`${this.resourceUrl}/${this.getBatchIdentifier(batch)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(batch: PartialUpdateBatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(batch);
    return this.http
      .patch<RestBatch>(`${this.resourceUrl}/${this.getBatchIdentifier(batch)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestBatch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBatch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBatchIdentifier(batch: Pick<IBatch, 'id'>): string {
    return batch.id;
  }

  compareBatch(o1: Pick<IBatch, 'id'> | null, o2: Pick<IBatch, 'id'> | null): boolean {
    return o1 && o2 ? this.getBatchIdentifier(o1) === this.getBatchIdentifier(o2) : o1 === o2;
  }

  addBatchToCollectionIfMissing<Type extends Pick<IBatch, 'id'>>(
    batchCollection: Type[],
    ...batchesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const batches: Type[] = batchesToCheck.filter(isPresent);
    if (batches.length > 0) {
      const batchCollectionIdentifiers = batchCollection.map(batchItem => this.getBatchIdentifier(batchItem));
      const batchesToAdd = batches.filter(batchItem => {
        const batchIdentifier = this.getBatchIdentifier(batchItem);
        if (batchCollectionIdentifiers.includes(batchIdentifier)) {
          return false;
        }
        batchCollectionIdentifiers.push(batchIdentifier);
        return true;
      });
      return [...batchesToAdd, ...batchCollection];
    }
    return batchCollection;
  }

  protected convertDateFromClient<T extends IBatch | NewBatch | PartialUpdateBatch>(batch: T): RestOf<T> {
    return {
      ...batch,
      purchaseDate: batch.purchaseDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restBatch: RestBatch): IBatch {
    return {
      ...restBatch,
      purchaseDate: restBatch.purchaseDate ? dayjs(restBatch.purchaseDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBatch>): HttpResponse<IBatch> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBatch[]>): HttpResponse<IBatch[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
