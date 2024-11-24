import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'warehouse',
    data: { pageTitle: 'Warehouses' },
    loadChildren: () => import('./warehouse/warehouse.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'Products' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'supplier',
    data: { pageTitle: 'Suppliers' },
    loadChildren: () => import('./supplier/supplier.routes'),
  },
  {
    path: 'batch',
    data: { pageTitle: 'Batches' },
    loadChildren: () => import('./batch/batch.routes'),
  },
  {
    path: 'supplier-contact',
    data: { pageTitle: 'SupplierContacts' },
    loadChildren: () => import('./supplier-contact/supplier-contact.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
