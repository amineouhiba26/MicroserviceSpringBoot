import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ClientService, Client } from '../../services/client.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './clients.component.html',
  styleUrl: './clients.component.scss'
})
export class ClientsComponent implements OnInit {
  clients: Client[] = [];
  loading = true;
  error = '';
  showModal = false;
  editMode = false;
  currentClient: Client = { nom: '', prenom: '', email: '' };

  constructor(
    private readonly clientService: ClientService,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.loading = true;
    this.clientService.getClients().subscribe({
      next: (clients) => {
        this.clients = clients;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des clients';
        this.loading = false;
      }
    });
  }

  openCreateModal(): void {
    this.editMode = false;
    this.currentClient = { nom: '', prenom: '', email: '' };
    this.showModal = true;
  }

  openEditModal(client: Client): void {
    this.editMode = true;
    this.currentClient = { ...client };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.currentClient = { nom: '', prenom: '', email: '' };
  }

  saveClient(): void {
    if (this.editMode && this.currentClient.id) {
      this.clientService.updateClient(this.currentClient.id, this.currentClient).subscribe({
        next: () => {
          this.loadClients();
          this.closeModal();
        },
        error: () => this.error = 'Erreur lors de la mise à jour'
      });
    } else {
      this.clientService.createClient(this.currentClient).subscribe({
        next: () => {
          this.loadClients();
          this.closeModal();
        },
        error: () => this.error = 'Erreur lors de la création'
      });
    }
  }

  deleteClient(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce client?')) {
      this.clientService.deleteClient(id).subscribe({
        next: () => this.loadClients(),
        error: () => this.error = 'Erreur lors de la suppression'
      });
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
