'use strict';
/* global angular */

import CacheService from './cache.service';
import AccountService from './account.service';
import TournamentService from './tournament.service';

export const services = angular
    .module('ngaythobet.services', [])
    .service('CacheService', CacheService)
    .service('AccountService', AccountService)
    .service('TournamentService', TournamentService);
