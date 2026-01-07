import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ProductService, Product } from '../../services/product.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss'
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  loading = true;
  error = '';
  showModal = false;
  editMode = false;
  currentProduct: Product = { nom: '', prix: 0 };
  
  // Role-based access control (mirrors backend ChatController logic)
  isAdmin = false;

  constructor(
    private readonly productService: ProductService,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    // Check admin status on init (mirrors backend isAdmin() check)
    this.isAdmin = this.authService.isAdmin();
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.productService.getProducts().subscribe({
      next: (products) => {
        this.products = products;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des produits';
        this.loading = false;
      }
    });
  }

  /**
   * Open create modal - ADMIN only
   * Mirrors backend Intent.CREATE_PRODUCT permission
   */
  openCreateModal(): void {
    if (!this.isAdmin) {
      this.error = '🚫 Accès refusé - Vous n\'avez pas les droits nécessaires pour créer un produit.';
      return;
    }
    this.editMode = false;
    this.currentProduct = { nom: '', prix: 0 };
    this.showModal = true;
  }

  /**
   * Open edit modal - ADMIN only
   * Mirrors backend Intent.UPDATE_PRODUCT permission
   */
  openEditModal(product: Product): void {
    if (!this.isAdmin) {
      this.error = '🚫 Accès refusé - Vous n\'avez pas les droits nécessaires pour modifier un produit.';
      return;
    }
    this.editMode = true;
    this.currentProduct = { ...product };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.currentProduct = { nom: '', prix: 0 };
  }

  /**
   * Save product (create or update) - ADMIN only
   * Mirrors backend Intent.CREATE_PRODUCT and Intent.UPDATE_PRODUCT permissions
   */
  saveProduct(): void {
    if (!this.isAdmin) {
      this.error = '🚫 Accès refusé - Vous n\'avez pas les droits nécessaires pour effectuer cette action.';
      return;
    }
    
    if (this.editMode && this.currentProduct.id) {
      this.productService.updateProduct(this.currentProduct.id, this.currentProduct).subscribe({
        next: () => {
          this.loadProducts();
          this.closeModal();
        },
        error: () => this.error = 'Erreur lors de la mise à jour'
      });
    } else {
      this.productService.createProduct(this.currentProduct).subscribe({
        next: () => {
          this.loadProducts();
          this.closeModal();
        },
        error: () => this.error = 'Erreur lors de la création'
      });
    }
  }

  /**
   * Delete product - ADMIN only
   * Mirrors backend Intent.DELETE_PRODUCT permission
   */
  deleteProduct(id: number): void {
    if (!this.isAdmin) {
      this.error = '🚫 Accès refusé - Vous n\'avez pas les droits nécessaires pour supprimer un produit.';
      return;
    }
    
    if (confirm('Êtes-vous sûr de vouloir supprimer ce produit?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => this.loadProducts(),
        error: () => this.error = 'Erreur lors de la suppression'
      });
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
