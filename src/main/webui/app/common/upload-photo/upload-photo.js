'use strict';

export default class UploadPhotoController {
  /* @ngInject */
  constructor($modalInstance, $rootScope, Upload, $timeout) {
    this.modalInstance = $modalInstance;
    this.rootScope = $rootScope;
    this.upload = Upload;
    this.timeout = $timeout;


  }
  uploadFiles(file, errFiles) {
    var self = this;
    this.rootScope.f = file;
    this.rootScope.errFile = errFiles && errFiles[0];
    if (file) {
      file.upload = this.upload.upload({
        url: 'https://angular-file-upload-cors-srv.appspot.com/upload',
        data: { file: file }
      });

      file.upload.then(function (response) {
        self.timeout(function () {
          self.rootScope.percent = { width: file.progress + '%' };
          file.result = response.data;
        });
      }, function (response) {
        if (response.status > 0)
          this.rootScope.errorMsg = response.status + ': ' + response.data;
      }, function (evt) {
        file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
      });
    }
  }

  closeModal() {
    this.modalInstance.dismiss();
    this.rootScope.f = null;
  }
}