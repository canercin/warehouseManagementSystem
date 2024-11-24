import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SupplierContactResolve from './route/supplier-contact-routing-resolve.service';

const supplierContactRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/supplier-contact.component').then(m => m.SupplierContactComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/supplier-contact-detail.component').then(m => m.SupplierContactDetailComponent),
    resolve: {
      supplierContact: SupplierContactResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/supplier-contact-update.component').then(m => m.SupplierContactUpdateComponent),
    resolve: {
      supplierContact: SupplierContactResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/supplier-contact-update.component').then(m => m.SupplierContactUpdateComponent),
    resolve: {
      supplierContact: SupplierContactResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default supplierContactRoute;
