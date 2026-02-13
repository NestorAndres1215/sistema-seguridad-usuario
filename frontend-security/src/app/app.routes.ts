import { Routes } from '@angular/router';
import { Principal } from './features/auth/principal/principal';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { AuthCallback } from './features/auth/auth-callback/auth-callback';
import { Dashboard } from './features/user/dashboard/dashboard';
import { AdminGuard } from './core/guards/admin.guard';
import { UserGuard } from './core/guards/user.guard';
import { NoAuthGuard } from './core/guards/noauth.guard';
import { HomeUser } from './features/user/home-user/home-user';
import { User } from './features/user/user/user';
import { HomeAdmin } from './features/admin/home-admin/home-admin';
import { DashboardAdmin } from './features/admin/dashboard-admin/dashboard-admin';
import { UserAdmin } from './features/admin/user-admin/user-admin';
import { ConfiguracionAdmin } from './features/admin/configuracion-admin/configuracion-admin';
import { UserPerfil } from './features/admin/user-perfil/user-perfil';
import { Error } from './shared/error/error';


export const routes: Routes = [

    // Página principal pública
    { path: '', component: Principal, pathMatch: 'full' },

    // Login y registro → solo accesibles si NO está logueado
    { path: 'login', component: Login, canActivate: [NoAuthGuard] },
    { path: 'register', component: Register, canActivate: [NoAuthGuard] },

    // Callback libre (por ejemplo para Google Auth)
    { path: 'auth-callback', component: AuthCallback },

    {
        path: '',
        component: HomeAdmin,
        canActivate: [AdminGuard],
        children: [
            { path: 'dashboard-admin', component: DashboardAdmin },
            { path: 'perfil-admin', component: UserPerfil },
            { path: 'user-admin', component: UserAdmin },
            { path: 'configuracion-admin', component: ConfiguracionAdmin },
        ]

    },
    {
        path: '',
        component: HomeUser,
        canActivate: [UserGuard],
        children: [
            { path: 'dashboard', component: Dashboard },
            { path: 'user', component: User }
        ]
    },
    // Cualquier otra ruta redirige a la principal
    { path: '**', component:Error }
];
