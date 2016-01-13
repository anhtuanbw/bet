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
import UpdateGroupController from './common/update-group/update-group';
import CreateBettingController from './common/create-betting-match/create-betting-match';
import UnAuthorizedController from './components/unAuthorized/unAuthorized';

angular.module('ngaythobet', [
  'ui.router',
  'mgcrea.ngStrap',
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
.controller('UpdateGroupController', UpdateGroupController)
.controller('PlayerBettingMatchController', PlayerBettingMatchController)
.controller('CreateBettingController', CreateBettingController)
.controller('UnAuthorizedController', UnAuthorizedController)
.config(/* @ngInject */($compileProvider, $translateProvider, $stateProvider, $urlRouterProvider) => {
  // disables AngularJS debug info
   $compileProvider.debugInfoEnabled(false);
   
  // Angular Translate
  $translateProvider
      .useSanitizeValueStrategy('sanitize')
      .useMissingTranslationHandlerLog()
      .useStaticFilesLoader({ prefix: 'i18n/', suffix: '.json' })
      .preferredLanguage('en_US');
      
  $urlRouterProvider.otherwise('/home');
  $stateProvider
    .state('home', {
      url: '/home',
      templateUrl: 'app/components/home/home.html',
      controller: HomeController,
      controllerAs: 'home'
    })
    .state('unauthorized', {
      url: '/unauthorized',
      templateUrl: 'app/components/unAuthorized/unAuthorized.html',
      controller: UnAuthorizedController,
      controllerAs: 'unauthorized'
    })
    .state('activate', {
      url: '/api/activate',
      templateUrl: 'app/components/activate/activate.html',
      controller: ActivatorController,
      controllerAs: 'activator'
    })
    .state('management', {
      abstract: true,
      url: '/management',
      templateUrl: 'app/components/management/management.html',
      controller: ManagementController,
      controllerAs: 'management',
      redirectTo: 'management.home',
    })
    .state('management.home', {
      url: '',
      templateUrl: 'app/components/home/rules.html'
    })
    .state('management.createtournament', {
      url: '/create-tournament',
      templateUrl: 'app/components/tournament/create-tournament/create-tournament.html',
      controller: CreateTournamentController,
      controllerAs: 'creator'
    })
    .state('management.tournament', {
      url: '/:tournamentId',
      templateUrl: 'app/components/tournament/edit-tournament/edit-tournament.html',
      controller: EditTournamentController,
      controllerAs: 'editor'
    })
    .state('management.group', {
      url: '/:tournamentId/:groupId',
      templateUrl: 'app/components/tournament/group/group.html',
      controller: TournamentGroupController,
      controllerAs: 'tourGroup'
    })
    .state('management.match', {
      url: '/:tournamentId/:groupId/:matchId',
      templateUrl: 'app/components/tournament/player-betting-match/player-betting-match.html',
      controller: PlayerBettingMatchController,
      controllerAs: 'player'
    });
});
