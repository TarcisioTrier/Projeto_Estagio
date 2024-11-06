import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root',
})
export class ApiInterceptor implements HttpInterceptor {
  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (req.url.includes('/gtins')) {
      req = req.clone({
        url: `https://api.cosmos.bluesoft.com.br${req.url}`,
        setHeaders: {
          'Content-Type': 'application/json',
          'X-Cosmos-Token': '5sR0AZm9kguOH2Z0IDv3pQ',
        },
      });
    } else if (!(req.url.startsWith('https://') || req.url.includes('/cnpj'))) {
      req = req.clone({ url: `http://localhost:8080/${req.url}` });
    }

    return next.handle(req);
  }
}
