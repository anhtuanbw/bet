'use strict';

export default class PlayerBettingMatchController {
	/* @ngInject */
	constructor(TournamentService, $rootScope, CacheService) {
		this.tournamentService = TournamentService;
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
		this.tournamentService.sendComment(this.comment)
			.then(response => {
				// Success
			});
	}
}
