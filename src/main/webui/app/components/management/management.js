'use strict';

export default class ManagementController {
  /* @ngInject */
  constructor(TournamentService, CacheService, $rootScope, AccountService, MenuService, $location, $mdSidenav, $timeout) {
    this.rootScope = $rootScope;
    this.location = $location;
    this.tournamentService = TournamentService;
    this.accountService = AccountService;
    this.tournaments = [];
    this.getAllTournament();
    this.selectedGroup = -1;
    this.showView = {
      isBetting: false,
      isCreate: true,
      isEdit: false,
      isGroup: false
    };
    this.cacheService = CacheService;
    this.isAdmin = false;
    this.authen();
    $rootScope.$on('addTournament', () => {
      this.getAllTournament();
    });

    this.menu = MenuService;
    this.autoFocusContent = false;
    this.status = {
        isFirstOpen: true,
        isFirstDisabled: false
    };

    this.$mdSidenav = $mdSidenav;
    this.$timeout = $timeout;
    this.toggleLeft = this.buildDelayedToggler('left');
    this.lastUrl = this.location.path();
  }

  debounce(func, wait) {
      var timer;
      var self = this;
      return function debounced() {
        var context = this,
            args = Array.prototype.slice.call(arguments);
        self.$timeout.cancel(timer);
        timer = self.$timeout(function() {
          timer = undefined;
          func.apply(context, args);
        }, wait || 10);
      };
  }

  buildDelayedToggler(navID) {
      return this.debounce(function() {
        this.$mdSidenav(navID)
          .toggle()
          .then(function () {
          });
      }, 200);
  }

  isOpen(section) {
    return this.menu.isSectionSelected(section);
  }

  toggleOpen(section) {
    this.menu.toggleSelectSection(section);
  }

  isSectionSelected(section) {
    var selected = false;
    var openedSection = this.menu.openedSection;
    if(openedSection === section){
      selected = true;
    }
    else if(section.children) {
      section.children.forEach(function(childSection) {
        if(childSection === openedSection){
          selected = true;
        }
      });
    }
    return selected;
  }

  focusSection() {
    this.autoFocusContent = true;
  }

  createTournament() {
    this.location.path('/management/create-tournament');
  }

  showGroup(index, tournament, group) {
    this.selectedGroup = index;
    this.location.path('/management/' + tournament.id + '/' + group.id);
  }
  
  isAuthorized() {
    return this.cacheService.get('loginUser')!= null;
  }

  getAllTournament() {
    this.tournamentService.findByRole()
    .then(response => {
      this.tournaments = response.data;
    })
    .catch(error => {
      if (error.status === 401) {
        this.location.path('/unauthorized').search({ lastUrl: this.lastUrl });
      }
    });
  }

  showTournamenDetail(tournamentId) {
    this.location.path('/management/' + tournamentId);
  }

  authen() {
    this.accountService.authen()
    .then(response => {
      if (response.data) {
        this.isAdmin = response.data.role === 'ADMIN' ? true : false;
      }
    });
  }
}
