'use strict';
/* global angular */

import {service} from './services/services'; // jshint ignore:line
import {common} from './common/common'; // jshint ignore:line
import {components} from './components/components'; // jshint ignore:line
import HomeController from './components/home/home';

export default class AppController {
  /* @ngInject */
  constructor($router) {
    $router.config([
      { path: '/home', component: 'home' },
      { path: '/', redirectTo: '/home' }
    ]);
  }
}

angular.module('ngaythobet', [
  'ngNewRouter',
  'ngSanitize',
  'ngCookies',
  'pascalprecht.translate',
  'ngaythobet.services',
  'ngaythobet.common',
  'ngaythobet.components'
])
.controller('AppController', AppController)
.controller('HomeController', HomeController)
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