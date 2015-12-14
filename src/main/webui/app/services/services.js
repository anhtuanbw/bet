'use strict';
/* global angular */

import CacheService from './cache.service';
import AccountService from './account.service';

export const services = angular
    .module('ngaythobet.services', [])
    .service('CacheService', CacheService)
    .service('AccountService', AccountService);
