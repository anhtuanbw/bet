'use strict';
/* global angular */

import Account from './account/account';
import NavBar from './navbar/navbar';
import LanguageSelect from './language-select/language-select';
import ChangePassword from './change-password/change-password';

export const commonModule = angular.module('ngaythobet.common', [
  'ui.bootstrap.tpls',
  'ui.bootstrap.modal',
  'ui.bootstrap.dropdown'
])
.directive('account', () => new Account())
.directive('navbar', () => new NavBar())
.directive('languageSelect', () => new LanguageSelect())
.directive('changePassword', () => new ChangePassword());