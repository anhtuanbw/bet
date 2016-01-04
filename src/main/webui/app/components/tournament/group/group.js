'use strict';

export default class TournamentGroupController {
  /* @ngInject */
  constructor($modal, $rootScope, GroupService, AccountService) {
    this.modal = $modal;
    this.rootScope = $rootScope;
    this.groupService = GroupService;
    this.accountService = AccountService;
    this.groupInfo = {};
    this.tournamentName = '';
    this.isMod = false;
    this.authen();
    this.rootScope.$on('selectGroup', (event, groupInfo) => {
      if (groupInfo) {
        if(groupInfo.tournamentName) {
          this.tournamentName = groupInfo.tournamentName;
        }
        this.groupInfo = groupInfo;
        this.findById();
        this.checkMod();
      }
    });
  }
  
  findById() {
    this.groupService.findById(this.groupInfo.id)
    .then(response => {
      this.groupInfo = response.data;
    })
    .catch();
  }
  
  authen() {
    this.accountService.authen()
    .then(response => {
      if (response.data) {
         this.currentUser = response.data;
      }
    });
  }
  
  checkMod() {
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
  
   openUpdateGroup() {
    var self = this;
    this.modal.open({
      templateUrl: 'app/common/update-group/update-group.html',
      controller: 'UpdateGroupController',
      controllerAs: 'updateGroup',
      resolve: {
        groupInfo: function () {
          return self.groupInfo;
        }
      }
    });
  }
}