'use strict';
/* global angular */

import CacheService from './cache.service';
import AccountService from './account.service';
import RegisterService from './register.service';
import RoundService from './round.service';
import GroupService from './group.service';
import TournamentService from './tournament.service';

export const services = angular
    .module('ngaythobet.services', [])
    .service('CacheService', CacheService)
    .service('AccountService', AccountService)
    .service('RegisterService', RegisterService)
    .service('RoundService', RoundService)
    .service('GroupService', GroupService);
