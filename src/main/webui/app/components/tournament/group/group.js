'use strict';

export default class TournamentGroupController {
  /* @ngInject */
  constructor($modal, $rootScope, $location, GroupService, AccountService, $stateParams, TournamentService) {
    this.modal = $modal;
    this.rootScope = $rootScope;
    this.location = $location;
    this.groupService = GroupService;
    this.accountService = AccountService;
    this.tournamentService = TournamentService;
    this.groupInfo = {};
    this.tournamentName = '';
    this.tournamentId = $stateParams.tournamentId;
    this.isMod = false;
    this.groupInfo.id = $stateParams.groupId;
    this.getTournamentById(this.tournamentId);
    this.findById();
    this.checkMod();
    this.activePlayer = 'group';
    this.lastUrl = this.location.path();
    $rootScope.$on('updateGroup', () => {
      this.findById();
    });
  }
  
  getTournamentById(tournamentId) {
    this.tournamentService.getById(tournamentId)
    .then(response => {
      this.tournamentName = response.data.name;
    })
    .catch(error => {
      if (error.status === 401) {
        this.location.path('/unauthorized').search({ lastUrl: this.lastUrl });
      }
    });
  }
  
  findById() {
    this.groupService.findById(this.groupInfo.id)
    .then(response => {
      this.groupInfo = response.data;
    })
    .catch(error => {
      if (error.status === 401) {
        this.location.path('/unauthorized').search({ lastUrl: this.lastUrl }); 
      }
    });
  }

  checkMod() {
    this.accountService.authen()
    .then(response => {
      if (response.data) {
        this.currentUser = response.data;
        var data = {};
        data.groupId = this.groupInfo.id;
        data.userId = this.currentUser.id;
        this.groupService.isModerator(data)
        .then(() => {
          this.isMod = true;
        })
        .catch(() => {
          this.isMod = false;
        });
      }
    });
  }

  playerStatistic() {
    this.rootScope.$broadcast('playerStatistic', this.groupInfo.id);
  }

  openUpdateGroup() {
    var self = this;
    this.modal.open({
      templateUrl: 'app/common/update-group/update-group.html',
      controller: 'UpdateGroupController',
      controllerAs: 'updateGroup',
      resolve: {
        groupInfo: function() {
          return self.groupInfo;
        }
      }
    });
  }
}