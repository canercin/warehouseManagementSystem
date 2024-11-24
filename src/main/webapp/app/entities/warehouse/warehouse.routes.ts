import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import WarehouseResolve from './route/warehouse-routing-resolve.service';

const warehouseRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/warehouse.component').then(m => m.WarehouseComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/warehouse-detail.component').then(m => m.WarehouseDetailComponent),
    resolve: {
      warehouse: WarehouseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/warehouse-update.component').then(m => m.WarehouseUpdateComponent),
    resolve: {
      warehouse: WarehouseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/warehouse-update.component').then(m => m.WarehouseUpdateComponent),
    resolve: {
      warehouse: WarehouseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default warehouseRoute;
