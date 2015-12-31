'use strict';
/* global angular */

import {service} from './services/services'; // jshint ignore:line
import {common} from './common/common'; // jshint ignore:line
import HomeController from './components/home/home';
import UpdateScoreController from './common/match/update-score/update-score';
import PlayerBettingMatchController from './components/tournament/player-betting-match/player-betting-match';
import ResetPasswordController from './common/reset-password/reset-password';
import ResetPasswordFinishController from './components/reset-passwordFinish/reset-passwordFinish';
import ChangePasswordController from './common/change-password/change-password';
import RoundManController from './common/round-management/round-management';
import GroupController from './common/group/group';
import ActivatorController from './components/activate/activate';
import CreateTournamentController from './components/tournament/create-tournament/create-tournament';
import EditTournamentController from './components/tournament/edit-tournament/edit-tournament';
import ManagementController from './components/management/management';
import CreateMatchController from './common/match/create-match/create-match';
import TournamentGroupController from './components/tournament/group/group';
import ResetPasswordSuccessController from './common/reset-passwordSuccess/reset-passwordSuccess';

export default class AppController {
  /* @ngInject */
  constructor($router) {
    $router.config([
      { path: '/home', component: 'home' },
      { path: '/api/reset-password/finish', component: 'reset-passwordFinish' },
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
  'ngAnimate',
  'ngMaterial'
])
.controller('AppController', AppController)
.controller('HomeController', HomeController)
.controller('UpdateScoreController', UpdateScoreController)
.controller('ResetPasswordController', ResetPasswordController)
.controller('ResetPasswordFinishController', ResetPasswordFinishController)
.controller('ChangePasswordController', ChangePasswordController)
.controller('ActivatorController', ActivatorController)
.controller('CreateTournamentController', CreateTournamentController)
.controller('EditTournamentController', EditTournamentController)
.controller('ManagementController', ManagementController)
.controller('RoundManController', RoundManController)
.controller('GroupController', GroupController)
.controller('CreateMatchController', CreateMatchController)
.controller('TournamentGroupController', TournamentGroupController)
.controller('ResetPasswordSuccessController', ResetPasswordSuccessController)
.controller('PlayerBettingMatchController', PlayerBettingMatchController)
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
