'use strict';

export default class PlayerBettingMatchController {
  /* @ngInject */
  constructor($rootScope, CacheService) {
    this.cacheService = CacheService;
	this.comment = '';
    this.stompClient = null;
	this.connect();
	}

  connect() {
	var token = this.cacheService.get('loginUser');
	var socket = new SockJS('/betting-match');
	this.stompClient = Stomp.over(socket);

	var self = this;
	this.stompClient.connect({}, function (frame) {
	  self.stompClient.subscribe('/topic/comment/1', function (comment) {
		  
	  });
	});
  }

  disconnect() {
	var self = this;
	if (self.stompClient != null) {
		self.stompClient.disconnect();
	}
  }

  sendComment() {
	var self = this;
	var token = this.cacheService.get('loginUser');
	var requestBody = {
		'bettingMatchId': 10,
		'comment': '12312'
	}
	self.stompClient.send('/app/betting-match/comment/1', { 'x-auth-token': token }, JSON.stringify(requestBody));
  }
}