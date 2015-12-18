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