'use strict';
/* global angular */

import {service} from './services/services'; // jshint ignore:line
import {common} from './common/common'; // jshint ignore:line
import HomeController from './components/home/home';
import ResetPasswordController from './common/reset-password/reset-password';
import ChangePasswordController from './common/change-password/change-password';
import ActivatorController from './components/activate/activate';
import TournamentController from './components/tournament/tournament';
import RegisterController from './components/register/register';

export default class AppController {
  /* @ngInject */
  constructor($router) {
    $router.config([
      { path: '/home', component: 'home' },
      { path: '/api/activate', component: 'activate' },
      { path: '/tournament', component: 'tournament' },      
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
.controller('TournamentController', TournamentController)
.controller('RegisterController', RegisterController)
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