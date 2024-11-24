import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import BatchResolve from './route/batch-routing-resolve.service';

const batchRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/batch.component').then(m => m.BatchComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/batch-detail.component').then(m => m.BatchDetailComponent),
    resolve: {
      batch: BatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/batch-update.component').then(m => m.BatchUpdateComponent),
    resolve: {
      batch: BatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/batch-update.component').then(m => m.BatchUpdateComponent),
    resolve: {
      batch: BatchResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default batchRoute;
