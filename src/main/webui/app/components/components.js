'use strict';
/* global angular */

import Register from './register/register';
import compareTo from './register/compareTo';

export const componentsModule = angular.module('ngaythobet.components', [
  'ui.bootstrap.tpls',
  'ui.bootstrap.modal',
  'ui.bootstrap.dropdown'
])
.directive('register', () => new Register())
.directive('compareTo', compareTo);