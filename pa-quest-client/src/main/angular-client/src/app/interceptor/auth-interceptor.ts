import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenStorageService } from '../service/token-storage.service';
import { tap } from 'rxjs/operators';
import { InfoBarComponent } from '../component/info-bar/info-bar.component';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable({
    providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

    constructor(private token: TokenStorageService, private snackBar: InfoBarComponent) {}

    intercept(req: HttpRequest<any>, next: HttpHandler) {
        let authRequest = req;
        const token = this.token.getToken();
        if (token != null) {
            authRequest = req.clone({ headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token)});
        }
        return next.handle(authRequest)
            .pipe(
                tap(
                    event => {},
                    error => {
                        let errorDescription;
                        if (error.status === 401) {
                            errorDescription = ' Ошибка ' + error.status + ' ' + error.error.message;
                        } else {
                            errorDescription = ' Ошибка ' + error.status + ' ' + error.statusText;
                            if (error.error.response_message) {
                                errorDescription = errorDescription + ': ' + error.error.response_message;
                            }
                        }
                        this.snackBar.openSnackBar(errorDescription, 'Close', 'error-snackbar');
                    }
                )
            );
    }

}

export const httpInterceptorProviders = [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
];