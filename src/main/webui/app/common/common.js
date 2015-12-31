'use strict';
/* global angular */

import Account from './account/account';
import NavBar from './navbar/navbar';
import LanguageSelect from './language-select/language-select';
import Login from './login/login';
import Register from './register/register';
import DateTimePicker from './match/create-match/date-time-picker';
import SideBar from './sidebar/sidebar';
import Unauthorized from './unAuthorized/unAuthorized';
import PlayerHistory from './player-history/player-history';
import Comment from './comment/comment';
import betting from './betting-match-management/betting-match-management';
import createBet from './create-betting-match/create-betting-match';

export const commonModule = angular.module('ngaythobet.common', [
  'ui.bootstrap.tpls',
  'ui.bootstrap.modal',
  'ui.bootstrap.dropdown'
])
.directive('account', () => new Account())
.directive('navbar', () => new NavBar())
.directive('languageSelect', () => new LanguageSelect())
.directive('login', () => new Login())
.directive('register', () => new Register())
.directive('sidebar', () => new SideBar())
.directive('unauthorized', () => new Unauthorized())
.directive('playerhistory', () => new PlayerHistory())
.directive('comment', () => new Comment())
.directive('dateTimePicker', DateTimePicker)
.directive('betting', () => new betting())
.directive('createBet', () => new createBet());
