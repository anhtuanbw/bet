ngaythobet - v1.0.3 / 2016-01-13
================================
**Improvement**
* [UI] Click on link breadscrumb
* [UI] Bookmark URL
* [API] Create auto tournament from livescore.com
* [API] Check round when create match
* [API] Add image into tournament
 

**Bug Fixed** 
* [#141] System allows to create round with Name = 'null'
* [#143] System doesn't display competitor for default round (round 'Circle' ) in create new match if this tournament has another round.
* [#144] System can't show error msg when creating new match with all field is empty.


ngaythobet - v1.0.2 / 2016-01-12
================================
**Improvement**
* [UI] Update tournament UI
* [UI] Completed comment, translate language
* [API] Updated role ADMIN can view any group statistic
* [API] Group statistic - udpate getComment 

**Bug Fixed** 
* [#122] Player can view ALL group in Tournament when they just join only group in this tournament


ngaythobet - v1.0.1 / 2016-01-11
================================
**Improvement**
* [UI] Support multi language for betting match
* [UI] Update front-end about create new match, realtime, update-score, show match list
* [UI] Enhance Add flag in language select
* [API] Updated getComments API
* [API] Fix expiredtime for update betting match

**Bug Fixed** 
* [#128] Activate' button is still displayed after Mod click active button in betting match
* [#130] System does not allow Admin to bet if admin is player in group
* [#131] Team's score displays null value when user selects that match to update score
* [#132] Total loss amount is incremental when user clicks on betting group that user viewing
* [#133] System translate from EN to VI language is not correct
* [#136[ System should not displayed 'add competitor' field in update round when all competitor have been selected in round


ngaythobet - v1.0.0 / 2016-01-08
================================

* [#90 UI] Player Statistic

**Improvement**
* [API] Player Statistic
* [API] Improve get current user without query each time to DB
* [UI] Group statistic
* [UI] Create betting match authorization

**Bug Fixed** 
* [#125] "No" column displays wrong ( "Total loss amount" does NOT work did not fixed )
* [#123] Cannot displays user on user list when user search user by name
* [#121] "Activate" button is checked when page is loaded after submitting tournament adding successfully


ngaythobet - v0.0.10 / 2016-01-07
================================

* [#63 UI] Realtime Activity
* [#89 API] Player Statistic
* [#88 UI] Group statistic

**Improvement**

**Bug Fixed** 
* [#117] Error message content is incorrect 
* [#106] System return 'SERVER ERROR' when user create betting match with all fields empty


ngaythobet - v0.0.9 / 2016-01-05
================================

* [#63 API] Realtime Activity
* [#88 UI] Group statistic

**Improvement**
* [Enhancement] Search tournaments on Side bar
* [Enhancement] Support  multi-languages for login form

**Bug Fixed**
* [#107] System allows create betting match successfully when Group and Match do NOT belong to the same tournament
* [#106] System return 'SERVER ERROR' when user create betting match with all fields empty
* [#103] System should not be response error message with coding language when Moderator create betting match with invalid balance1 & balance2 
* [#104] System return duplicate data when a user is moderator of multiple group
* [#97] Can not create new group when name group is existed in another tournament
* [#95] Home page should display "Login" and "Sign up" section when page is launched at the first time
* [#86] System allows create betting match successfully when balance is not multiples of 0.25. Eg: 0.001111
* [#80] Footer is override the other elements
* [#76] Some elements/ message displays with correct language selected
* [#63] Error msg display incorrectly when user login with username/password empty


ngaythobet - v0.0.8 / 2016-01-04
================================

* [#55 API] Player Betting Match.
* [#57 API] Player History (Not optimized)
* [#60 UI] Adding members to the Group

**Improvement**
* [UI Side bar] Update autocomplete (Contain username and display name)

**Bug**
* [#81] Error message displays inconsistently when user enter tournament name with blank space in the begin and end (Fixed)
* [#79] Allowing Moderator user to update score of match (Fixed)
* [#72] System allows to create match with match's time before current time (Fixed)
* [#71] Can not create match when Admin is moderator of 2 group (Fixed)
* [#65] System allows create match when competitors do not belong to round (Fixed)


ngaythobet - v0.0.6 / 2015-12-29
================================

* [#8 API, #28 UI] Create Tournaments.
* [#9 API, #31 UI] Create Matches.
* [#10 API, #32 UI] Create Group.
* [#29 API, #30 UI] Create Round.
* [#33 UI] Home page and sidebar.
* [#52 UI] Fixture list and update score.
* [#59 API] Adding members to the Group.
* [#58 UI] Player history.
* [#52 UI] Betting match management.
**Improvement**

**Bug**
* [UI Player history] Waiting API to complete task.
* [UI Betting match management] Some areas are not active.


ngaythobet - v0.0.5 / 2015-12-29
================================

* [#8 API, #28 UI] Create Tournaments.
* [#9 API, #31 UI] Create Matches.
* [#10 API, #32 UI] Create Group.
* [#29 API, #30 UI] Create Round.
* [#33 UI] Home page and sidebar.
* [#52 UI] Fixture list and update score.
* [#59 API] Adding members to the Group.
**Improvement**

**Bug**
* Devs didn't find bugs for this release.


ngaythobet - v0.0.4 / 2015-12-24
================================

* [#8 API, #28 UI] Create Tournaments.
* [#9 API, #31 UI] Create Matches.
* [#10 API, #32 UI] Create Group.
* [#12 API] Fixture list.
* [#29 API, #30 UI] Create Round.
* [#33 UI] Home page and sidebar.


**Improvement**

**Bug**
* [API Tournament/Round/Group] System allows create name of Tournament/Round/Group when ALL character of name is blank SPACEs
* [API Match Management] System allows to create multi-matches with the same competitor in the same time


ngaythobet - v0.0.3 / 2015-12-23
================================

* [#4] Log in/Log out.
* [#13] Register User.
* [#28] Tournament Management.
* [#30] Round Management.
* [#32] Group Management.

**Improvement**


**Bug**
* [UI Login] Error message displays even though user logged in with valid account
* [UI Register User] Error message display invalid.
* [UI Tournament Management] Not work when click save button
* [UI Group Management] Get all tornaments ok but still not save group when click save



ngaythobet - v0.0.3 / 2015-12-23
================================

* [#8] Create Tournaments.
* [#9] Create Matches.
* [#10] Create Group.
* [#29] Create Round.
* [#12] Fixture list.


**Improvement**

**Bug**
* [API Tournament Management] Fixed not validation input data
* [API Group Management] Fixed not validation input data
* [API Match Management] Fixed not validation input data
* [API Round Management] Fixed not validation input data


ngaythobet - v0.0.2 / 2015-12-22
================================

* [#8] Create Tournaments.
* [#9] Create Matches.
* [#10] Create Group.
* [#29] Create Round.

**Improvement**

**Bug**
* [API Tournament Management] Not validdation when input data competitorId doesn't exist in database.
* [API Group Management] Not validdation when input data tournamentId & username doesn't exist in database.
* [API Match Management] Not validdation when input data competitorId doesn't exist in database.
* [API Round Management] Not validdation when input data competitorId & tournamentId doesn't exist in database.


ngaythobet - v0.0.1 / 2015-12-18
================================

**Feature**
* [#1, #13] Register user(s).
* [#2, #4] Log in, Log out.
* [#3, #5] Reset password.
* [#6, #7] Change password.

**Improvement**

**Bug**
* [API Register] System should not be response error message with coding language when register with invalid username or email
* [API-Register] User can register successfully when length of username < minimum length (Ex: length = 1)
* [API Register] System allows register user successfully with name is empty
* [API Register] User should be able to register username with uppercase letters
* [API Reset Password] System does not send email when user reset password
* [API Login] Error message is not clear when user login with invalid username or password