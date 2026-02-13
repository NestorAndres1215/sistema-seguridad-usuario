import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { GoogleService } from '../services/google.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(GoogleService);
  const token = authService.token;

  console.log('🔍 Interceptor activo. Token encontrado:', token);

  if (token) {
    const cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    console.log('✅ Token agregado al header');
    return next(cloned);
  } else {
    console.warn('⚠️ No se encontró token');
  }

  return next(req);
};
