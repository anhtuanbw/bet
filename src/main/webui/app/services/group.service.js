'use strict';

export default class GroupService {
  /* @ngInject */
  constructor($http) {
    this.$http = $http;
  }

  create(data) {
    return this.$http.post('api/group/create', data);
  }

}