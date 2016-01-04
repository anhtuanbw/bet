'use strict';
/* global angular */

import CacheService from './cache.service';
import AccountService from './account.service';
import MatchService from './match.service';
import TournamentService from './tournament.service';
import RoundService from './round.service';
import GroupService from './group.service';
import UserService from './user.service';
import BettingService from './betting.service';

export const services = angular
    .module('ngaythobet.services', [])
    .service('CacheService', CacheService)
    .service('AccountService', AccountService)
    .service('MatchService', MatchService)
    .service('TournamentService', TournamentService)
    .service('RoundService', RoundService)
    .service('GroupService', GroupService)
    .service('UserService', UserService)
    .service('BettingService', BettingService);
