'use strict';

export default class CommentController {
  /* @ngInject */
  constructor() {}
}

export default class Comment {
  constructor() {
    return {
      replace: false,
      scope: true,
      controller: CommentController,
      controllerAs: 'comment',
      templateUrl: 'app/common/comment/comment.html'
    };
  }
}