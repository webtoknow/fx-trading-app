import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';
import { BlotterViewComponent } from './blotter-view/blotter-view.component';
import { FxRatesViewComponent } from './fx-rates-view/fx-rates-view.component';

@Component({
  selector: 'app-dashboard-page',
  imports: [BlotterViewComponent, FxRatesViewComponent],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.css'
})
export class DashboardPageComponent {
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
}
