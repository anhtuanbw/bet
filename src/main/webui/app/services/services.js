'use strict';
/* global angular */

import CacheService from './cache.service';
import AccountService from './account.service';
import TournamentService from './tournament.service';
import RoundService from './round.service';
import GroupService from './group.service';

export const services = angular
    .module('ngaythobet.services', [])
    .service('CacheService', CacheService)
    .service('AccountService', AccountService)
    .service('TournamentService', TournamentService)
    .service('RoundService', RoundService)
    .service('GroupService', GroupService);
