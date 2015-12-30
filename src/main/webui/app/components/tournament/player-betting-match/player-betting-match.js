'use strict';

export default class PlayerBettingMatchController {
	/* @ngInject */
	constructor($rootScope, CacheService) {
		this.cacheService = CacheService;
		this.comment = '';
		this.connect();
	}

	connect() {
		this.disconnect();
		var socket = new SockJS('/hello');
		stompClient = Stomp.over(socket);

		stompClient.connect({}, function (frame) {
			setConnected(true);
			console.log('Connected: ' + frame);
			stompClient.subscribe('/topic/greetings/1', function (greeting) {
				showGreeting(JSON.parse(greeting.body).content);
			});
		});
	}

	disconnect() {
		if (stompClient != null) {
			stompClient.disconnect();
		}
		setConnected(false);
		console.log("Disconnected");
	}

	sendComment() {
		var token = this.cacheService.get('loginUser');
		stompClient.send("/app/api/greetings/1", { "x-auth-token": token}, JSON.stringify({ 'comment': this.comment }));
	}
}

// export default class PlayerBettingMatch {
//   /* @ngInject */
//   constructor() {
//     return {
//       replace: true,
//       scope: true,
//       controller: PlayerBettingMatchController,
//       controllerAs: 'player',
//       templateUrl: 'app/components/tournament/player-betting-match/player-betting-match.html'
//     };
//   }
}