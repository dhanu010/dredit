'use strict';

var EditorState = {
    CLEAN:0, // NO CHANGES
    DIRTY:1, // UNSAVED CHANGES
    SAVE:2, // SAVE IN PROGRESS
    LOAD:3, // LOADING
    READONLY:4
};

google.load('picker', '1');

angular.module('app', ['app.filters', 'app.services', 'app.directives'])
    .constant('saveInterval', 15000)
    .constant('appId', [["903420506429-fu6c9g8rkfhjdpd6espmqqqa1m3a8ua0.apps.googleusercontent.com"]]) // Please replace this with your Application ID.
    .config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/edit/:id', {
            templateUrl: '/public/partials/editor.html',
            controller: EditorCtrl})
        .otherwise({redirectTo:'/edit/'});
}]);
