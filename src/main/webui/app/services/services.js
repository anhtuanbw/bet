'use strict';
/* global angular */

import CacheService from './cache.service';
import AccountService from './account.service';
import RegisterService from './register.service';

export const services = angular
    .module('ngaythobet.services', [])
    .service('CacheService', CacheService)
    .service('AccountService', AccountService)
    .service('RegisterService', RegisterService);
