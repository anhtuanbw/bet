'use strict';

export default class UpdateGroupController {
  /* @ngInject */
  constructor($modalInstance, toaster, groupInfo, $rootScope, $location, UserService, GroupService) {
    this.rootScope = $rootScope;
    this.location = $location;
    this.userService = UserService;
    this.groupService = GroupService;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;
    this.errorMessage = {};
	  this.groupInfo = groupInfo;
    this.players = [];
  }

  closeModal() {
    this.modalInstance.dismiss();
  }
  
  loadUsers(name) {
	  return this.userService.users(name);
  }
  
  getData() {
    var data ={};
    data.groupId = this.groupInfo.id;
    data.memberIds = [];
    for(var i in this.players) {
      data.memberIds.push(this.players[i].id);
    }
    return data;
  }
  
  updateGroup() {
    this.groupService.addMember(this.getData())
    .then(() => {
      this.rootScope.$broadcast('selectGroup', this.groupInfo);
      this.toaster.pop('success', null, 'app/common/update-group/success.html', null, 'template');
      this.players = [];
      this.closeModal();
    })
    .catch(error => {
      if (error.status === 401) {
        this.location.path('/unauthorized');
      }
      if (error.status === 400) {
        if (!error.data.message) {
          this.errorMessage = error.data.fieldErrors;
        }
        else {
          this.errorMessage.memberIds = error.data.message;
        }
      }
    });
  }
}
