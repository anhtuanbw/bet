'use strict';
/* global angular */

import ChangePassword from './change-password/change-password';

export const componentsModule = angular.module('ngaythobet.components', [
  'ui.bootstrap.tpls',
  'ui.bootstrap.modal',
  'ui.bootstrap.dropdown'
])

.directive('changePassword', () => new ChangePassword());