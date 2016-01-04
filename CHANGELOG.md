ngaythobet - v0.0.7 / 2016-01-04
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