import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ChatMessage {
  role: 'user' | 'assistant';
  content: string;
  timestamp: Date;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private readonly apiUrl = 'http://localhost:8888/agent-ia-service';

  constructor(private readonly http: HttpClient) {}

  sendMessage(message: string): Observable<string> {
    return this.http.get(`${this.apiUrl}/chat`, {
      params: { message },
      responseType: 'text'
    });
  }
}
