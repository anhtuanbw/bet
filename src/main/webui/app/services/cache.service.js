'use strict';

export default class CacheService {
  constructor() {
    this._cache = {};
  }

  hasLocalStorage() {
    try {
      return 'localStorage' in window && window.localStorage !== null;
    } catch (e) {
      return false;
    }
  }

  get(key) {
    if (this.hasLocalStorage()) {
      return localStorage.getItem(key) || undefined;
    } else {
      return this._cache[key] || undefined;
    }
  }

  set(key, value) {
    if (this.hasLocalStorage()) {
      localStorage.setItem(key, value);
    } else {
      this._cache[key] = value;
    }
  }

  remove(key) {
    if (this.hasLocalStorage()) {
      localStorage.removeItem(key);
    } else {
      delete this._cache[key];
    }
  }
}