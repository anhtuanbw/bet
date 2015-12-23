'use strict';
/* global angular */

import {service} from './services/services'; // jshint ignore:line
import {common} from './common/common'; // jshint ignore:line
import HomeController from './components/home/home';
import ResetPasswordController from './common/reset-password/reset-password';
import ChangePasswordController from './common/change-password/change-password';
import ActivatorController from './components/activate/activate';
import CreateTournamentController from './components/tournament/create-tournament/create-tournament';
import EditTournamentController from './components/tournament/edit-tournament/edit-tournament';
import ManagementController from './components/management/management';
import CreateMatchController from './common/createMatch/createMatch';

export default class AppController {
  /* @ngInject */
  constructor($router) {
    $router.config([
      { path: '/home', component: 'home' },
      { path: '/api/activate', component: 'activate' },
      { path: '/management', component: 'management' },      
      { path: '/', redirectTo: '/home' }
    ]);
  }
}

angular.module('ngaythobet', [
  'ngNewRouter',
  'ngSanitize',
  'ngCookies',
  'ngTagsInput',
  'pascalprecht.translate',
  'ngaythobet.services',
  'ngaythobet.common',
  'toaster',
  'ngAnimate'
])
.controller('AppController', AppController)
.controller('HomeController', HomeController)
.controller('ResetPasswordController', ResetPasswordController)
.controller('ChangePasswordController', ChangePasswordController)
.controller('ActivatorController', ActivatorController)
.controller('CreateTournamentController', CreateTournamentController)
.controller('EditTournamentController', EditTournamentController)
.controller('ManagementController', ManagementController)
.controller('CreateMatchController', CreateMatchController)
.config(/* @ngInject */($compileProvider, $componentLoaderProvider, $translateProvider) => {
  // disables AngularJS debug info
  $compileProvider.debugInfoEnabled(false);

  // set templates path
  $componentLoaderProvider.setTemplateMapping(name => `app/components/${name}/${name}.html`);

  // Angular Translate
  $translateProvider
      .useSanitizeValueStrategy('sanitize')
      .useMissingTranslationHandlerLog()
      .useStaticFilesLoader({ prefix: 'i18n/', suffix: '.json' })
      .preferredLanguage('en_US');
});