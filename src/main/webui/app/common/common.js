'use strict';
/* global angular */

import Account from './account/account';
import NavBar from './navbar/navbar';
import LanguageSelect from './language-select/language-select';
import Login from './login/login';
import Register from './register/register';
import DateTimePicker from './match/create-match/date-time-picker';
import SideBar from './sidebar/sidebar';
import PlayerHistory from './player-history/player-history';
import GroupHistory from './group-history/group-history';
import Comment from './comment/comment';
import InfoBettingMatch from './info-betting-match/info-betting-match';
import Betting from './betting-match-management/betting-match-management';
import CreateBet from './create-betting-match/create-betting-match';
import PlayerStatistics from './player-statistics/player-statistics';
import MenuLink from './menu/menu-link/menu-link';
import MenuToggle from './menu/menu-toggle/menu-toggle';
import RoundEmpty from './match/create-match/round-empty';

export const commonModule = angular.module('ngaythobet.common', [
  'ui.bootstrap.tpls',
  'ui.bootstrap.modal',
  'ui.bootstrap.dropdown'
])
.directive('account', () => new Account())
.directive('navbar', () => new NavBar())
.directive('languageSelect', () => new LanguageSelect())
.directive('login', () => new Login())
.directive('register', () => new Register())
.directive('sidebar', () => new SideBar())
.directive('playerHistory', () => new PlayerHistory())
.directive('groupHistory', () => new GroupHistory())
.directive('comment', () => new Comment())
.directive('infoBettingMatch', () => new InfoBettingMatch())
.directive('dateTimePicker', DateTimePicker)
.directive('betting', () => new Betting())
.directive('createBet', () => new CreateBet())
.directive('menuLink', () => new MenuLink())
.directive('menuToggle', () => new MenuToggle())
.directive('playerStatistics', () => new PlayerStatistics())
.directive('roundEmpty', () => new RoundEmpty());
