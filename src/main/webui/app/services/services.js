'use strict';
/* global angular */

import CacheService from './cache.service';
import AccountService from './account.service';
import MatchService from './match.service';
import RegisterService from './register.service';

export const services = angular
    .module('ngaythobet.services', [])
    .service('CacheService', CacheService)
    .service('AccountService', AccountService)
    .service('MatchService', MatchService)
    .service('RegisterService', RegisterService);
