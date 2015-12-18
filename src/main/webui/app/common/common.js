'use strict';
/* global angular */

import Account from './account/account';
import ResetPassword from './reset-password/reset-password';
import NavBar from './navbar/navbar';
import Login from './login/login';
import LanguageSelect from './language-select/language-select';

export const commonModule = angular.module('ngaythobet.common', [
  'ui.bootstrap.tpls',
  'ui.bootstrap.modal',
  'ui.bootstrap.dropdown'
])
.directive('account', () => new Account())
.directive('resetpassword', () => new ResetPassword())
.directive('navbar', () => new NavBar())
.directive('login', () => new Login())
.directive('languageSelect', () => new LanguageSelect());