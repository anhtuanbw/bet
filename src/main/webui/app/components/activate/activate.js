'use strict';

export default class ActivateController {
  constructor($location, $timeout, AccountService) {
    this.location = $location;
    this.accountService = AccountService;
    this.success = false;
    this.show = false;
    this.statusCSS = 'alert alert-danger';
    this.timeout = $timeout;
     this.location = $location;
    this.activate();
     
  }
   
   activate() {
     var key = this.location.search().key;
     var self = this;
     if (key) {
       this.accountService.activate(key)
       .then(() => {
         this.success = true;
         this.message = '';
         this.statusCSS = 'alert alert-success';
         this.show=true;
       })
       .catch(error => {
         this.success = false;
         this.message = error.data.message;
         this.statusCSS = 'alert alert-danger';
         this.show=true;
       });
     }
      this.timeout(function() {
      self.location.path('/home');
      }, 5000);
   }
}


