import { Component, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ChatService, ChatMessage } from '../../services/chat.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss'
})
export class ChatComponent implements AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;
  
  messages: ChatMessage[] = [];
  newMessage = '';
  loading = false;
  sidebarOpen = true;

  quickActions = [
    { label: 'List Products', message: 'Liste tous les produits disponibles' },
    { label: 'List Clients', message: 'Liste tous les clients' },
    { label: 'Search Product', message: 'Donne moi le produit avec ID 1' },
    { label: 'Summary', message: 'Donne moi un résumé des produits et clients' }
  ];

  constructor(
    private readonly chatService: ChatService,
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  private scrollToBottom(): void {
    try {
      this.messagesContainer.nativeElement.scrollTop = this.messagesContainer.nativeElement.scrollHeight;
    } catch (err) {}
  }

  sendMessage(): void {
    if (!this.newMessage.trim() || this.loading) return;

    const userMessage: ChatMessage = {
      role: 'user',
      content: this.newMessage,
      timestamp: new Date()
    };
    this.messages.push(userMessage);

    const messageToSend = this.newMessage;
    this.newMessage = '';
    this.loading = true;

    this.chatService.sendMessage(messageToSend).subscribe({
      next: (response) => {
        const assistantMessage: ChatMessage = {
          role: 'assistant',
          content: response,
          timestamp: new Date()
        };
        this.messages.push(assistantMessage);
        this.loading = false;
      },
      error: (err) => {
        const errorMessage: ChatMessage = {
          role: 'assistant',
          content: 'Erreur lors de la communication avec l\'agent. Veuillez réessayer.',
          timestamp: new Date()
        };
        this.messages.push(errorMessage);
        this.loading = false;
      }
    });
  }

  sendQuickAction(message: string): void {
    this.newMessage = message;
    this.sendMessage();
  }

  clearChat(): void {
    this.messages = [];
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
  }
}
